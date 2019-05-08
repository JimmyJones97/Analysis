#include <cstdio>
#include <cstdlib>
#include <sys/ptrace.h>
#include <sys/user.h>
#include <sys/wait.h>
#include <sys/mman.h>
#include <sys/syscall.h>
#include <dlfcn.h>
#include <dirent.h>
#include <unistd.h>
#include <cstring>
#include <android/log.h>
#include <elf.h>
#include <cerrno>
#include <jni.h>
#include <sys/un.h>
#include <sys/socket.h>
#include <csignal>
#include <pthread.h>

#ifdef __cplusplus
extern "C" {
#endif

#define LOG_TAG "DEBUG"
#define CLOG_TAG_GOOD "[+] [AnyHook]:"
#define CLOG_TAG_BAD  "[-] [AnyHook]:"

#define LOGD(lvl, fmt, args...) __android_log_print(lvl, \
		LOG_TAG, fmt, ##args)

const char *linker_path = "/system/bin/linker";
const char *libcso_path = "/system/lib/libc.so";
const char *libcsppo_path = "/system/lib/libc++.so";

#define CPSR_T_MASK	(1u << 5)
#define PADDING 0x100
#define SIG_SETMBP	__SIGRTMAX - 10
#define SIG_NOBLOCK	__SIGRTMAX - 9

#define ARM_DBG_READ(N, M, OP2, VAL) do {\
	asm volatile("mrc p14, 0, %0, " #N "," #M ", " #OP2 : "=r" (VAL));\
} while (0)

//---------------------------------------DumpSo SubFunc---------------------------------------------
int get_module_mem_image(pid_t pid, const char* module_name, long **startDump)
{
	int ret = 0;
    FILE* fp;
    char *pch_start;
    char *pch_end;
    char filename[32];
    char line[1024];
    long *pre_mem = nullptr;

    if(pid <= 0){
        snprintf(filename, sizeof(filename), "/proc/self/maps", pid);
    }else{
        snprintf(filename, sizeof(filename), "/proc/%d/maps", pid);
    }
    fp = fopen(filename,"r");
    if(fp != nullptr)
    {
        while(fgets(line, sizeof(line), fp))
        {
            if(strstr(line, module_name))
            {
            	/***此处存在风险，有出现内存断裂的可能，引发的结果是程序崩溃
            	//不过鉴于内存虚拟空间机制，该可能性较小，就算中途被调度可能性也不大
            	//每个进程的虚拟内存空间相对独立，故此处不出问题的赌注是底层在分配堆内存时会连续分配的机制
            	//该机制若修改或有什么隐藏因素发生时此处会失败，返回0！*/
            	pch_start = strtok(line, "-");
            	pch_end = strtok(nullptr, " ");
            	LOGD(ANDROID_LOG_DEBUG, "pch_start & pch_end : %s, %s\n", pch_start, pch_end);
            	*startDump = new long[2];

            	//添加内存断裂保护
            	if(pre_mem == NULL)
            		pre_mem = (*startDump) + 2;
            	else
            	{
            		if(pre_mem != (*startDump))
            		{
            			LOGD(ANDROID_LOG_ERROR, "Heap Mem Break !");
            			return 0;
            		}
            		else
            			pre_mem = (*startDump) + 2;
            	}

            	**startDump = strtoul(pch_start, NULL, 16);
            	LOGD(ANDROID_LOG_DEBUG, "startDump : %p, %p\n", *startDump, **startDump);
            	(*startDump)++;
            	**startDump = strtoul(pch_end, NULL, 16);
            	LOGD(ANDROID_LOG_DEBUG, "startDump : %p, %p\n", *startDump, **startDump);
            	(*startDump)++;
            	ret += 2;
            }
        }
        fclose(fp);
    }
    return ret;
}

////---------------------------------------Inject Funcs---------------------------------------------
////读取/proc目录下以id为文件夹名的文件夹内的cmdline的内容
pid_t find_pid_of(const char * process_name)
{
	int id;
	pid_t pid = -1;
	DIR * dir;
	FILE *fp;
	char filename[32];
	char cmdline[256];

	dirent * entry;

	if(process_name == 0)
		return -1;

	dir = opendir("/proc");
	if(dir == 0)
		return -1;

	while((entry = readdir(dir)) != 0)
	{
		id = atoi(entry->d_name);
		if(id != 0)
		{
			sprintf(filename, "/proc/%d/cmdline", id);
			fp = fopen(filename, "r");
			if(fp)
			{
				fgets(cmdline, sizeof(cmdline), fp);
				fclose(fp);
				if(strcmp(process_name, cmdline) == 0)
				{
					/* process found */
					pid = id;
					break;
				}
			}
		}
	}
	closedir(dir);
	return pid;
}

//解除zygote进程的阻塞状态
static void* connect_to_zygote(void* arg)
{
    int s, len;
    struct sockaddr_un remote;

    LOGD(ANDROID_LOG_DEBUG, "[+] wait 2s...\n");
    //zygote进程接收socket连接的时间间隔是500ms，2s足以保证此socket连接能连接到zygote socket
    sleep(2);

    /***
     * zygote启动后会进入一个死循环，用来接收AMS的请求连接.
     * 当没有应用启动时，zygote进程一直处于阻塞状态。
     * 所以我们后面代码中的第三次wait会无法返回，解决办法也很简单，就是主动发起一个zygote的连接。
     * 我们看到第二个waitpid后面调用了一个connect_to_zygote函数。
     */

    //创建socket套接字
    if ((s = socket(AF_UNIX, SOCK_STREAM, 0)) != -1)
    {
        // 设置连接的套接字的协议类型
        remote.sun_family = AF_UNIX;
        // 设置连接的套接字的目标
        strcpy(remote.sun_path, "/dev/socket/zygote_secondary");
        // 设置传递的参数的字节长度
        len = strlen(remote.sun_path) + sizeof(remote.sun_family);

        LOGD(ANDROID_LOG_DEBUG, "[+] start to connect zygote_secondary socket\n");
        // 向"/dev/socket/zygote"目标套接字发起连接
        connect(s, (struct sockaddr *) &remote, len);

        LOGD(ANDROID_LOG_DEBUG, "[+] close socket\n");
        // 关闭socket套接字
        close(s);
    }

    /***
     * 这个函数的功能很简单，先发起socket连接，然后再关闭连接。
     * 看上去没有做什么有用的事情，但是它却非常重要，
     * 通过连接zygote，它使zygote进程解除了阻塞状态，
     * 我们才得以注入进zygote进程。
     * 参考网址：http://zke1ev3n.me/2015/12/02/Android-so%E6%B3%A8%E5%85%A5/
     */

    return NULL ;
}

int ptrace_attach(pid_t pid, int is_Zygote)
{
	int ret = 0;
	if((ret = ptrace(PTRACE_ATTACH, pid, 0, 0)) < 0)
	{
		LOGD(ANDROID_LOG_ERROR, "[-] ptrace_attach %d !\n", ret);
		return -1;
	}

	//暂停目标进程
	waitpid(pid, 0, WUNTRACED);

	//做系统调用或者准备退出的时候暂停
	if(ptrace(PTRACE_SYSCALL, pid, 0, 0) < 0)
	{
		LOGD(ANDROID_LOG_ERROR, "[-] ptrace_syscall !\n");
		perror("ptrace_syscall");
		return -1;
	}

	//子进程暂停之后立即返回
	waitpid(pid, 0, WUNTRACED);

	//对Zygote单独处理
	if(is_Zygote)
	{
		connect_to_zygote(NULL);

		//做系统调用或者准备退出的时候暂停
		if(ptrace(PTRACE_SYSCALL, pid, 0, 0) < 0)
		{
			LOGD(ANDROID_LOG_ERROR, "[-] ptrace_syscall !\n");
			return -1;
		}

		//子进程暂停之后立即返回
		waitpid(pid, 0, WUNTRACED);
	}

	LOGD(ANDROID_LOG_DEBUG, "[+] ptrace_attach succ !\n");
	return 0;
}

//获取目标进程信号
int ptrace_setsig(pid_t pid, siginfo_t *siginfo)
{
    if(ptrace(PTRACE_SETSIGINFO, pid, 0, siginfo) < 0)
    {
    	LOGD(ANDROID_LOG_ERROR, "ptrace_setsig: Can not set signals");
        return -1;
    }

    return 0;
}

//获取目标进程信号
int ptrace_getsig(pid_t pid, siginfo_t *siginfo)
{
    if(ptrace(PTRACE_GETSIGINFO, pid, 0, siginfo) < 0)
    {
    	LOGD(ANDROID_LOG_ERROR, "ptrace_getsig: Can not get signals");
        return -1;
    }

    return 0;
}

//获取目标进程寄存器
int ptrace_getregs(pid_t pid, struct pt_regs * regs)
{
    if(ptrace(PTRACE_GETREGS, pid, 0, regs) < 0)
    {
    	LOGD(ANDROID_LOG_ERROR, "ptrace_getregs: Can not get register values");
        return -1;
    }

    return 0;
}

//设置目标进程寄存器
int ptrace_setregs(pid_t pid, struct pt_regs* regs)
{
    if(ptrace(PTRACE_SETREGS, pid, NULL, regs) < 0)
    {
    	LOGD(ANDROID_LOG_ERROR, "ptrace_setregs: Can not set register values");
        return -1;
    }
    return 0;
}

uint8_t * ptrace_retval(struct pt_regs* regs)
{
	uint8_t * uint = (uint8_t *)regs->ARM_r0;
    return uint;
}

uint8_t * ptrace_ip(struct pt_regs* regs)
{
	uint8_t * uint = (uint8_t *)regs->ARM_pc;
	return uint;
}

int ptrace_getdata(pid_t pid, uint8_t * src, uint8_t * data, size_t size)
{
	uint32_t i, j, remain;
	uint8_t *laddr;

	union u
	{
		long val;
		char chars[sizeof(long)];
	}d;

	j = size / 4;
	remain = size % 4;

	laddr = data;

	for(i = 0; i < j; i ++)
	{
		d.val = ptrace(PTRACE_PEEKTEXT, pid, src, 0);
		memcpy(laddr, d.chars, 4);

		src  += 4;
		laddr += 4;
	}

	if(remain > 0)
	{
		d.val = ptrace(PTRACE_PEEKTEXT, pid, src, 0);
		memcpy(laddr, d.chars, remain);
	}
	return 0;
}

int ptrace_writedata(pid_t pid, uint8_t * dest, uint8_t * data, size_t size)
{
    uint32_t i, j, remain;
    uint8_t *laddr;

    union u
    {
        long val;
        char chars[sizeof(long)];
    }d;

    j = size / 4;
    remain = size % 4;

    laddr = data;

    for(i = 0; i < j; i ++)
    {
        memcpy(d.chars, laddr, 4);
        ptrace(PTRACE_POKETEXT, pid, dest, d.val);

        dest  += 4;
        laddr += 4;
    }

    if(remain > 0)
    {
        d.val = ptrace(PTRACE_PEEKTEXT, pid, dest, 0);
        for (i = 0; i < remain; i ++)
        {
            d.chars[i] = *laddr++;
        }
        ptrace(PTRACE_POKETEXT, pid, dest, d.val);
    }
    return 0;
}

int ptrace_singlestep(pid_t pid, void *data)
{
    if(ptrace(PTRACE_SINGLESTEP, pid, NULL, data) < 0)
	{
    	LOGD(ANDROID_LOG_ERROR, "ptrace_singlestep");
		return -1;
	}
	return 0;
}

int ptrace_continue(pid_t pid, void *data)
{
    if(ptrace(PTRACE_CONT, pid, NULL, data) < 0)
	{
    	LOGD(ANDROID_LOG_ERROR, "ptrace_cont");
		return -1;
	}
	return 0;
}

//在目标进程中执行指定函数
int ptrace_call(pid_t pid, uint32_t addr, long * params,
		uint32_t num_params, struct pt_regs * regs)
{
    uint32_t i;

//    for(int j = 0; j < num_params; j++)
//    {
//    	LOGD(ANDROID_LOG_ERROR, "params[%d] : %p", j, params[j]);
//    }

    for(i = 0; i < num_params && i < 4; i++)
    {
        regs->uregs[i] = params[i];
    }

    // push remained params onto stack
    if (i < num_params)
    {
        //sp-4 ， 参数入栈
        regs->ARM_sp -= (num_params - i) * sizeof(long) ;
        ptrace_writedata(pid, (uint8_t *)regs->ARM_sp, (uint8_t *)&params[i],
        		(num_params - i) * sizeof(long));
    }
    //pc寄存器指向要call的地址
    regs->ARM_pc = addr;

    if (regs->ARM_pc & 1)
    {
        /*判断最后一位，如果是1就是thumb指令集，0则是arm指令集*/
        regs->ARM_pc &= (~1u);
        regs->ARM_cpsr |= CPSR_T_MASK;
    }
    else
    {
        /* arm */
        regs->ARM_cpsr &= ~CPSR_T_MASK;
    }

    regs->ARM_lr = 0; //目标进程执行完mmap之后暂停

    if(ptrace_setregs(pid, regs) == -1 || ptrace_continue(pid, NULL) == -1)
    {
    	LOGD(ANDROID_LOG_ERROR, "[-] ptrace_setregs || ptrace_continue is error.\n");
        return -1;
    }

    //等待目标进程中mmap执行完成
    waitpid(pid, NULL, WUNTRACED);

    return 0;
}

int ptrace_call_wrapper(pid_t target_pid, const char* func_name, void* func_addr,
		long* parameters, int param_num, struct pt_regs* regs)
{
	LOGD(ANDROID_LOG_DEBUG, "[+] Calling '%s' in target process.\n", func_name);
    //修改eip，运行函数
    if(ptrace_call(target_pid, (uint32_t)func_addr, parameters, param_num, regs) == -1)
        return -1;
    if(ptrace_getregs(target_pid, regs) == -1)
        return -1;
//    LOGD(ANDROID_LOG_DEBUG, "[+] Target process returned from '%s', return value = %x, pc = %x\n",
//    		func_name, ptrace_retval(regs), ptrace_ip(regs));
    return 0;
}

int ptrace_detach(pid_t pid)
{
    if(ptrace(PTRACE_DETACH, pid, NULL, 0) < 0)
	{
    	LOGD(ANDROID_LOG_ERROR, "ptrace_detach");
		return -1;
	}
	return 0;
}

void * get_module_base(pid_t pid, const char * module_name)
{
	FILE * fp;
	char filename[32];
	char line[1024];
	char * pch;
	long addr = 0;

	if(pid <= 0)
	{
		/* self process */
		snprintf(filename, sizeof(filename), "/proc/self/maps", pid);
	}
	else
	{
		snprintf(filename, sizeof(filename), "/proc/%d/maps", pid);
	}

	fp = fopen(filename, "r");

	if(fp != NULL)
	{
		while(fgets(line, sizeof(line), fp))
		{
			if(strstr(line, module_name))
			{
				pch = strtok(line, "-");
				addr = strtoul(pch, NULL, 16);

				if (addr == 0x8000)
					addr = 0;
				break;
			}
		}
		fclose(fp);
	}

	return (void *)addr;
}

//获取函数在目标进程中的地址
void * get_remote_addr(pid_t target_pid, const char * module_name, void * local_addr)
{
	void * local_handle, * remote_handle;
	//指定模块在我们自己进程中的基地址
	local_handle = get_module_base(-1, module_name);
	//指定模块在目标进程中的基地址
	remote_handle = get_module_base(target_pid, module_name);

	LOGD(ANDROID_LOG_DEBUG, "[+] get_remote_addr: local[%x], remote[%x]\n",
			local_handle, remote_handle);
	//mmap函数在目标进程的绝对地址
	void *ret_addr = (void *)((uint32_t)local_addr - (uint32_t)local_handle
			+ (uint32_t)remote_handle);

	return ret_addr;
}

int checkHasInjected(const char *record_path, pid_t target_pid, long *size, long *addr)
{
	char record[100] = {0};
	char line[100];
	char pid_str[10];
	char *pch_size;
	char *pch_mem;
	snprintf(record, 100, "%s%s", record_path, "inject_record");
	snprintf(pid_str, 10, "%d", target_pid);
	LOGD(ANDROID_LOG_DEBUG, "[+] record & pid_str: %s, %s\n", record, pid_str);

	FILE *recFile = NULL;
	if(access(record, F_OK))
	{
		recFile = fopen(record, "w");
		fclose(recFile);
		return 0;
	}

	recFile = fopen(record, "r");
	if(recFile != NULL)
	{
		while(fgets(line, sizeof(line), recFile))
		{
			if(strstr(line, pid_str))
			{
				strtok(line, ";");
				pch_size = strtok(line, ";");
				pch_mem = strtok(line, ";");
				*size = strtoul(pch_size, NULL, 10);
				*addr = strtoul(pch_mem, NULL, 16);
				LOGD(ANDROID_LOG_DEBUG, "[+] FOUND ! size & addr: %d, %p\n", size, addr);
				break;
			}
		}
		fclose(recFile);
		if(*size != 0 && *addr != 0)
			return 1;
		else
			return 0;
	}
	else
	{
		LOGD(ANDROID_LOG_ERROR, "[-] Open InjectRecord File Failed !\n");
		return -1;
	}
}

void *other_injectfunc_addr[32] = {NULL};

int inject_so(pid_t target_pid, uint8_t * map_base, const char * library_path,
		const char * func_name, int func_num, long* param, int param_size,
		struct pt_regs *original_regs, struct pt_regs *regs,
		long *mem_param, int mem_param_count, long mem_param_size[], bool need_detach)
{
	void * dlopen_addr, * dlsym_addr, * dlclose_addr,* dlerror_addr;
	long padding = 0;
	long parameters[2];
	void * sohandle = NULL;
	void * hook_entry_addr = NULL;
	int ret = -1;
	char *tmp_func = NULL;

	LOGD(ANDROID_LOG_DEBUG, "Start To Inject So !\n");
	LOGD(ANDROID_LOG_DEBUG, "library_path : %s\n", library_path);
	LOGD(ANDROID_LOG_DEBUG, "main func_name : %s\n", func_name);
	LOGD(ANDROID_LOG_DEBUG, "param_size : %d\n", param_size);

	//获得目标进程中四神函数地址
	dlopen_addr = get_remote_addr(target_pid, linker_path, (void *)dlopen);
	dlsym_addr = get_remote_addr(target_pid, linker_path, (void *)dlsym);
	dlclose_addr = get_remote_addr(target_pid, linker_path, (void *)dlclose);
	dlerror_addr = get_remote_addr(target_pid,linker_path,(void *)dlerror);
	LOGD(ANDROID_LOG_DEBUG, "[+] Get imports: dlopen: %x, dlsym: %x, dlclose: %x, dlerror: %x\n",
			dlopen_addr, dlsym_addr, dlclose_addr, dlerror_addr);

	//在目标进程分配的空间中，写入要加载的动态库路径
	ptrace_writedata(target_pid, map_base + padding,
			(uint8_t *)library_path, strlen(library_path) + 1);

	parameters[0] = (long)(map_base + padding);
	parameters[1] = RTLD_NOW | RTLD_GLOBAL;
	//调用dlopen函数，加载动态库
	if(ptrace_call_wrapper(target_pid, "dlopen", dlopen_addr, parameters, 2, regs) == -1)
		goto _quit;

	sohandle = ptrace_retval(regs);
	LOGD(ANDROID_LOG_DEBUG, "[+] sohandle is %x", sohandle);
	padding += PADDING;

	if(sohandle == NULL)
	{
		if(ptrace_call_wrapper(target_pid, "dlerror", dlerror_addr, NULL, 0, regs) == -1)
			goto _quit;

		void *error_inf = ptrace_retval(regs);

		char data[256] = {0};
		ptrace_getdata(target_pid, (uint8_t *)error_inf, (uint8_t *)data, 256);

		LOGD(ANDROID_LOG_DEBUG, "[+] Error Data is %s", data);

		ptrace_setregs(target_pid, original_regs); //还原寄存器
		ptrace_detach(target_pid); //关闭

		goto _quit;
	}

	//将动态库中函数的名称写入分配地址+0x100的地方
	tmp_func = (char *)func_name;
	for(int i = 0; i < func_num; i++)
	{
		ptrace_writedata(target_pid, map_base + padding, (uint8_t *)tmp_func, strlen(tmp_func) + 1);
		tmp_func += strlen(tmp_func) + 1;
		padding += PADDING;
	}

	padding -= func_num * PADDING;

	for(int i = 0; i < func_num; i++)
	{
		parameters[0] = (long)sohandle;
		parameters[1] = (long)(map_base + padding);
		LOGD(ANDROID_LOG_DEBUG, "[+] para0 is %x, para1 is %x", parameters[0], parameters[1]);

		//调用dlsym，获得动态库中hook_entry的地址
		if(ptrace_call_wrapper(target_pid, "dlsym", dlsym_addr, parameters, 2, regs) == -1)
			goto _quit;

		if(i == 0)
			hook_entry_addr = ptrace_retval(regs);
		else
		{
			other_injectfunc_addr[i - 1] = ptrace_retval(regs);
		}
		LOGD(ANDROID_LOG_DEBUG, "[+] hook_entry_addr = %p\n", hook_entry_addr);
		padding += PADDING;
	}

	if(mem_param_count != 0)
	{
		long *func_params = new long[mem_param_count + param_size];
		//将动态库中函数的名称写入分配地址padding + PADDING的地方
		for(int i = 0; i < mem_param_count; i++)
		{
			ptrace_writedata(target_pid, map_base + padding,
					(uint8_t *)mem_param, mem_param_size[i]);
			func_params[i] = (long)(map_base + padding);
			padding += PADDING;
			LOGD(ANDROID_LOG_DEBUG, "[+] mem_para[%d] %s [at] %p",
					i, (char *)mem_param, func_params);
			mem_param += mem_param_size[i];
		}

		//将mem_param复原，否则delete [] mem_param时会出错
		for(int i = 0; i < mem_param_count; i++)
			mem_param -= mem_param_size[i];

		for(int i = 0; i < param_size; i++)
		{
			func_params[i + mem_param_count] = *param;
			param++;
		}

		//调用注入的动态库中hook_entry函数
		if(ptrace_call_wrapper(target_pid, func_name, hook_entry_addr,
				(long *)func_params, mem_param_count + param_size, regs) == -1)
			goto _quit;
	}
	else
	{
		//调用注入的动态库中hook_entry函数
		if(ptrace_call_wrapper(target_pid, func_name, hook_entry_addr,
				param, param_size, regs) == -1)
			goto _quit;
	}

	ptrace_setregs(target_pid, original_regs); //还原寄存器
	if(need_detach)
	{
		ptrace_detach(target_pid); //关闭
	}

	ret = 0;
_quit:
	return ret;
}

long inject_remote_process(pid_t target_pid, const char *record_path,
		int is_Zygote, struct pt_regs *original_regs, struct pt_regs *regs)
{
	long ret = -1;
	void * mmap_addr;
	uint8_t * map_base;
	long parameters[10];
	long size = 0, addr = 0;
	int checkInjectRetCode = 0;

	LOGD(ANDROID_LOG_DEBUG, "[+] Injecting process: %d\n", target_pid);

	/*attach到指定进程*/
	if(ptrace_attach(target_pid, is_Zygote) == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "[-] ptrace_attach failed !\n");
		goto _quit;
	}

	/*获得进程寄存器*/
	if(ptrace_getregs(target_pid, regs) == -1)
		goto _quit;

	/*保存进程寄存器值*/
	memcpy(original_regs, regs, sizeof(struct pt_regs));

	mmap_addr = get_remote_addr(target_pid, libcso_path, (void *)mmap);
	LOGD(ANDROID_LOG_DEBUG, "[+] Remote 'mmap' address: %x\n", mmap_addr);

	if(record_path == NULL)
		checkInjectRetCode = 0;
	else
		checkInjectRetCode = checkHasInjected(record_path, target_pid, &size, &addr);
	if(checkInjectRetCode == -1)
	{
		ptrace_detach(target_pid);
		goto _quit;
	}
	else if(checkInjectRetCode == 0)
	{
		/*调用mmap分配内存空间*/
		parameters[0] = 0;    // addr
		parameters[1] = 0x4000; // size
		parameters[2] = PROT_READ | PROT_WRITE | PROT_EXEC;  // prot
		parameters[3] = MAP_ANONYMOUS | MAP_PRIVATE; // flags
		parameters[4] = 0; //fd
		parameters[5] = 0; //offset

		//调用mmap在目标进程中分配内存空间
		if(ptrace_call_wrapper(target_pid, "mmap", mmap_addr, parameters, 6, regs) == -1)
			goto _quit;

		map_base = ptrace_retval(regs);  //取回分配的地址
		LOGD(ANDROID_LOG_DEBUG, "[+] map_base is %x", map_base);
	}
	else if(checkInjectRetCode == 1)
	{
		map_base = (uint8_t *)addr;
		LOGD(ANDROID_LOG_DEBUG, "[+] map_base is %x", map_base);
	}

	ret = (long) map_base;
_quit:
	return ret;
}

void *make_mem_params(long param_size[], int count...)
{
	va_list arg_ptr;
	va_start(arg_ptr, count);
	long *param_ptr = new long[count];
	long total_size = 0;
	long *param_zip = NULL;

	for(int i = 0; i < count; i++)
	{
		param_size[i] = va_arg(arg_ptr, long);
		param_ptr[i] = va_arg(arg_ptr, long);
		total_size += param_size[i];
	}
	param_zip = new long[total_size];
	memset(param_zip, 0, total_size);
	for(int i = 0; i < count; i++)
	{
		memcpy(param_zip, (void *)(param_ptr[i]), (size_t)param_size[i]);
		param_zip += param_size[i];
	}
	param_zip -= total_size;

	delete [] param_ptr;

	return param_zip;
}

//---------------------------------------SetMBP ThreadFunc---------------------------------------------
u_int32_t addr = 0;

void *setmbp_d(void *arg)
{
	char buf[256];
	pid_t pid = (pid_t)arg;

	while(fgets(buf, 256, stdin) != NULL)
	{
		if(buf[strlen(buf) - 1] == '\n')
			buf[strlen(buf) - 1] = 0;
		char *str = strstr(buf, "stop ");
		if(str != 0)
		{
			str += 5;
			addr =  strtoul(str, NULL, 16);
			printf("stop at %s, addr is %p!\n", str, addr);
			int ret = kill(pid, SIG_SETMBP);
			printf("ret is: %d, errno is: %s!\n", ret, strerror(errno));
		}
		else if(strcmp(buf, "start") == 0)
			printf("start!\n");
	}
	return NULL;
}

void SetMBP(pid_t target_pid, struct pt_regs *original_regs, struct pt_regs *regs, uint8_t *map_base)
{
	siginfo_t siginfo;
	unsigned int val;
	long ret = 0;
	u_int32_t didr = 0;

	LOGD(ANDROID_LOG_ERROR, "sizeof(siginfo) %d !", sizeof(siginfo));
	memset(&siginfo, 0, sizeof(siginfo));

	if(ptrace_getsig(target_pid, &siginfo) == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "Can't ptrace_getsig !");
		goto _quit;
	}

	LOGD(ANDROID_LOG_VERBOSE, "[+] 'signo111' is %d !\n", siginfo.si_signo);
	LOGD(ANDROID_LOG_VERBOSE, "[+] 'signo111._pid' is %d !\n", siginfo._sifields._sigchld._pid);

	//在目标进程分配的空间中，写入要下断点的地址
	ptrace_writedata(target_pid, map_base, (uint8_t *)&addr, 4);

	//检测HBP
	errno = 0;
	ret = ptrace (PTRACE_GETHBPREGS, target_pid, 0, &val);
	LOGD(ANDROID_LOG_VERBOSE, "[+] ret is: %d, errno is: %s !\n", ret, strerror(errno));
//	LOGD(ANDROID_LOG_DEBUG, "ret is: %d, errno is: %s!\n", ret, strerror(errno));

	ARM_DBG_READ(c0, c0, 0, didr);
	LOGD(ANDROID_LOG_VERBOSE, "[+] didr is: %d !\n", didr);

	//放行调试进程
	if(ptrace_continue(target_pid, (void *)siginfo.si_signo) == -1)
		LOGD(ANDROID_LOG_ERROR, "[-] ptrace_continue Error !");
	else
		LOGD(ANDROID_LOG_DEBUG, "[+] ptrace_continue !");
	waitpid(target_pid, NULL, WUNTRACED);

_quit:
	return;
}

void addr_not_match(pid_t target_pid, struct pt_regs *original_regs, struct pt_regs *regs)
{
//	LOGD(ANDROID_LOG_ERROR, "[+] addr_not_match start !");
	long parameters[1] = {0};
	/*获得进程寄存器*/
	if(ptrace_getregs(target_pid, regs) == -1)
		goto _quit;
	/*保存进程寄存器值*/
	memcpy(original_regs, regs, sizeof(struct pt_regs));

	//远程调用mprotectchange
	parameters[0] = PROT_EXEC | PROT_READ | PROT_WRITE;
	if(ptrace_call_wrapper(target_pid, "mprotectchange", other_injectfunc_addr[0], parameters, 1, regs) == -1)
		goto _quit;

	//单步执行
	ptrace_setregs(target_pid, original_regs); //还原寄存器
	LOGD(ANDROID_LOG_ERROR, "[+] original_regs : PC is = %p !", original_regs->ARM_pc);
	if(ptrace_singlestep(target_pid, NULL) == -1)
		LOGD(ANDROID_LOG_ERROR, "[-] ptrace_singlestep Error !");
//	else
//		LOGD(ANDROID_LOG_DEBUG, "[+] ptrace_singlestep !");
	waitpid(target_pid, NULL, WUNTRACED);

	/*再次获得进程寄存器*/
	if(ptrace_getregs(target_pid, regs) == -1)
		goto _quit;
	/*保存进程寄存器值*/
	memcpy(original_regs, regs, sizeof(struct pt_regs));

	//再次远程调用mprotectchange
	parameters[0] = PROT_EXEC | PROT_READ;
	if(ptrace_call_wrapper(target_pid, "mprotectchange", other_injectfunc_addr[0], parameters, 1, regs) == -1)
		goto _quit;

	//放行进程
	ptrace_setregs(target_pid, original_regs); //还原寄存器
	if(ptrace_continue(target_pid, NULL) == -1)
		LOGD(ANDROID_LOG_ERROR, "[-] ptrace_continue Error !");
//	else
//		LOGD(ANDROID_LOG_DEBUG, "[+] ptrace_continue !");
	waitpid(target_pid, NULL, WUNTRACED);

_quit:
	return;
}

void addr_matched(pid_t target_pid, struct pt_regs *original_regs, struct pt_regs *regs)
{
	LOGD(ANDROID_LOG_ERROR, "[+] addr_matched start !");
}

void GetSignal(pid_t target_pid, struct pt_regs *original_regs, struct pt_regs *regs)
{
	siginfo_t siginfo;
	u_int32_t start, end = 0;
	uint32_t page_size = getpagesize();

	start = (addr) & (~(page_size-1));
	end = start + page_size;

	while(1)
	{
		memset(&siginfo, 0, sizeof(siginfo));
		if(ptrace_getsig(target_pid, &siginfo) == -1)
		{
			LOGD(ANDROID_LOG_ERROR, "Can't ptrace_getsig !");
			goto _quit;
		}
//		LOGD(ANDROID_LOG_VERBOSE, "[+] 'signo111' is %d !\n", siginfo.si_signo);
//		LOGD(ANDROID_LOG_VERBOSE, "[+] '_sigfault._addr' is %p !\n", siginfo._sifields._sigfault._addr);

		if(siginfo.si_signo != SIGSEGV || (u_int32_t)siginfo._sifields._sigfault._addr >= end ||
				(u_int32_t)siginfo._sifields._sigfault._addr < start)
		{
			LOGD(ANDROID_LOG_ERROR, "[-] Not My Business !");
			if(ptrace_continue(target_pid, (void *)siginfo.si_signo) == -1)
				LOGD(ANDROID_LOG_ERROR, "[-] ptrace_continue Error !");
			else
				LOGD(ANDROID_LOG_DEBUG, "[+] ptrace_continue !");
			waitpid(target_pid, NULL, WUNTRACED);
		}
		else if((u_int32_t)siginfo._sifields._sigfault._addr != addr/* ||
				(u_int32_t)siginfo._sifields._sigfault._addr != addr + 1 ||
				(u_int32_t)siginfo._sifields._sigfault._addr != addr + 2 ||
				(u_int32_t)siginfo._sifields._sigfault._addr != addr + 3*/)
		{
			addr_not_match(target_pid, original_regs, regs);
		}
		else
		{
			LOGD(ANDROID_LOG_VERBOSE, "[+] YES ! '_sigfault._addr' is %p !\n", siginfo._sifields._sigfault._addr);
			addr_matched(target_pid, original_regs, regs);
			break;
		}
	}

_quit:
	return;
}

//---------------------------------------Main Func---------------------------------------------
int DumpSo(int argc, char *argv[])
{
	if(argc != 5)
		return -1;

	char *pid_str = argv[2];
	int pid = atoi(pid_str);
	char *module_name = argv[3];
	char *toFilePath = argv[4];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "pid : %d, module_name : %s, toFilePath : %s !\n",
			pid, module_name, toFilePath);

	//构建参数
	long *startMem = NULL;
	int count = get_module_mem_image(pid, module_name, &startMem);
	if(count == 0)
	{
		LOGD(ANDROID_LOG_ERROR, "Can't Find Module In MEM !");
		return 1;
	}
	startMem -= count;
	for(int i = 0; i < count; i++)
		LOGD(ANDROID_LOG_DEBUG, "startMem[%d] : %p\n", i, startMem[i]);

	//start hook
	if(pid == find_pid_of("zygote") || pid == find_pid_of("zygote64"))
		isZygote = 1;
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	//制作堆参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆传递的参数
	long mem_params_size[1] = {0};
	long *mem_params = (long *) make_mem_params(mem_params_size, 1,
			strlen(toFilePath), toFilePath);
	LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);

//	char *tmp = strstr(toFilePath, "/files/");
//	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s%s", toFilePath, "libInjectUtils.so");
	//制作寄存器或栈参数，可使用寄存器或栈传递的参数（不同于堆参数）
	long *params = new long[count + 1];
	params[0] = count;
	LOGD(ANDROID_LOG_DEBUG, "params[0] : %p\n", params[0]);
	for(int i = 0; i < count; i++)
	{
		params[i + 1] = startMem[i];
		LOGD(ANDROID_LOG_DEBUG, "params[%d] : %p\n", i + 1, params[i + 1]);
	}

	if(inject_so(pid, map_base, inject_so_path, "dump_entry", 1,
			params, count + 1, &original_regs, &regs,
			mem_params, 1, mem_params_size, true) == -1)
	{
		delete [] startMem;
		delete [] params;
		delete [] mem_params;
		return 1;
	}

	delete [] startMem;
	delete [] params;
	delete [] mem_params;
	return 0;
}

int DumpSo2(int argc, char *argv[])
{
	if(argc != 5)
		return -1;

	char *pid_str = argv[2];
	int pid = atoi(pid_str);
	char *start_mem_str = argv[3];
	char *end_mem_str = argv[4];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "pid : %d, start_mem_str : %s, end_mem_str : %s !\n",
			pid, start_mem_str, end_mem_str);

	//start hook
	char *toFilePath = "/data/abu/";
	if(pid == find_pid_of("zygote") || pid == find_pid_of("zygote64"))
		isZygote = 1;
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	//制作堆参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆传递的参数
	long mem_params_size[1] = {0};
	long *mem_params = (long *) make_mem_params(mem_params_size, 1,
			strlen(toFilePath), toFilePath);
	LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);

//	char *tmp = strstr(toFilePath, "/files/");
//	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s%s", toFilePath, "libInjectUtils.so");
	//制作寄存器或栈参数，可使用寄存器或栈传递的参数（不同于堆参数）
	int count = 2;
	long *params = new long[count + 1];
	params[0] = count;
	LOGD(ANDROID_LOG_DEBUG, "params[0] : %p\n", params[0]);
	params[1] = strtoul(start_mem_str, NULL, 16);
	params[2] = strtoul(end_mem_str, NULL, 16);
	LOGD(ANDROID_LOG_DEBUG, "params[1] : %p\n", params[1]);
	LOGD(ANDROID_LOG_DEBUG, "params[2] : %p\n", params[2]);

	if(inject_so(pid, map_base, inject_so_path, "dump_entry", 1,
			params, count + 1, &original_regs, &regs,
			mem_params, 1, mem_params_size, true) == -1)
	{
		delete [] params;
		delete [] mem_params;
		return 1;
	}

	LOGD(ANDROID_LOG_DEBUG, "DumpSo2 Done !!!\n");

	delete [] params;
	delete [] mem_params;
	return 0;
}

int HookDlopen(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 1;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of("zygote");
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_dlopen_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookDlopen2(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *pid_str = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			pid_str, toFilePath);

	//start hook
	pid_t pid = atoi(pid_str);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

//	char *tmp = strstr(toFilePath, "/files/");
//	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_dlopen_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookDlopen3(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of(process_name);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

//	char *tmp = strstr(toFilePath, "/files/");
//	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_dlopen_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookSYZH(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of(process_name);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_SYZH_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookHJZG(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 1;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of("zygote");
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_HJZG_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookSYZHTW(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 1;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of("zygote");
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_SYZHTW_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookQMCS(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 1;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of("zygote");
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_QMCS_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookJNBG(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of(process_name);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_JNBG_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookYSQ(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 1;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of("zygote");
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_YSQ_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int CheckHWBP(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *pid_str = argv[2];
	int pid = atoi(pid_str);
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "pid : %d, toFilePath : %s !\n",
			pid, toFilePath);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	unsigned int val;
	long ret = 0;

	ret = ptrace (PTRACE_GETHBPREGS, pid, 0, &val);
	LOGD(ANDROID_LOG_DEBUG, "ret : %d\n", ret);
	return 1;
//	return 0;
}

int XorWRITE(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of(process_name);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");

	//制作寄存器或栈参数，可使用寄存器或栈传递的参数（不同于堆参数）
	long params = (long)map_base;
	LOGD(ANDROID_LOG_DEBUG, "params : %p\n", params);

	char func_name[256] = {0};
	char *tem_func = func_name;
	char name1[] = "initMBP_entry";
	char name2[] = "mprotectchange";
	memcpy(tem_func, name1, strlen(name1));
	tem_func += strlen(name1) + 1;
	memcpy(tem_func, name2, strlen(name2));

	if(inject_so(pid, map_base, inject_so_path, func_name, 2,
			&params, 1, &original_regs, &regs,
			NULL, 0, NULL, false) == -1)
	{
		return 1;
	}

	//开启线程等待命令
	pthread_t tid;
	int err = 0;
	err = pthread_create(&tid, NULL, setmbp_d, (void *)pid);
	if(err != 0)
		LOGD(ANDROID_LOG_ERROR, "[-] pthread_create Error !");
	else
		LOGD(ANDROID_LOG_ERROR, "[+] pthread_create !");

	//放行调试进程
	if(ptrace_continue(pid, NULL) == -1)
		LOGD(ANDROID_LOG_ERROR, "[-] ptrace_continue Error !");
	else
		LOGD(ANDROID_LOG_DEBUG, "[+] ptrace_continue !");
	waitpid(pid, NULL, WUNTRACED);

	SetMBP(pid, &original_regs, &regs, map_base);
	GetSignal(pid, &original_regs, &regs);

	LOGD(ANDROID_LOG_DEBUG, "Get child's Signal !");

	return 0;
}

int HookOpen(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of(process_name);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

//	char *tmp = strstr(toFilePath, "/files/");
//	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_open_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookOpen2(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_id = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_id, toFilePath);

	//start hook
	pid_t pid = atoi(process_id);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

//	char *tmp = strstr(toFilePath, "/files/");
//	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_open_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int SetMemBP(int argc, char *argv[])
{
	if(argc != 5)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	char *soName = argv[4];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of(process_name);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	//制作堆参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆传递的参数
	long mem_params_size[1] = {0};
	long *mem_params = (long *) make_mem_params(mem_params_size, 1,
			strlen(soName), soName);
	LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "set_memBP_entry", 1,
			NULL, 0, &original_regs, &regs,
			mem_params, 1, mem_params_size, true) == -1)
	{
		return 1;
	}

	return 0;
}

int Hook12306(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_pid = argv[2];
	char *hook_point = argv[3];

	uint32_t point = 0;
	sscanf(hook_point, "%x", &point);
	point += 0x1000;
	pid_t pid = atoi(process_pid);
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_pid : %s !\n", process_pid);
	LOGD(ANDROID_LOG_DEBUG, "hook_point : %p !\n", point);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, NULL,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *inject_so_path = "/data/temp_12306/libInjectUtils.so";
	if(inject_so(pid, map_base, inject_so_path, "hook_12306_entry", 1,
			(long *)&point, 1, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookAny2(int argc, char *argv[])
{
	if(argc != 6)
		return -1;

	printf("[+] HookApk : HookAny2 at %p\n", HookAny2);

	char *pid_str = argv[2];
	char *moudle_name = argv[3];
	char *hook_point = argv[4];
	char *hook_length = argv[5];

	pid_t pid = 0;
	sscanf(pid_str, "%d", &pid);

//	pid_t pid = find_pid_of(process_name);
	int isZygote = 0;
//	if(strstr(process_name, "zygote") != 0
//		|| strstr(process_name, "zygote64") != 0
//		|| strstr(process_name, "Zygote") != 0
//		|| strstr(process_name, "Zygote64") != 0)
//	{
//		isZygote = 1;
//	}
	uint32_t point = 0;
	sscanf(hook_point, "%x", &point);
	uint32_t length = 0;
	sscanf(hook_length, "%d", &length);

	LOGD(ANDROID_LOG_DEBUG, "process_pid : %d !\n", pid);
	LOGD(ANDROID_LOG_DEBUG, "isZygote : %d !\n", isZygote);
	LOGD(ANDROID_LOG_DEBUG, "moudle_name : %s !\n", moudle_name);
	LOGD(ANDROID_LOG_DEBUG, "hook_point : %p !\n", point);
	LOGD(ANDROID_LOG_DEBUG, "hook_length : %d !\n", length);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, NULL,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "[-] inject_remote_process FAILED !");
		return 1;
	}

	//制作内存参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆内存传递的参数
	long mem_params_size[1] = {0};
	long *mem_params = (long *) make_mem_params(mem_params_size, 1,
			strlen(moudle_name), moudle_name);
	LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);

	//制作寄存器或栈参数，可使用寄存器或栈传递的参数（不同于堆参数）
	int count = 2;
	long *params = new long[count + 1];
	params[0] = count;
	LOGD(ANDROID_LOG_DEBUG, "params[0] : %p\n", params[0]);
	params[1] = point;
	params[2] = length;
	LOGD(ANDROID_LOG_DEBUG, "params[%d] : %p\n", 1, params[1]);
	LOGD(ANDROID_LOG_DEBUG, "params[%d] : %p\n", 2, params[2]);

	char *inject_so_path = "/data/cs/libInjectUtils.so";
	if(inject_so(pid, map_base, inject_so_path, "hook_any_entry", 1,
			params, count + 1, &original_regs, &regs,
			mem_params, 1, mem_params_size, true) == -1)
	{
		delete [] params;
		delete [] mem_params;
		return 1;
	}

	delete [] params;
	delete [] mem_params;
	return 0;
}

int HookAny(int argc, char *argv[])
{
	if(argc != 6)
		return -1;

	printf("[+] HookApk : HookAny at %p\n", HookAny);

	char *process_name = argv[2];
	char *moudle_name = argv[3];
	char *hook_point = argv[4];
	char *hook_length = argv[5];

	pid_t pid = find_pid_of(process_name);
	int isZygote = 0;
	if(strstr(process_name, "zygote") != 0
		|| strstr(process_name, "zygote64") != 0
		|| strstr(process_name, "Zygote") != 0
		|| strstr(process_name, "Zygote64") != 0)
	{
		isZygote = 1;
	}
	uint32_t point = 0;
	sscanf(hook_point, "%x", &point);
	uint32_t length = 0;
	sscanf(hook_length, "%d", &length);

	LOGD(ANDROID_LOG_DEBUG, "process_pid : %d !\n", pid);
	LOGD(ANDROID_LOG_DEBUG, "isZygote : %d !\n", isZygote);
	LOGD(ANDROID_LOG_DEBUG, "moudle_name : %s !\n", moudle_name);
	LOGD(ANDROID_LOG_DEBUG, "hook_point : %p !\n", point);
	LOGD(ANDROID_LOG_DEBUG, "hook_length : %d !\n", length);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, NULL,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "[-] inject_remote_process FAILED !");
		return 1;
	}

	//制作内存参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆内存传递的参数
	long mem_params_size[1] = {0};
	long *mem_params = (long *) make_mem_params(mem_params_size, 1,
			strlen(moudle_name), moudle_name);
	LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);

	//制作寄存器或栈参数，可使用寄存器或栈传递的参数（不同于堆参数）
	int count = 2;
	long *params = new long[count + 1];
	params[0] = count;
	LOGD(ANDROID_LOG_DEBUG, "params[0] : %p\n", params[0]);
	params[1] = point;
	params[2] = length;
	LOGD(ANDROID_LOG_DEBUG, "params[%d] : %p\n", 1, params[1]);
	LOGD(ANDROID_LOG_DEBUG, "params[%d] : %p\n", 2, params[2]);

	char *inject_so_path = "/data/cs/libInjectUtils.so";
	if(inject_so(pid, map_base, inject_so_path, "hook_any_entry", 1,
			params, count + 1, &original_regs, &regs,
			mem_params, 1, mem_params_size, true) == -1)
	{
		delete [] params;
		delete [] mem_params;
		return 1;
	}

	delete [] params;
	delete [] mem_params;
	return 0;
}

int StaticMethodId(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of(process_name);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_staticMethodId_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookTest(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s, toFilePath : %s !\n",
			process_name, toFilePath);

	//start hook
	pid_t pid = find_pid_of(process_name);
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_testfunc_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int ScanMemory(int argc, char *argv[])
{
	LOGD(ANDROID_LOG_WARN, "ScanMemory !");

	if(argc != 4)
		return -1;

	char *pid_str = argv[2];
	char *num_str = argv[3];

	pid_t pid = 0;
	sscanf(pid_str, "%d", &pid);
	int number = 0;
	sscanf(num_str, "%d", &number);

	int isZygote = 0;
	LOGD(ANDROID_LOG_DEBUG, "pid : %d, number : %d !\n",
			pid, number);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, NULL,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *inject_so_path = "/data/cs/libInjectUtils.so";
	if(inject_so(pid, map_base, inject_so_path, "hook_scanmemory_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookPtrace(int argc, char *argv[])
{
	if(argc != 4)
		return -1;

	char *process_name = argv[2];
	char *toFilePath = argv[3];
	int isZygote = 0;
	pid_t pid = find_pid_of(process_name);
	LOGD(ANDROID_LOG_DEBUG, "process_name : %s : %d, toFilePath : %s !\n",
			process_name, pid, toFilePath);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, toFilePath,
			isZygote, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		return 1;
	}

	char *tmp = strstr(toFilePath, "/files/");
	*tmp = 0;
	char inject_so_path[100] = {0};
	snprintf(inject_so_path, 100, "%s/lib/%s", toFilePath, "libInjectUtils.so");
	if(inject_so(pid, map_base, inject_so_path, "hook_ptrace_entry", 1,
			NULL, 0, &original_regs, &regs,
			NULL, 0, NULL, true) == -1)
	{
		return 1;
	}

	return 0;
}

int HookAddrPid(int argc, char *argv[])
{
	if(argc < 4)
		return -1;

	char *pid_str = argv[2];
	char *addr_str = argv[3];
	char *func_so_name = NULL;
	if(argc == 5)
		func_so_name = argv[4];

	LOGD(ANDROID_LOG_DEBUG, "pid : %s, addr : %s\n", pid_str, addr_str);
	printf("%s pid : %s, addr : %s\n", CLOG_TAG_GOOD, pid_str, addr_str);

	pid_t pid = 0;
	sscanf(pid_str, "%d", &pid);
	uint32_t addr = 0;
	sscanf(addr_str, "%x", &addr);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, NULL,
			0, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		printf("%s inject_remote_process FAILED !\n", CLOG_TAG_BAD);
		return 1;
	}

	if(func_so_name != NULL)
	{
		//制作内存参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆内存传递的参数
		int count = 3;
		long mem_params_size[3] = {0};
		long *mem_params = (long *) make_mem_params(mem_params_size, 3,
				4, &count,
				4, &addr,
				strlen(func_so_name), func_so_name);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[1] : %p\n", mem_params_size[1]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[2] : %p\n", mem_params_size[2]);
		printf("%s mem_params_size[0] : %p\n", CLOG_TAG_GOOD, mem_params_size[0]);
		printf("%s mem_params_size[1] : %p\n", CLOG_TAG_GOOD, mem_params_size[1]);
		printf("%s mem_params_size[2] : %p\n", CLOG_TAG_GOOD, mem_params_size[2]);

		char inject_so_path[100] = "/data/cs/libsupporter.so";
		if(inject_so(pid, map_base, inject_so_path, "support_entry", 1,
				NULL, 0, &original_regs, &regs,
				mem_params, count, mem_params_size, true) == -1)
		{
			return 1;
		}
		return 0;
	}
	else
	{
		//制作内存参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆内存传递的参数
		int count = 2;
		long mem_params_size[2] = {0};
		long *mem_params = (long *) make_mem_params(mem_params_size, 2,
				4, &count,
				4, &addr);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[1] : %p\n", mem_params_size[1]);
		printf("%s mem_params_size[0] : %p\n", CLOG_TAG_GOOD, mem_params_size[0]);
		printf("%s mem_params_size[1] : %p\n", CLOG_TAG_GOOD, mem_params_size[1]);

		char inject_so_path[100] = "/data/cs/libsupporter.so";
		if(inject_so(pid, map_base, inject_so_path, "support_entry", 1,
				NULL, 0, &original_regs, &regs,
				mem_params, count, mem_params_size, true) == -1)
		{
			return 1;
		}
		return 0;
	}
}

int HookAddr(int argc, char *argv[])
{
	if(argc < 4)
		return -1;

	char *proc_str = argv[2];
	char *addr_str = argv[3];
	char *func_so_name = NULL;
	if(argc == 5)
		func_so_name = argv[4];

	LOGD(ANDROID_LOG_DEBUG, "proc : %s, addr : %s\n", proc_str, addr_str);
	printf("%s proc : %s, addr : %s\n", CLOG_TAG_GOOD, proc_str, addr_str);

	pid_t pid = find_pid_of(proc_str);
	uint32_t addr = 0;
	sscanf(addr_str, "%x", &addr);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, NULL,
			0, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		printf("%s inject_remote_process FAILED !\n", CLOG_TAG_BAD);
		return 1;
	}

	if(func_so_name != NULL)
	{
		//制作内存参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆内存传递的参数
		int count = 3;
		long mem_params_size[3] = {0};
		long *mem_params = (long *) make_mem_params(mem_params_size, 3,
				4, &count,
				4, &addr,
				strlen(func_so_name), func_so_name);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[1] : %p\n", mem_params_size[1]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[2] : %p\n", mem_params_size[2]);
		printf("%s mem_params_size[0] : %p\n", CLOG_TAG_GOOD, mem_params_size[0]);
		printf("%s mem_params_size[1] : %p\n", CLOG_TAG_GOOD, mem_params_size[1]);
		printf("%s mem_params_size[2] : %p\n", CLOG_TAG_GOOD, mem_params_size[2]);

		char inject_so_path[100] = "/data/cs/libsupporter.so";
		if(inject_so(pid, map_base, inject_so_path, "support_entry", 1,
				NULL, 0, &original_regs, &regs,
				mem_params, count, mem_params_size, true) == -1)
		{
			return 1;
		}
		return 0;
	}
	else
	{
		//制作内存参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆内存传递的参数
		int count = 2;
		long mem_params_size[2] = {0};
		long *mem_params = (long *) make_mem_params(mem_params_size, 2,
				4, &count,
				4, &addr);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[1] : %p\n", mem_params_size[1]);
		printf("%s mem_params_size[0] : %p\n", CLOG_TAG_GOOD, mem_params_size[0]);
		printf("%s mem_params_size[1] : %p\n", CLOG_TAG_GOOD, mem_params_size[1]);

		char inject_so_path[100] = "/data/cs/libsupporter.so";
		if(inject_so(pid, map_base, inject_so_path, "support_entry", 1,
				NULL, 0, &original_regs, &regs,
				mem_params, count, mem_params_size, true) == -1)
		{
			return 1;
		}
		return 0;
	}
}

int HookModu(int argc, char *argv[])
{
	if(argc < 5)
		return -1;

	char *proc_str = argv[2];
	char *modu_str = argv[3];
	char *addr_str = argv[4];
	char *func_so_name = NULL;
	if(argc == 5)
		func_so_name = argv[5];

	LOGD(ANDROID_LOG_DEBUG, "proc : %s, modu : %s, addr : %s\n", proc_str, modu_str, addr_str);
	printf("proc : %s, modu : %s, addr : %s\n", CLOG_TAG_GOOD, proc_str, modu_str, addr_str);

	pid_t pid = find_pid_of(proc_str);
	uint32_t addr = 0;
	sscanf(addr_str, "%x", &addr);

	//start hook
	struct pt_regs original_regs, regs;
	uint8_t * map_base = (uint8_t *) inject_remote_process(pid, NULL,
			0, &original_regs, &regs);
	if((long)map_base == -1)
	{
		LOGD(ANDROID_LOG_ERROR, "inject_remote_process FAILED !");
		printf("%s inject_remote_process FAILED !\n", CLOG_TAG_BAD);
		return 1;
	}

	if(func_so_name != NULL)
	{
		//制作内存参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆内存传递的参数
		int count = 3;
		long mem_params_size[3] = {0};
		long *mem_params = (long *) make_mem_params(mem_params_size, 3,
				4, &count,
				4, &addr,
				strlen(func_so_name), func_so_name);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[1] : %p\n", mem_params_size[1]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[2] : %p\n", mem_params_size[2]);
		printf("%s mem_params_size[0] : %p\n", CLOG_TAG_GOOD, mem_params_size[0]);
		printf("%s mem_params_size[1] : %p\n", CLOG_TAG_GOOD, mem_params_size[1]);
		printf("%s mem_params_size[2] : %p\n", CLOG_TAG_GOOD, mem_params_size[2]);

		char inject_so_path[100] = "/data/cs/libsupporter.so";
		if(inject_so(pid, map_base, inject_so_path, "support_entry", 1,
				NULL, 0, &original_regs, &regs,
				mem_params, count, mem_params_size, true) == -1)
		{
			return 1;
		}
		return 0;
	}
	else
	{
		//制作内存参数，mem_param，该类参数需要在调用hook入口函数时写入目标进程的内存中，即一定是使用堆内存传递的参数
		int count = 2;
		long mem_params_size[2] = {0};
		long *mem_params = (long *) make_mem_params(mem_params_size, 2,
				4, &count,
				4, &addr);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[0] : %p\n", mem_params_size[0]);
		LOGD(ANDROID_LOG_DEBUG, "mem_params_size[1] : %p\n", mem_params_size[1]);
		printf("%s mem_params_size[0] : %p\n", CLOG_TAG_GOOD, mem_params_size[0]);
		printf("%s mem_params_size[1] : %p\n", CLOG_TAG_GOOD, mem_params_size[1]);

		char inject_so_path[100] = "/data/cs/libsupporter.so";
		if(inject_so(pid, map_base, inject_so_path, "support_entry", 1,
				NULL, 0, &original_regs, &regs,
				mem_params, count, mem_params_size, true) == -1)
		{
			return 1;
		}
		return 0;
	}
}

int main(int argc, char *argv[])
{
	//切记，该subfunc的定义只能出现在goto Error语句上方，否则会发生"cross initialization of char *subfunc"的错误
	//意为跳过变量初始化，即变量作用域为从定义起一直到函数末，而goto发生之后subfunc变量仍在作用域，但此时定义未出现！
	char *subfunc = NULL;
	if(argc < 2)
		goto Error;

	printf("[+] HookApk : Main Func at %p\n", main);
	LOGD(ANDROID_LOG_DEBUG, "Main Func at %p\n", main);
	subfunc = argv[1];
	if(!strcmp(subfunc, "DumpSo"))
	{
		int retcode = DumpSo(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "DumpSo2"))
	{
		int retcode = DumpSo2(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "CheckHWBP"))
	{
		int retcode = CheckHWBP(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookDlopen"))
	{
		int retcode = HookDlopen(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookDlopen2"))
	{
		int retcode = HookDlopen2(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookDlopen3"))
	{
		int retcode = HookDlopen3(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookSYZH"))
	{
		int retcode = HookSYZH(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookHJZG"))
	{
		int retcode = HookHJZG(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookSYZHTW"))
	{
		int retcode = HookSYZHTW(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookQMCS"))
	{
		int retcode = HookQMCS(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "XorWRITE"))
	{
		int retcode = XorWRITE(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "SetMemBP"))
	{
		int retcode = SetMemBP(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookOpen"))
	{
		int retcode = HookOpen(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookOpen2"))
	{
		int retcode = HookOpen2(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookJNBG"))
	{
		int retcode = HookJNBG(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "Hook12306"))
	{
		int retcode = Hook12306(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookAny"))
	{
		int retcode = HookAny(argc, argv);
		if(retcode == -1)
		{
			printf("[-] Args Num Error !\n");
			printf("[-] [Usage <HookAny>]: arg1(pName) arg2(soName) arg3(hookPoint) arg4(insLength) !\n");
			goto Error;
		}
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "StaticMethodId"))
	{
		int retcode = StaticMethodId(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookTest"))
	{
		int retcode = HookTest(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookAny2"))
	{
		int retcode = HookAny2(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "ScanMemory"))
	{
		int retcode = ScanMemory(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookYSQ"))
	{
		int retcode = HookYSQ(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "HookPtrace"))
	{
		int retcode = HookPtrace(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "hookaddr"))
	{
		int retcode = HookAddr(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}
	else if(!strcmp(subfunc, "hookaddrpid"))
	{
		int retcode = HookAddrPid(argc, argv);
		if(retcode == -1)
			goto Error;
		else
			return retcode;
	}

Error:
	printf("[-] HookApk : Args Num Error !\n");
	printf("[-] HookApk : [Usage <DumpSo>]: arg1(pid) arg2(soName) arg3(toFilePath) !\n");
	LOGD(ANDROID_LOG_ERROR, "Args Num Error !\n");
	LOGD(ANDROID_LOG_ERROR, "[Usage <DumpSo>]: arg1(pid) arg2(soName) arg3(toFilePath) !\n");
	return -1;
}

#ifdef __cplusplus
}
#endif

