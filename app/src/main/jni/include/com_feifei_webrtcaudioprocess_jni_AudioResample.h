#ifndef WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_AUDIORESAMPLE_H
#define WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_AUDIORESAMPLE_H

#include <jni.h>
#include <android/log.h>

/* 日志输出 */
#define LOG_TAG "jni_audio_effect_native"
#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,__VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG,__VA_ARGS__)

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioResampleInit(JNIEnv *env, jobject thiz, jint inFreq, jint outFreq, jint channels);



#ifdef __cplusplus
}
#endif

#endif //WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_AUDIORESAMPLE_H
