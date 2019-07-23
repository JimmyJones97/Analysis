//
// Created by pwd61 on 2019/7/22.
//

#ifndef ANALYSIS_YEECALL_HPP
#define ANALYSIS_YEECALL_HPP

#include <jni.h>

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved);

static int registerNatives(JNIEnv *env);

static int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *getMethods,
                                 int methodsNum);

jint Xors(JNIEnv *jnienv, jobject classzz,
          jbyteArray byteArr, jint ibegin, jint byteArr_len,
          jbyteArray keyt, jint kindex, jint key_len);

static JNINativeMethod getMethods[] = {
        {"xor", "([BII[BII)I", (void *) Xors},//for test no scense;
//        {"jniRandomKey",    "()Ljava/lang/String;",                                                       (void *) (MyjniRandomKey)},/// org-> jniRandomKey
//        {"jniEncryptMsg",   "([BILjava/lang/String;)[B",                                                  (void *) (MyjniEncryptMsg)},/// org-> jniEncryptMsg
//        {"jniDecryptMsg",   "([BILjava/lang/String;)[B",                                                  (void *) (MyjniDecryptMsg)},/// org-> jniDecryptMsg
//        {"getToken",        "(Ljava/lang/Object;)Ljava/lang/String;",                                     (void *) (getToken)},/// org-> getToken
//        /**
//         * isolate  encrypt algorithms from so .
//         * */
//        {"MyjniRandomKey",  "()Ljava/lang/String;",                                                       (void *) (MyjniRandomKey)},///
//        {"MyjniDecryptMsg", "([BILjava/lang/String;)[B",                                                  (void *) (MyjniDecryptMsg)},///
//        {"MyjniEncryptMsg", "([BILjava/lang/String;)[B",                                                  (void *) (MyjniEncryptMsg)},///
//
//        /**
//         * test perpose
//         */
//        {"signthis",        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void *) (getSignFromJni)},

};

#endif //ANALYSIS_YEECALL_HPP
