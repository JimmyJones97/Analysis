/*
 ============================================================================
 Name        : libinject.c
 Author      :  
 Version     :
 Copyright   : 
 Description : Android shared library inject helper
 ============================================================================
 */
#include <stdio.h>
#include <fcntl.h>
#include <elf.h>
#include <android/log.h>
#include <string.h>

#include "libinject.h"

#ifndef O_BINARY
#define O_BINARY 0
#endif

#define LOGV(fmt, args...) printf(fmt, ##args)


int main(int argc, char **argv) {
    pid_t target_pid;
    target_pid = find_pid_of("com.Autel.maxi");
    printf("[+] com.Autel.maxi pid-> %d\n", target_pid);
    if (writecode_to_targetproc(target_pid, "/data/local/tmp/libtest.so", "hook_entry",
                              "I'm parameter!", strlen("I'm parameter!")) == 0)
        LOGV("inject success\n");
    else
        LOGV("inject wrong\n");
    return 0;
}
