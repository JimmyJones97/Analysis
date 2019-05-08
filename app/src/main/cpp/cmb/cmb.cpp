//
// Created by pwd61 on 2019/4/29.
//

#include <pthread.h>
#include <dlfcn.h>
#include <android/log.h>
#include <cstring>
#include <cstdio>
#include <asm/fcntl.h>
#include <Util.h>
#include "cmb.hpp"

unsigned int g_sdkVer = 28;

int doInitArt50() {
    int v0; // ST64_4
    int v1; // ST64_4
    int v2; // ST60_4
    int v3; // ST60_4
    int v4; // ST5C_4
    int v5; // ST5C_4
    void *v7; // [sp+58h] [bp-90h]
    char v8; // [sp+80h] [bp-68h]

//    pthread_mutex_init((pthread_mutex_t *) &dex_mutex, 0);
//    if (g_sdkVer < 28) {
//        if (g_sdkVer < 26) {
//            if (g_sdkVer < 24) {
//                v7 = dlopen("libart.so", 0);
//                if (g_sdkVer < 23) {
//                    j_hookFun(
//                            v7,
//                            "_ZN3art11ClassLinker10LoadMethodEPNS_6ThreadERKNS_7DexFileERKNS_21ClassDataItemIteratorENS_6HandleINS_6mirror5ClassEEE",
//                            art50ClassLinkerLoadMethodStub,
//                            &art50ClassLinkerLoadMethodOri);
//                    j_hookFun(
//                            v7,
//                            "_ZNK3art7OatFile9OatMethod10LinkMethodEPNS_6mirror9ArtMethodE",
//                            artOatFileOatMethodLinkMethodStub,
//                            &artOatFileOatMethodLinkMethodOri);
//                } else {
//                    j_hookFun(
//                            v7,
//                            "_ZN3art11ClassLinker10LoadMethodEPNS_6ThreadERKNS_7DexFileERKNS_21ClassDataItemIteratorENS_6HandleINS_6mirro"
//                            "r5ClassEEEPNS_9ArtMethodE",
//                            artMClassLinkerLoadMethodStub,
//                            &artMClassLinkerLoadMethodOri);
//                    j_hookFun(
//                            v7,
//                            "_ZNK3art7OatFile9OatMethod10LinkMethodEPNS_9ArtMethodE",
//                            artMOatFileOatMethodLinkMethodStub,
//                            &artMOatFileOatMethodLinkMethodOri);
//                }
//                __android_log_print(
//                        6,
//                        (int) "2g.out",
//                        "art50ClassLinkerLoadMethodOri %p artMClassLinkerLoadMethodOri %p",
//                        art50ClassLinkerLoadMethodOri,
//                        artMClassLinkerLoadMethodOri);
//                if (g_sdkVer == 21) {
//                    sub_1EB38("ro.product.manufacturer", &v8, &unk_42853);
//                    if (!strcmp(&v8, "samsung"))
//                        j_hookFun(
//                                v7,
//                                "_ZN3art11ClassLinker11DefineClassEPKcNS_6HandleINS_6mirror11ClassLoaderEEERKNS_7DexFileERKNS7_8ClassDefE",
//                                artClassLinkerDefineClassStub,
//                                &artClassLinkerDefineClassOri);
//                }
//            } else {
//                v4 = j_get_libart_funaddr(
//                        "_ZN3art11ClassLinker10LoadMethodEPNS_6ThreadERKNS_7DexFileERKNS_21ClassDataItemIteratorENS_6HandleINS_6mi"
//                        "rror5ClassEEEPNS_9ArtMethodE");
//                __android_log_print(6, (int) "2g.out", "the addr = %p", v4);
//                j_hookFunAddr(v4, artMClassLinkerLoadMethodStub, &artMClassLinkerLoadMethodOri);
//                v5 = j_get_libart_funaddr("_ZNK3art7OatFile9OatMethod10LinkMethodEPNS_9ArtMethodE");
//                j_hookFunAddr(v5, artMOatFileOatMethodLinkMethodStub,
//                              &artMOatFileOatMethodLinkMethodOri);
//            }
//        } else {
//            v2 = j_get_libart_funaddr(
//                    "_ZN3art11ClassLinker10LoadMethodERKNS_7DexFileERKNS_21ClassDataItemIteratorENS_6HandleINS_6mirror5ClassEEEPNS_9ArtMethodE");
//            __android_log_print(6, (int) "2g.out", "the addr = %p", v2);
//            j_hookFunAddr(v2, artOClassLinkerLoadMethodStub, &artOClassLinkerLoadMethodOri);
//            v3 = j_get_libart_funaddr("_ZNK3art7OatFile9OatMethod10LinkMethodEPNS_9ArtMethodE");
//            j_hookFunAddr(v3, artMOatFileOatMethodLinkMethodStub,
//                          &artMOatFileOatMethodLinkMethodOri);
//        }
//    } else {
//        //todo:
//        // 28一下的API
//        v0 = j_get_libart_funaddr(
//                "_ZN3art11ClassLinker10LoadMethodERKNS_7DexFileERKNS_21ClassDataItemIteratorENS_6HandleINS_6mirror5ClassEEEPNS_9ArtMethodE");
//        __android_log_print(6, "2g.out", "the addr = %p", v0);
//        j_hookFunAddr(v0, artPClassLinkerLoadMethodStub, &artPClassLinkerLoadMethodOri);
//        v1 = j_get_libart_funaddr("_ZNK3art7OatFile9OatMethod10LinkMethodEPNS_9ArtMethodE");
//        j_hookFunAddr(v1, artMOatFileOatMethodLinkMethodStub, &artMOatFileOatMethodLinkMethodOri);
//    }
//    return _stack_chk_guard;
}

int get_libart_funaddr(const char *) {

}

int get_so_addr(const char *libname) {
    int result; // r0
    FILE *fp; // [sp+18h] [bp-420h]

    int v4; // [sp+20h] [bp-418h]
    int v5; // [sp+24h] [bp-414h]
    int v6; // [sp+28h] [bp-410h]
    char s[1024]; // [sp+2Ch] [bp-40Ch]
    int v8; // [sp+42Ch] [bp-Ch]
    LOGS("FUCKCMB","GET ENTER");

    fp= fopen("/proc/self/maps", "r");
    if (fp!= nullptr) {
        v6 = 0;
        v5 = 0;
        while (fgets(s, sizeof(s), fp)) {
            if (strstr(s, libname)) {
                sscanf(s, "%lx-%lx", &v6, &v5);
                break;
            }
        }
        fclose(fp);
        v4 = v6;
    } else {
        v4 = 0;
    }
    result = v4;
    LOGS("FUCKCMB","GET OUT %lx",result);
    return result;
}