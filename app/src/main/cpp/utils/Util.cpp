
#include <string>
#include "Util.h"
#include <fstream>
#include <sstream>
//这个方法来自 android inject 用于获取地址
void* get_module_base(int pid, const char* module_name)
{
    FILE *fp;
    long addr = 0;
    char *pch;
    char filename[32];
    char line[1024];
    if (pid < 0) {
        /* self process */
        snprintf(filename, sizeof(filename), "/proc/self/maps", pid);
    }
    else {
        snprintf(filename, sizeof(filename), "/proc/%d/maps", pid);
    }
    fp = fopen(filename, "r");
    if (fp != nullptr) {
        while (fgets(line, sizeof(line), fp)) {
            if (strstr(line, module_name)) {
                pch = strtok(line, "-");
                addr = strtoul(pch, nullptr, 16);

                if (addr == 0x8000)
                    addr = 0;

                break;
            }
        }
        fclose(fp);
    }
    return (void *)addr;
}
//这个方法来自 android inject 用于获取地址
void* get_remote_addr(int target_pid, const char* module_name, void* local_addr)
{
    void* local_handle, *remote_handle;

    local_handle = get_module_base(-1, module_name);
    remote_handle = get_module_base(target_pid, module_name);

    LOGV("[+] get_remote_addr: local[%x], remote[%x]\n", local_handle, remote_handle);

    void * ret_addr = (void *)((uint32_t)local_addr + (uint32_t)remote_handle - (uint32_t)local_handle);

#if defined(__i386__)
    if (!strcmp(module_name, "/system/lib/libc.so")) {
        int ret_add=reinterpret_cast<int>(ret_addr);
        ret_add += 2;
        ret_addr =reinterpret_cast<void*>(ret_add);
    }
#endif
    return ret_addr;
}


void faklog(std::string str) {
    //LOGD("YOUZ");
    unsigned int max_len = 4096;
    unsigned int str_len = str.length();

    for (int i = 0; i <= str_len / max_len; ++i) {
        unsigned int end = i * max_len + max_len;
        LOGD("%s", str.substr(i * max_len, end > str_len ? str_len : end).c_str());
    }
}

void file_cp(const char *SRC, const char *DEST) {
    std::ifstream src(SRC, std::ios::binary);
    std::ofstream dest(DEST, std::ios::binary);
    dest << src.rdbuf();
    if (src && dest) {
        LOGD("保存：%s,succ",DEST);
    }
}

void write2file(const char* buffer, const char* DEST)
{
    std::ofstream dest(DEST, std::ios::binary|std::ios::app);
    dest<<buffer;
    if (dest)
        LOGD("保存：%s,succ",DEST);
    dest.close();
}
