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

extern "C" JNIEXPORT jlong JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioFrameCreate(JNIEnv *env, jobject thiz, jint sampleChannel, jint sampleRate) {
    webrtc::AudioFrame* audioFrame = new webrtc::AudioFrame();

    /* 暂定送入10ms数据 */
    float frame_step = 10;
    audioFrame->num_channels_ = sampleChannel;
    audioFrame->samples_per_channel_ = sampleRate * frame_step / 1000.0;
    audioFrame->sample_rate_hz_ = sampleRate;

    return (jlong)audioFrame;
}

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

	apm->echo_cancellation()->Enable(true);
    apm->echo_cancellation()->enable_metrics(true);
    apm->echo_cancellation()->enable_delay_logging(true);
	apm->set_stream_delay_ms(0);

    config.Set<webrtc::ExtendedFilter>((webrtc::ExtendedFilter*)extendedFilterID);
    config.Set<webrtc::DelayAgnostic>((webrtc::DelayAgnostic*)delayAgnosticID);

    apm->SetExtraOptions(config);
    apm->high_pass_filter()->Enable(true);

    /* NoiseSuppression噪声抑制 */
    if(ns != -1){
        apm->noise_suppression()->Enable(true);
        switch (ns) {
        case 0:
            apm->noise_suppression()->set_level(webrtc::NoiseSuppression::kLow);
            break;
        case 1:
            apm->noise_suppression()->set_level(webrtc::NoiseSuppression::kModerate);
            break;
        case 2:
            apm->noise_suppression()->set_level(webrtc::NoiseSuppression::kHigh);
            break;
        case 3:
            apm->noise_suppression()->set_level(webrtc::NoiseSuppression::kVeryHigh);
            break;
        default:
            break;
        }
    }

    /* GainControl增益控制 */
    if(gc != -1){
        apm->gain_control()->Enable(true);
        apm->gain_control()->set_analog_level_limits(0, 255);
        switch (gc) {
        case 0:
            apm->gain_control()->set_mode(webrtc::GainControl::kAdaptiveAnalog);
            break;
        case 1:
            apm->gain_control()->set_mode(webrtc::GainControl::kAdaptiveDigital);
            break;
        case 2:
            apm->gain_control()->set_mode(webrtc::GainControl::kFixedDigital);
            break;
        default:
            break;
        }
    }

    /* EchoCancellation回声消除 */
    if(ec != -1){
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
		switch(vd){
			case 0:
				apm->voice_detection()->set_likelihood(webrtc::VoiceDetection::kVeryLowLikelihood);
      			apm->voice_detection()->set_frame_size_ms(10);
      			break;
    		default:
                break;
		}
    }
    apm->Initialize();
    return (jlong)apm;
}

extern "C" JNIEXPORT jint JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessStream(JNIEnv *env, jobject thiz, jlong audioProcessingID, jlong audioFrameID, jshortArray audioBuffers){
    webrtc::AudioProcessing* apm = (webrtc::AudioProcessing*)audioProcessingID;
    webrtc::AudioFrame* audioFrame = (webrtc::AudioFrame*)audioFrameID;
    jshort *buffers = env->GetShortArrayElements(audioBuffers, nullptr);

    std::copy(buffers, buffers + audioFrame->samples_per_channel_, audioFrame->data_);
    int ret = apm->ProcessStream(audioFrame);
    std::copy(audioFrame->data_, audioFrame->data_ + audioFrame->samples_per_channel_, buffers);

    env->ReleaseShortArrayElements(audioBuffers, buffers, 0);

    return ret;
}

extern "C" JNIEXPORT void JNICALL Java_com_feifei_webrtcaudioprocess_AudioEffect_AudioEffectInterface_audioProcessingDestroy(JNIEnv *env, jobject thiz, jlong audioFrameID, jlong extendedFilterID, jlong delayAgnosticID){
    delete (webrtc::AudioFrame*)audioFrameID;
    delete (webrtc::ExtendedFilter*)extendedFilterID;
    delete (webrtc::DelayAgnostic*)delayAgnosticID;
}