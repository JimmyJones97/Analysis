#ifndef Native_lib_h
#define Native_lib_h

#include <jni.h>

#define ANDROID_M 23
#define ANDROID_P 28

enum RET_TYE {
    SUC = 0,
    FAIL,
};

#define HOOK_DEF(ret, func, ...) \
  static ret (*orig_##func)(__VA_ARGS__); \
  static ret new_##func(__VA_ARGS__)

////解密函数
extern char *(*JIEMI)(void *a1, int offset);

static void *base;

void hook_main();

void hook_dlopen(int api_level);

int findSymbol(const char *name, const char *libn,
               unsigned long *addr);

void onSoLoaded(const char *name, void *handle);

void obtainClassName(JNIEnv *jniEnv, jclass clazz);

void hookX86(void *);

void x86_spec();

int rest();

void __attribute__((constructor)) init_so();

#endif //Native_lib_h