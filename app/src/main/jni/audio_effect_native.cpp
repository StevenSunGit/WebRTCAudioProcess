#include <android/log.h>
#include "include/com_feifei_webrtcaudioprocess_jni_AudioEffect.h"

#include <jni.h>
#include <stdlib.h>
#include <assert.h>
#include <stddef.h>
#include <unistd.h>
#include <memory>

#include "webrtc/modules/audio_processing/include/audio_processing.h"
#include "webrtc/modules/include/module_common_types.h"
#include "webrtc/common_audio/channel_buffer.h"
#include "webrtc/common_audio/include/audio_util.h"
#include "webrtc/common.h"
#include "webrtc/common_audio/resampler/include/resampler.h"

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_extendedFilterCreate(JNIEnv *env, jobject thiz) {
    webrtc::ExtendedFilter* extendedFilter = new webrtc::ExtendedFilter(true);
    return (jlong)extendedFilter;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_delayAgnosticCreate(JNIEnv *env, jobject thiz) {
    webrtc::DelayAgnostic* delayAgnostic = new webrtc::DelayAgnostic(true);
    return (jlong)delayAgnostic;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessingCreate(JNIEnv *env, jobject thiz, jlong extendedFilterID, jlong delayAgnosticID, jint ns, jint gc, jint ec, jint vd){
    webrtc::AudioProcessing* apm = webrtc::AudioProcessing::Create();
    webrtc::Config config;
    apm->level_estimator()->Enable(true);

    config.Set<webrtc::ExtendedFilter>((webrtc::ExtendedFilter*)extendedFilterID);
    config.Set<webrtc::DelayAgnostic>((webrtc::DelayAgnostic*)delayAgnosticID);

    apm->SetExtraOptions(config);
    return (jlong)apm;
}

extern "C" JNIEXPORT void JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessingDestroy(JNIEnv *env, jobject thiz, jlong extendedFilterID, jlong delayAgnosticID){
    delete (webrtc::ExtendedFilter*)extendedFilterID;
    delete (webrtc::DelayAgnostic*)delayAgnosticID;
}