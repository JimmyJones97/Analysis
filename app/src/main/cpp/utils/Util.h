#ifndef UTIL_H
#define UTIL_H

#include<android/log.h>
#include <cstdio>
#include <string>
#include <cstdlib>

#define LOG_TAG "FUCK"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE,LOG_TAG,__VA_ARGS__)
#define LOGS(TAG,...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
void *get_module_base(int pid, const char *module_name);

void *get_remote_addr(int target_pid, const char *module_name, void *local_addr);

void faklog(std::string);

void file_cp(const char *SRC, const char *DEST);

void write2file(const char* buffer, const char* DEST);
#endif ////UTIL_H