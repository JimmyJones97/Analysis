
#ifndef KRNEL_H
#define KRNEL_H

#include <sys/types.h>
#include <sys/ptrace.h>
int ptrace_attach( pid_t pid );
int ptrace_setregs( pid_t pid, struct pt_regs* regs );
int ptrace_getregs( pid_t pid, struct pt_regs* regs );
int ptrace_writedata( pid_t pid, uint8_t *dest, uint8_t *data, size_t size );
void* get_module_base( pid_t pid, const char* module_name );
int ptrace_detach( pid_t pid );
void* get_remote_addr( pid_t target_pid, const char* module_name, void* local_addr );
int ptrace_continue(pid_t pid);

int ptrace_call( pid_t pid, uint32_t addr, long *params, uint32_t num_params, struct pt_regs* regs );




#endif //KRNEL_H