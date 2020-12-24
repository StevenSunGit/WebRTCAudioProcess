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

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioNearFrameCreate(JNIEnv *env, jobject thiz, jint sampleChannel, jint sampleRate) {
    webrtc::AudioFrame* audioFrame = new webrtc::AudioFrame();

    /* 暂定送入10ms数据 */
    float frame_step = 10;
    audioFrame->num_channels_ = sampleChannel;
    audioFrame->samples_per_channel_ = sampleRate * frame_step / 1000.0;
    audioFrame->sample_rate_hz_ = sampleRate;

    return (jlong)audioFrame;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioFarFrameCreate(JNIEnv *env, jobject thiz, jint sampleChannel, jint sampleRate) {
    webrtc::AudioFrame* audioFrame = new webrtc::AudioFrame();

    /* 暂定送入10ms数据 */
    float frame_step = 10;
    audioFrame->num_channels_ = sampleChannel;
    audioFrame->samples_per_channel_ = sampleRate * frame_step / 1000.0;
    audioFrame->sample_rate_hz_ = sampleRate;

    return (jlong)audioFrame;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessingCreate(JNIEnv *env, jobject thiz, jlong nearFrameID, jlong farFrameID, jint ns, jint gc, jint ec, jint vd){
    webrtc::Config config;
    config.Set<webrtc::ExtendedFilter>(new webrtc::ExtendedFilter(true));
    config.Set<webrtc::Intelligibility>(new webrtc::Intelligibility(true));
    config.Set<webrtc::DelayAgnostic>(new webrtc::DelayAgnostic(true));
    config.Set<webrtc::NextGenerationAec>(new webrtc::NextGenerationAec(true));
    config.Set<webrtc::ExperimentalNs>(new webrtc::ExperimentalNs(true));
    config.Set<webrtc::ExperimentalAgc>(new webrtc::ExperimentalAgc(true));

    webrtc::AudioProcessing* apm = webrtc::AudioProcessing::Create(config);
    apm->Initialize({{{((webrtc::AudioFrame*)nearFrameID)->sample_rate_hz_, ((webrtc::AudioFrame*)nearFrameID)->num_channels_},
                     {((webrtc::AudioFrame*)nearFrameID)->sample_rate_hz_, ((webrtc::AudioFrame*)nearFrameID)->num_channels_},
                     {((webrtc::AudioFrame*)farFrameID)->sample_rate_hz_, ((webrtc::AudioFrame*)farFrameID)->num_channels_},
                     {((webrtc::AudioFrame*)farFrameID)->sample_rate_hz_, ((webrtc::AudioFrame*)farFrameID)->num_channels_}}});
    /* EchoCancellation回声消除 */
    if(ec != -1){
        ALOGI("gc set level: %d", ec);
        apm->echo_cancellation()->enable_drift_compensation(true);
        apm->echo_cancellation()->set_suppression_level(webrtc::EchoCancellation::kHighSuppression);
        switch (ec) {
            case 0:
                apm->echo_cancellation()->set_suppression_level(webrtc::EchoCancellation::kLowSuppression);
                break;
            case 1:
                apm->echo_cancellation()->set_suppression_level(webrtc::EchoCancellation::kModerateSuppression);
                break;
            case 2:
    			apm->echo_cancellation()->set_suppression_level(webrtc::EchoCancellation::kHighSuppression);
    		default:
                break;
        }
    }

    /* VoiceDetection语音活动检测 */
    if(vd != -1){
        ALOGI("vd set mode: %d", vd);
		switch(vd){
			case 0:
				apm->voice_detection()->set_likelihood(webrtc::VoiceDetection::kVeryLowLikelihood);
      			apm->voice_detection()->set_frame_size_ms(10);
      			break;
			case 1:
				apm->voice_detection()->set_likelihood(webrtc::VoiceDetection::kHighLikelihood);
      			apm->voice_detection()->set_frame_size_ms(10);
      			break;
    		default:
                break;
		}
    }
    apm->Initialize();
    return (jlong)apm;
}

extern "C" JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setNoiseSuppressionParameter(JNIEnv *env, jobject thiz, jlong audioProcessingID, jint level){
    webrtc::AudioProcessing* apm = (webrtc::AudioProcessing*)audioProcessingID;
    int ret = -1;
    /* NoiseSuppression噪声抑制 */
    ALOGI("ns set level: %d", level);
    apm->noise_suppression()->Enable(true);
    switch (level) {
        case 0:
             ret = apm->noise_suppression()->set_level(webrtc::NoiseSuppression::kLow);
             break;
        case 1:
             ret = apm->noise_suppression()->set_level(webrtc::NoiseSuppression::kModerate);
             break;
        case 2:
             ret = apm->noise_suppression()->set_level(webrtc::NoiseSuppression::kHigh);
             break;
        case 3:
             ret = apm->noise_suppression()->set_level(webrtc::NoiseSuppression::kVeryHigh);
             break;
        default:
             break;
    }
    return ret;
}

extern "C" JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_setGainControlParameter(JNIEnv *env, jobject thiz, jlong audioProcessingID, jint mode){
    webrtc::AudioProcessing* apm = (webrtc::AudioProcessing*)audioProcessingID;
    int ret = -1;
    /* GainControl增益控制 */
    ALOGI("gc set mode: %d", mode);
    apm->gain_control()->Enable(true);
    apm->gain_control()->set_analog_level_limits(0, 255);
    //apm->_apm->gain_control()->set_target_level_dbfs(level);
    //apm->_apm->gain_control()->set_compression_gain_db(gain);
    //apm->_apm->gain_control()->enable_limiter(enable);
    switch (mode) {
        case 0:
            ret = apm->gain_control()->set_mode(webrtc::GainControl::kAdaptiveAnalog);
            break;
        case 1:
            ret = apm->gain_control()->set_mode(webrtc::GainControl::kAdaptiveDigital);
            break;
        case 2:
            ret = apm->gain_control()->set_mode(webrtc::GainControl::kFixedDigital);
            break;
        default:
            break;
        }
    return ret;
}


extern "C" JNIEXPORT void JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessingDestroy(JNIEnv *env, jobject thiz, jlong nearFrameID, jlong farFrameID){
    delete (webrtc::AudioFrame*)nearFrameID;
    delete (webrtc::ExtendedFilter*)farFrameID;
}

extern "C" JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessReverseStream(JNIEnv *env, jobject thiz, jlong audioProcessingID, jlong nearFrameID, jshortArray audioBuffers){
    webrtc::AudioProcessing* apm = (webrtc::AudioProcessing*)audioProcessingID;
    webrtc::AudioFrame* audioFrame = (webrtc::AudioFrame*)nearFrameID;
    jshort *buffers = env->GetShortArrayElements(audioBuffers, nullptr);

    std::copy(buffers, buffers + audioFrame->samples_per_channel_, audioFrame->data_);
    int ret = apm->ProcessReverseStream(audioFrame);

    return ret;
}

extern "C" JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessStream(JNIEnv *env, jobject thiz, jlong audioProcessingID, jlong farFrameID, jshortArray audioBuffers){
    webrtc::AudioProcessing* apm = (webrtc::AudioProcessing*)audioProcessingID;
    webrtc::AudioFrame* audioFrame = (webrtc::AudioFrame*)farFrameID;
    jshort *buffers = env->GetShortArrayElements(audioBuffers, nullptr);

    std::copy(buffers, buffers + audioFrame->samples_per_channel_, audioFrame->data_);
    int ret = apm->ProcessStream(audioFrame);
    std::copy(audioFrame->data_, audioFrame->data_ + audioFrame->samples_per_channel_, buffers);

    env->ReleaseShortArrayElements(audioBuffers, buffers, 0);

    return ret;
}