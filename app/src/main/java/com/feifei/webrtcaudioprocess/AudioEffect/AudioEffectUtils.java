package com.feifei.webrtcaudioprocess.AudioEffect;

public class AudioEffectUtils {
    private  static String TAG = "AudioEffectUtils";

    private AudioEffectInterface mAudioEffectInterface;

    private long mAudioNeareFrameID = 0;
    private long mAudioFarFrameID = 0;
    private long mAudioProcessingID = 0;

    public AudioEffectUtils(){
        mAudioEffectInterface = new AudioEffectInterface();
    }

    /* MinBuffer参数说明
       1. frequency：8000Hz, 16000Hz, 32000Hz, 47000Hz
       2. audioFormat: 8, 16, 32
     */
    public static int get10msBufferInByte(int frequency){
        return 10 * frequency / 1000;
    }

    public void audioEffectInit(int sampleChannel, int sampleRate){
        mAudioNeareFrameID = mAudioEffectInterface.audioNearFrameCreate(sampleChannel, sampleRate);
        mAudioFarFrameID = mAudioEffectInterface.audioFarFrameCreate(sampleChannel, sampleRate);

        mAudioProcessingID = mAudioEffectInterface.audioProcessingCreate(mAudioNeareFrameID, mAudioFarFrameID);
    }

    /* HighPassFilter高通滤波器 */
    public int setHighPassFilterParameter(boolean enable){
        return mAudioEffectInterface.setHighPassFilterParameter(mAudioProcessingID, enable);
    }

    /* NoiseSuppression噪声抑制
        一. level参数说明：
            0. webrtc::NoiseSuppression::kLow
            1. webrtc::NoiseSuppression::kModerate
            2. webrtc::NoiseSuppression::kHigh, 默认值
            3. webrtc::NoiseSuppression::kVeryHigh
    */
    public int setNoiseSuppressionParameter(int level){
        return mAudioEffectInterface.setNoiseSuppressionParameter(mAudioProcessingID, level);
    }

    /* GainControl增益控制targetLevel：
        一.targetLevel参数说明：
            1. 取值范围：0 - 31;
            2. 默认值： 6;
        二.compressionGrain参数说明：
            1. 取值范围：0 - 90;
            2. 默认值： 9;
        三. mode参数说明：
            0. webrtc::GainControl::kAdaptiveAnalog
            1. webrtc::GainControl::kAdaptiveDigital
            2. webrtc::GainControl::kFixedDigital
    */
    public int setGainControlParameter(int targetLevel, int compressionGrain, int mode){
        return mAudioEffectInterface.setGainControlParameter(mAudioProcessingID, targetLevel, compressionGrain, mode);
    }

    /* EchoCancellation回声消除：
        0. webrtc::EchoCancellation::kLowSuppression
        1. webrtc::EchoCancellation::kModerateSuppression
        2. webrtc::EchoCancellation::kHighSuppression
    */
    public int setEchoCancellationLevel(int level){
        return mAudioEffectInterface.setEchoCancellationParameter(mAudioProcessingID, level);
    }

    /* EchoCancellationMobile回声消除：
        0. webrtc::EchoControlMobile::kQuietEarpieceOrHeadset
        1. webrtc::EchoControlMobile::kLoudSpeakerphone
    */
    public int setEchoCancellationMobileParameter(int level){
        return mAudioEffectInterface.setEchoCancellationMobileParameter(mAudioProcessingID, level);
    }

    /* VoiceDetection语音活动检测：
        0. webrtc::VoiceDetection::kVeryLowLikelihood
        1. webrtc::VoiceDetection::kHighLikelihood
    */
    public int setVoiceDetectionLikeLiHood(int likelihood){
        return mAudioEffectInterface.setVoiceDetectionParameter(mAudioProcessingID, likelihood);
    }


    public int audioProcessReverseStream(short[] audioBuffers){
        return mAudioEffectInterface.audioProcessReverseStream(mAudioProcessingID, mAudioNeareFrameID, audioBuffers);
    }

    public int audioProcessStream(short[] audioBuffers){
        return mAudioEffectInterface.audioProcessStream(mAudioProcessingID, mAudioFarFrameID, audioBuffers);
    }

    public void audioEffectDestroy(){
        mAudioEffectInterface.audioProcessingDestroy(mAudioProcessingID, mAudioNeareFrameID, mAudioFarFrameID);
    }
}
