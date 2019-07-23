//
// Created by pwd61 on 2019/7/22.
//
#include <cassert>
#include <utils/Util.h>
#include "yeecall.hpp"


jint Xors(JNIEnv *jnienv, jobject classzz,
          jbyteArray byteArr, jint ibegin, jint byteArr_len,
          jbyteArray keyt, jint kindex, jint key_len) {
    LOGD("ibegin:%d,long kindex:%d,key_len:%d,byteArr_len:%d", ibegin, kindex,key_len,byteArr_len);
    jboolean isCopy = 0;
    unsigned int v22;
    if (byteArr_len == 0 || key_len == 0) {
        return 0;
    }
    //signed int v14 = (kindex >> 32) & 0xffff;
    signed int v14=0;
    jbyte *v17 = jnienv->GetByteArrayElements(byteArr, &isCopy);
    isCopy=0;
    jbyte *v21 = jnienv->GetByteArrayElements(keyt, &isCopy);
    jbyte *v20 = v17 + ibegin;
    /**
     *  before:
     *  ddb2eb74d82947371955cb812f17889f3231bd09     40chars a
     *  072624da3f472a5be4ac8b84b4540d9edc370195     40chars digest
     *  after:
     *  da94cfaee76e6d6cfdf940059b438501ee06bc9c     40chars a
     *
     */
    do {
        v22 = kindex++ % key_len;
        //*(jbyte *) (v20 + v14++) ^= *(jbyte *) (v21 + v22);
        v17[v14++]^=v21[v22];
        LOGD("pin:%c",v17[v14]);
    } while (byteArr_len > v14);
    jnienv->ReleaseByteArrayElements(byteArr, v17, 0);
    jnienv->ReleaseByteArrayElements(keyt, v21, 2);

    return byteArr_len;
}
//回调函数 在这里面注册函数
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = nullptr;
    //判断虚拟机状态是否有问题
    LOGD("%s", "start to register ！");
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        LOGD("%s", "GetEnv error");
        return -1;
    }
    assert(env != nullptr);
    //开始注册函数 registerNatives -》registerNativeMethods -》env->RegisterNatives
    if (!registerNatives(env)) {
        LOGD("%s", "RegisterNatives error");
        return -1;
    }
    //返回jni 的版本
    LOGD("%s", "register finished!!");
    return JNI_VERSION_1_6;

}

static int registerNatives(JNIEnv *env) {
    //指定类的路径，通过FindClass 方法来找到对应的类
    const char *className = "com/example/pwd61/analysis/app/yeecall/MyXorJNI";
    return registerNativeMethods(env, className, getMethods,
                                 sizeof(getMethods) / sizeof(getMethods[0]));
}

//此函数通过调用JNI中 RegisterNatives 方法来注册我们的函数
static int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *getMethods,
                                 int methodsNum) {
    jclass clazz;
    //找到声明native方法的类
    LOGD("className:%s", className);
    clazz = env->FindClass(className);

    if (clazz == nullptr) {
        LOGD("%s", "FindClass nothing special");
        return JNI_FALSE;
    }
    //注册函数 参数：java类 所要注册的函数数组 注册函数的个数
    LOGD("RegisterNatives:%d", methodsNum);
    if (env->RegisterNatives(clazz, getMethods, methodsNum) < 0) {
        LOGD("%s", "RegisterNatives nothing special");
        return JNI_FALSE;
    }
    return JNI_TRUE;
}