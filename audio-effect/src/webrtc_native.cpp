#include <jni.h>
#include <android/log.h>
#include "com.feifei.webrtcaudioprocess_jni_AudioEffectEngine.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

#ifndef nullptr
#define nullptr 0
#endif

/* 日志输出 */
#define LOG_TAG "jni_NoiseSuppressionNative"
#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,__VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG,__VA_ARGS__)

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffectEngine_AudioEffectInterface_extendedFilterCreate(JNIEnv *env, jobject thiz){
    return 0;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffectEngine_AudioEffectInterface_delayAgnosticCreate(JNIEnv *env, jobject thiz) {
    return 0;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffectEngine_AudioEffectInterface_audioProcessingCreate(JNIEnv *env, jobject thiz, jlong extendedFilterId, jlong delayAgnosticId) {
    return 0;
}

extern "C" JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffectEngine_AudioEffectInterface_audioProcessingDestroy(JNIEnv *env, jobject thiz) {
    return 0;
}