#include <android/log.h>
#include "include/com_feifei_webrtcaudioprocess_jni_AudioEffect.h"


extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_extendedFilterCreate(JNIEnv *env, jobject thiz) {
    return 0;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_delayAgnosticCreate(JNIEnv *env, jobject thiz) {
    return 0;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessingCreate(JNIEnv *env, jobject thiz, jlong extended_filter_id, jlong delay_agnostic_id){
    return 0;
}