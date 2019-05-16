
#include <string>
#include <cstdlib>
#include <sys/system_properties.h>
#include <SubstrateHook.h>
#include <memory>
#include <dlfcn.h>
#include <pthread.h>
#include "native-lib.h"
#include "SymbolFinder.h"
#include "utils/Util.h"
#include "native_bridge.h"
#include "lua.hpp"
#include "jd_utils.h"
//#include <execinfo.h>
#include <linux/watchdog.h>
//#include <android-base/logging.h>


static bool firstLoad = true;
static bool isFirst = true;

char *(*JIEMI)(void *a1, int offset);

slua::lua_CFunction fnc_call;

typedef slua::lua_State *(*Lua_STATE_FUNC)();
Lua_STATE_FUNC lua_state_func;

typedef int (*luaL_loadbufferx_def)(slua::lua_State *L, const char *buff, size_t sz,
                                    const char *name, const char *mode);
luaL_loadbufferx_def lua_loadbuf_fnc;

typedef int (*luaL_loadstring_def) (slua::lua_State *L, const char *s);
luaL_loadstring_def  load_string;
//
slua::lua_State *g_luastate;

HOOK_DEF(slua::lua_State*, luaL_newstate, void *a) {

    g_luastate = orig_luaL_newstate(a);
    LOGD("狀態機hook成功了哦,狀態機地址：%p", g_luastate);
    return g_luastate;
}

HOOK_DEF(int, lua_loadbufer, slua::lua_State *L, const char *buff, size_t sz,
         const char *name, const char *mode) {
    char buffer[4096];
    int j = snprintf(buffer, 4095, "%s\n", buff);
    LOGD("LOAD BUFFER.num%d",j);
    std::string tmp(buff);
    faklog(tmp);
    return orig_lua_loadbufer(L, buff, sz, name, mode);
}

HOOK_DEF(int,luaL_loadstring,slua::lua_State *L, const char *s)
{
    //std::string tmpstr(s);
    //faklog(tmpstr);
    write2file(s,"/sdcard/pubg.lua");
    return orig_luaL_loadstring(L,s);
}

void x86_spec() {
    char *error;
    void *handle = dlopen("/data/data/com.tencent.tmgp.pubgmhd/lib/libUE4.so", RTLD_LAZY);
    if (!handle) {
        LOGD("dlopen: -->%s \n", dlerror());
        //exit(EXIT_FAILURE);
    } else {
        //    dlerror();    /* Clear any existing error */
        LOGD("load UE4 succ!! STEP into next stage!");
    }
    dlerror();
    load_string = (luaL_loadstring_def) dlsym(handle, "luaL_loadstring");
    if ((error = dlerror()) != nullptr) {
        LOGD("dlsym: -->%s\n", error);
        //exit(EXIT_FAILURE);
    } else {
        //dlerror();
        LOGD("發現LUA load buffer !! STEP into next stage! 繼續hook it !");
        MSHookFunction(reinterpret_cast<void *>(load_string),
                       (void *) new_luaL_loadstring,
                       (void **) &orig_luaL_loadstring);
    }
    lua_state_func = (Lua_STATE_FUNC) dlsym(handle, "luaL_newstate");
    if ((error = dlerror()) != nullptr) {
        LOGD("dlsym: -->%s\n", error);
        //exit(EXIT_FAILURE);
    } else {
        //dlerror();
        LOGD("發現LUA狀態機!! STEP into next stage! HOOK iT");
//        MSHookFunction(reinterpret_cast<void *>(lua_state_func),
//                       (void *) new_luaL_newstate,
//                       (void **) &orig_luaL_newstate);
        LOGD("HOOK狀態機成功了！！！");

    }
    dlerror();
}


HOOK_DEF(void*, dlsym, const char *symbol, void *handle, int a2) {
    LOGD("the handle [0x%p] symbol name:%s", handle, symbol);
    return orig_dlsym(symbol, handle, a2);
}

HOOK_DEF(void*, getTrampoline, void *handle, const char *name, const char *shorty, uint32_t len) {
    LOGV("getTrampoline 跳板函数: %s", name);
    void *ret = orig_getTrampoline(handle, name, shorty, len);
    LOGV("enter native_bridge2_getTrampoline %s, trampoline_addr %p", name, ret);
    return ret;
}


void hookX86(void *handle) {
    LOGV("FUCK HOUDINI ");
    if (handle != nullptr) {
        auto cbks = (android::NativeBridgeCallbacks *) dlsym(handle, "NativeBridgeItf");
        //MSHookFunction(&(cbks->getTrampoline), (void *) new_getTrampoline,
        //             (void **) &orig_getTrampoline);
        if (cbks != nullptr) {
            LOGV("我日可以version : %d ,: %p ", cbks->version, cbks->getTrampoline);
        }
    }

    ////dlsym_addr = get_remote_addr(getpid(), "/system/bin/linker", (void *) dlsym);
    ////__dl__Z19dlsym_linear_lookupPKcPP6soinfoS2_ proc
//    if (findSymbol("__dl__Z19dlsym_linear_lookupPKcPP6soinfoS2_", "linker",
//                   (unsigned long *) &dlsym_addr) == 0) {
//        if (dlsym_addr != nullptr) {
//            LOGV("开始hook dl_sym");
//            MSHookFunction(dlsym_addr, (void *) new_dlsym, (void **) &orig_dlsym);
//       }
//    }
}

void obtainClassName(JNIEnv *jniEnv, jclass clazz) {
    jclass clsClazz = jniEnv->GetObjectClass(clazz);
    if (clsClazz == nullptr)
        LOGD("ERROR!");
    jmethodID methodId = jniEnv->GetMethodID(clsClazz, "getName", "()Ljava/lang/String;");
    if (methodId == nullptr)
        LOGD("ERROR!");
    auto className = (jstring) jniEnv->CallObjectMethod(clazz, methodId);
    if (className == nullptr)
        LOGD("ERROR!");
    jint strlen = jniEnv->GetStringUTFLength(className);
    if (strlen == 0) LOGD("ERROR!");
    std::unique_ptr<char[]> str(new char[strlen + 1]);
    jniEnv->GetStringUTFRegion(className, 0, strlen, str.get());
    jniEnv->DeleteLocalRef(clsClazz);
    jniEnv->DeleteLocalRef(className);
    LOGD("Calling class is: %s", str.get());
}

HOOK_DEF(void*, do_dlopen_V23, const char *name, int flags, const void *extinfo
) {
    void *ret = orig_do_dlopen_V23(name, flags, extinfo);
    onSoLoaded(name, ret);
    ////note: 能返回非空地址的是x86 库的地址，nullptr的话是arm架构的代码,需要翻译
    LOGD("do_dlopen : %s, return : %p.", name, ret);
    return ret;
}

HOOK_DEF(void*, RegisterNatives, JNIEnv *env, jclass clazz, const JNINativeMethod *methods,
         jint nMethods) {
    LOGD("env:%p,class:%p,methods:%p,num_method:%d", env, clazz, methods, nMethods);
    ////反射name
    //get_clazz_name(env, clazz);
    obtainClassName(env, clazz);
    /////開始反射java類加載
    for (int i = 0; i < nMethods; i++) {
        LOGD("NAME:%s,sign:%s,addr:%p", methods[i].name, methods[i].signature, methods[i].fnPtr);
    }
    //call orig func
    void *ret = orig_RegisterNatives(env, clazz, methods, nMethods);
    LOGD("ORIG RET:%d", (int) ret);
    return ret;
}

HOOK_DEF(void*, FindClass, JNIEnv *env, const char *classname) {
    LOGD("FindClass name: %s", classname);
    return orig_FindClass(env, classname);
}

HOOK_DEF(void*, JNI_OnLoad, void *javaVM) {
    auto *vm = reinterpret_cast<JavaVM *>(javaVM);
    JNIEnv *env;
    vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    assert(env != nullptr);
    const struct JNINativeInterface *nativeInterface = env->functions;
    //nativeInterface->RegisterNatives(jclass clazz, const JNINativeMethod* methods, jint nMethods);
    if (nativeInterface->RegisterNatives != nullptr && firstLoad && nativeInterface->FindClass !=
                                                                    nullptr) {
        MSHookFunction(reinterpret_cast<void *>(nativeInterface->RegisterNatives),
                       (void *) new_RegisterNatives,
                       (void **) &orig_RegisterNatives);
        MSHookFunction(reinterpret_cast<void *>(nativeInterface->FindClass),
                       (void *) new_FindClass,
                       (void **) &orig_FindClass);
        LOGD("Hook RegisterNatives & Findclass succ!!!");
        firstLoad = false;
    }
    LOGD("RegisterNatives addr: %p", nativeInterface->RegisterNatives);
    pthread_t tid;
    pthread_create(&tid, nullptr, decryptMSG, nullptr);
    return orig_JNI_OnLoad(javaVM);
}

HOOK_DEF(void *, jiemi, void *a1, int a2) {
    LOGD("解密地址： %p-%x off: %d", a1, (uint32_t) a1 - (uint32_t) base, a2);
    void *ret = orig_jiemi(a1, a2);
    LOGD("jiemi: %s", reinterpret_cast<char *>(ret));
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
    if (strstr(name, "Security") != nullptr) {
        if (findSymbol("JNI_OnLoad", "libSecurity.so", (unsigned long *) &symbol) == 0 && isFirst) {
            LOGD("FIND JNI_ONLOAD FUCK IT ：%p", symbol);
            MSHookFunction(symbol, (void *) new_JNI_OnLoad, (void **) &orig_JNI_OnLoad);
            symbol = nullptr;
            base = get_module_base(getpid(), "libSecurity.so");
            symbol = reinterpret_cast<void *>((uint32_t) base + 0x00003E4C + 1);
            LOGD("libSecurity :%p jiemi :%p ", base, symbol);
            *(void **) (&JIEMI) = symbol;
            if (symbol != nullptr) {
                //MSHookFunction(symbol, (void *) new_jiemi, (void **) &orig_jiemi);
                LOGD("HOOK 加密函数");
            }
//            if (find_libbase(getpid(), name, (unsigned long *) &symbol) == SUC) {
//                LOGD("FIND so:%s addr:%p", name, symbol);
//            }
            isFirst = false;
        }
    }
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
    LOGD("SYTEM: %s", abi);
    if (strstr(abi, "x86") != nullptr) {
        ////这里特殊化处理
        LOGD("NOT SPECIAL!");
        x86_spec();
    } else {
        hook_main();
    }
}
