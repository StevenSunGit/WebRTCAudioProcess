#include <jni.h>
#ifndef AUDIOEFFECT_COM_FEIFEI_WEBRTC_AUDIOEFFECTENGINE_H
#define AUDIOEFFECT_COM_FEIFEI_WEBRTC_AUDIOEFFECTENGINE_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffectEngine_AudioEffectInterface_extendedFilterCreate(JNIEnv *env, jobject thiz);
JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffectEngine_AudioEffectInterface_delayAgnosticCreate(JNIEnv *env, jobject thiz);
JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffectEngine_AudioEffectInterface_audioProcessingCreate(JNIEnv *env, jobject thiz);

#ifdef __cplusplus
}
#endif

#endif
