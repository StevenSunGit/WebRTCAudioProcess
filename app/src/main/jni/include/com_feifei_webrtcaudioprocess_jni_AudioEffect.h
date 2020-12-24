#ifndef WEBRTC_NS_COM_EXAMPLE_WEBRTC_NS_NOISESUPPRESSION_H
#define WEBRTC_NS_COM_EXAMPLE_WEBRTC_NS_NOISESUPPRESSION_H

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

JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioNearFrameCreate(JNIEnv *env, jobject thiz, jint sampleChannel, jint sampleRate);

JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioFarFrameCreate(JNIEnv *env, jobject thiz, jint sampleChannel, jint sampleRate);

JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessingCreate(JNIEnv *env, jobject thiz, jlong nearFrameID, jlong farFrameID);

JNIEXPORT void JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessingDestroy(JNIEnv *env, jobject thiz, jlong audioProcessingID, jlong nearFrameID, jlong farFrameID);


JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setStreamDelayMs(JNIEnv *env, jobject thiz, jlong audioProcessingID, jint time);

JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setHighPassFilterParameter(JNIEnv *env, jobject thiz, jlong audioProcessingID, jboolean enable);

JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setNoiseSuppressionParameter(JNIEnv *env, jobject thiz, jlong audioProcessingID, jint level);

JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setGainControlParameter(JNIEnv *env, jobject thiz, jlong audioProcessingID, jint targetLevel, jint compressionGrain, jint mode);

JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setEchoCancellationParameter(JNIEnv *env, jobject thiz, jlong audioProcessingID, jint level);

JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setEchoCancellationMobileParameter(JNIEnv *env, jobject thiz, jlong audioProcessingID, jint level);

JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setVoiceDetectionParameter(JNIEnv *env, jobject thiz, jlong audioProcessingID, jint likelihood);


JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessReverseStream(JNIEnv *env, jobject thiz, jlong audioProcessingID, jlong nearFrameID, jshortArray audioBuffers);

JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessStream(JNIEnv *env, jobject thiz, jlong audioProcessingID, jlong farFrameID, jshortArray audioBuffers);

JNIEXPORT jboolean JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioHasVoice(JNIEnv *env, jobject thiz, jlong audioProcessingID);

#ifdef __cplusplus
}
#endif

#endif