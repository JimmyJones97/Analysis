
.global _dlopen_addr_s       @dlopen函数在目标进程中的地址     注:以下全局变化在C中可读写
.global _dlopen_param1_s     @dlopen参数1<.so>在目标进程中的地址 
.global _dlopen_param2_s     @dlopen参数2在目标进程中的地址
 
.global _dlsym_addr_s        @dlsym函数在目标进程中的地址
.global _dlsym_param2_s      @dlsym参数2在目标进程中的地址,其实为函数名
 
.global _dlclose_addr_s      @dlcose在目标进程中的地址
 
.global _inject_start_s      @汇编代码段的起始地址
.global _inject_end_s        @汇编代码段的结束地址
 
.global _inject_function_param_s  @hook_init参数在目标进程中的地址
 
.global _saved_cpsr_s        @保存CPSR,以便执行完hook_init之后恢复环境
.global _saved_r0_pc_s       @保存r0-r15,以便执行完hook_init之后恢复环境

.set size_of_string, _dlopen_param2_s - _dlopen_param1_s


.data
_inject_start_s:
	@debug loop
3:
	@sub r1, r1, #0
	@B 3b
    mov  r0, #1                       @ STDOUT
    ldr  r1, _dlopen_param1_s         @ memory address of string
    movs r2, #20         @ size of string
    mov  r7, #4                       @ write syscall
    swi  #0                           @ invoke syscall

	@ dlopen
	ldr r1, _dlopen_param2_s        @设置dlopen第二个参数,flag
	ldr r0, _dlopen_param1_s        @设置dlopen第一个参数 .so
	ldr r3, _dlopen_addr_s          @设置dlopen函数
	blx r3                          @执行dlopen函数,返回值位于r0中
	subs r4, r0, #0                 @把dlopen的返回值soinfo保存在r4中，以方便后面dlclose使用
	beq	2f
 
	@dlsym
	ldr r1, _dlsym_param2_s        @设置dlsym第二个参数,第一个参数已经在r0中了
	ldr r3, _dlsym_addr_s          @设置dlsym函数
	blx r3                         @执行dlsym函数,返回值位于r0中
	subs r3, r0, #0                @把返回值<hook_init在目标进程中的地址>保存在r3中
	beq 1f
 
	@call our function
	ldr r0, _inject_function_param_s  @设置hook_init第一个参数
    blx r3                            @执行hook_init
	subs r0, r0, #0
	beq 2f
 
1:
	@dlclose                        
	mov r0, r4                        @把dlopen的返回值设为dlcose的第一个参数
	ldr r3, _dlclose_addr_s           @设置dlclose函数
	blx r3                            @执行dlclose函数
 
2:
	@restore context
	ldr r1, _saved_cpsr_s             @恢复CPSR
	msr cpsr_cf, r1
	ldr sp, _saved_r0_pc_s            @恢复寄存器r0-r15
	ldmfd sp, {r0-pc}
	
 
    
 
_dlopen_addr_s:                           @初始化_dlopen_addr_s
.word 0x11111111
 
_dlopen_param1_s:
.word 0x11111111
 
_dlopen_param2_s:
.word 0x2                                 @RTLD_GLOBAL
 
_dlsym_addr_s:
.word 0x11111111
 
_dlsym_param2_s:
.word 0x11111111
 
_dlclose_addr_s:
.word 0x11111111
 
_inject_function_param_s:
.word 0x11111111
 
_saved_cpsr_s:
.word 0x11111111
 
_saved_r0_pc_s:
.word 0x11111111
_inject_end_s:                     @代码结束地址
 
.space 0x400, 0                    @代码段空间大小
 
.end
