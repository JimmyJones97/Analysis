/* Copyright (c) 2015, Simone 'evilsocket' Margaritelli
   Copyright (c) 2015, Jorrit 'Chainfire' Jongma
   See LICENSE file for details */

#include "hook.h"
#include <android/log.h>


#ifdef __cplusplus
extern "C" {
#endif
#define  STAG "FUCK"
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, STAG, fmt, ##args)
extern void hook();

#ifdef __cplusplus
}
#endif

int hooked = 0;
void __attribute__ ((constructor)) OnLoad() {
    if (hooked) return;
#ifndef DEBUG
//    libhook_log(NULL);
#endif
    pid_t  mypid=getpid();
    LOGD("草泥马的 pid：%d",mypid);
    hooked = 1;
//    hook();
}
