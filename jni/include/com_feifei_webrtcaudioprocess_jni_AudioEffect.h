#ifndef WEBRTC_NS_COM_EXAMPLE_WEBRTC_NS_NOISESUPPRESSION_H
#define WEBRTC_NS_COM_EXAMPLE_WEBRTC_NS_NOISESUPPRESSION_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_extendedFilterCreate(JNIEnv *env, jobject thiz);


#ifdef __cplusplus
}
#endif

#endif