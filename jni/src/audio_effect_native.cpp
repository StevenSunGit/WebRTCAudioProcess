#ifndef WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_H
#define WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_H

#include <android/log.h>
#include "com_feifei_webrtcaudioprocess_jni_AudioEffect.h"


extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_extendedFilterCreate(JNIEnv *env, jobject thiz) {
    return 0;
}

#endif //WEBRTCAUDIOPROCESS_COM_FEIFEI_WEBRTCAUDIOPROCESS_JNI_H
