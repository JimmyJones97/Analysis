#ifndef COMMON_H_
#define COMMON_H_

#include <android/log.h>
#include <stdlib.h>

#ifndef LOG_TAG
#undef LOG_TAG
#define LOG_TAG "test"
#endif

#ifdef DEBUG
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE,LOG_TAG,__VA_ARGS__)
#define LOGE(...)   __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#else
#define LOGI(...) while(0){}
#define LOGE(...) while(0){}
#define LOGW(...) while(0){}
#endif

#define check_value(x) if (NULL == x) {LOGE("NULL =="#x);}

#define CHECK_VALID(V) 				\
	if(V == NULL){					\
		LOGE("%s is null.", #V);	\
		exit(-1);					\
	}else{							\
		LOGI("%s is %p.", #V, V);	\
	}								\

#endif /* COMMON_H_ */
