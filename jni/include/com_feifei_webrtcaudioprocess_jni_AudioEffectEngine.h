#include <jni.h>

#ifndef WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_H
#define WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffectEngine_AudioEffectInterface_extendedFilterCreate(JNIEnv *env, jobject thiz);


#ifdef __cplusplus
}
#endif

#endif //WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_H
