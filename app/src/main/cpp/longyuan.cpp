#include <string>
#include <cstdlib>
#include <sys/system_properties.h>
#include <SubstrateHook.h>
#include <memory>
#include <dlfcn.h>
#include <pthread.h>
#include "longyuan.h"
#include "SymbolFinder.h"
#include "Util.h"
#include "native_bridge.h"
#include "lua.hpp"

//#include <execinfo.h>

static bool firstLoad = true;
static bool isFirst = true;

typedef int (*waitpid_t)(__pid_t a1, int a2);

waitpid_t waitpid_func;
//

HOOK_DEF(void*,waitpid_addr,__pid_t a1,int a2)
{
        LOGD("INTO ???????????????");
        return orig_waitpid_addr(a1,a2);
}

void x86_spec() {
    char *error;
    void *handle = dlopen("/data/data/com.ilongyuan.sdorica.longyuan/lib/libDexHelper.so", RTLD_LAZY);
    if (!handle) {
        LOGD("dlopen: -->%s \n", dlerror());
        //exit(EXIT_FAILURE);
    } else {
        dlerror();    /* Clear any existing error */
        LOGD("load dexhelper succ!! STEP into next stage!");
    }

    waitpid_func = (waitpid_t) dlsym(handle, "pF4F36CD8C4D8E8E497736A07697A5804");
    if ((error = dlerror()) != nullptr) {
        LOGD("dlsym: -->%s\n", error);
        //exit(EXIT_FAILURE);
    } else {
        LOGD("find sym dexhelper succ!! STEP into next stage!");
        dlerror();
        MSHookFunction(reinterpret_cast<void *>(waitpid_func),
                       (void *) new_waitpid_addr,
                      (void **) &orig_waitpid_addr);
    }
}



HOOK_DEF(void*, do_dlopen_V23, const char *name, int flags, const void *extinfo
) {
    void *ret = orig_do_dlopen_V23(name, flags, extinfo);
    onSoLoaded(name, ret);
    ////note: 能返回非空地址的是x86 库的地址，nullptr的话是arm架构的代码,需要翻译
    LOGD("do_dlopen : %s, return : %p.", name, ret);
    return ret;
}

void onSoLoaded(const char *name, void *handle) {
    void *symbol = nullptr;
#ifdef __i386__
    if (strstr(name, "libhoudini") != nullptr) {
        hookX86(handle);
//        if (findSymbol("JNI_OnLoad", "libnb.so", (unsigned long *) &symbol) == 0) {
//            LOGD("FIND JNI_ONLOAD FUCK IT ：%p", symbol);
//            MSHookFunction(symbol, (void *) new_JNI_OnLoad, (void **) &orig_JNI_OnLoad);
//        }
    }
#endif

}

int findSymbol(const char *name, const char *libn,
               unsigned long *addr) {
    return find_name(getpid(), name, libn, addr);
}

HOOK_DEF(int, dlopen_28, char *a1, int a2, long *a3, unsigned int a4) {
    LOGD("dlopen_28: %s %d  %p  %d", a1, a2, a3, a4);
    return orig_dlopen_28(a1, a2, a3, a4);
}

void hook_dlopen(int api_level) {
    void *symbol = nullptr;
    if (api_level <= ANDROID_M) {
        ////note: x86和arm 的linker的dlopen符号一致
        ////处理细节不同。请注意
        if (findSymbol("__dl__Z9do_dlopenPKciPK17android_dlextinfo", "linker",
                       (unsigned long *) &symbol) == 0) {
            MSHookFunction(symbol, (void *) new_do_dlopen_V23,
                           (void **) &orig_do_dlopen_V23);
        }
    } else if (api_level >= 28) {
        //// 这段代码没有鸡巴用。所以以后用吧的，我操；
        LOGV("ANDROID 9.0");
        if (findSymbol("__dl__Z9do_dlopenPKciPK17android_dlextinfoPKv", "linker",
                       (unsigned long *) &symbol) == 0) {
            MSHookFunction(symbol, (void *) new_dlopen_28, (void **) &orig_dlopen_28);
        }
    } else {
        LOGD("NOT suport OS-api:%d", api_level);
    }
}

void hook_main() {
    char sdk_ver_str[PATH_MAX] = "0";
    __system_property_get("ro.build.version.sdk", sdk_ver_str);
    int api_level = atoi(sdk_ver_str);
    hook_dlopen(api_level);
}

void __attribute__((constructor)) init_so() {
    LOGD("into so hook module!!!");
    char abi[PATH_MAX] = "";
    __system_property_get("ro.product.cpu.abi", abi);
    LOGD("SYTEM-API: %s", abi);
    if (strstr(abi, "x86") != nullptr) {
        //x86_spec();

    } else {
        hook_main();
    }
}
