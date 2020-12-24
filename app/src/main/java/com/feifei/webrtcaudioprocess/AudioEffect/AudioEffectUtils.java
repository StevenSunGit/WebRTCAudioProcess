package com.feifei.webrtcaudioprocess.AudioEffect;

public class AudioEffectUtils {
    private  static String TAG = "AudioEffectUtils";

    private AudioEffectInterface mAudioEffectInterface;

    private int mSampleChannel = 0;
    private int mSampleRate = 0;

    private long mAudioNeareFrameID = 0;
    private long mAudioFarFrameID = 0;
    private long mAudioProcessingID = 0;

    public AudioEffectUtils(){
        mAudioEffectInterface = new AudioEffectInterface();
    }

    public static int getMinBufferInByte(int frequency){
        return 10 * frequency / 1000 * 16 / 8;
    }

    public void audioEffectInit(int sampleChannel, int sampleRate){
        mAudioNeareFrameID = mAudioEffectInterface.audioNearFrameCreate(sampleChannel, sampleRate);
        mAudioFarFrameID = mAudioEffectInterface.audioFarFrameCreate(sampleChannel, sampleRate);

        mAudioProcessingID = mAudioEffectInterface.audioProcessingCreate(mAudioNeareFrameID, mAudioFarFrameID);
    }

    /* NoiseSuppression噪声抑制
        一. level参数说明：
            0. webrtc::NoiseSuppression::kLow
            1. webrtc::NoiseSuppression::kModerate
            2. webrtc::NoiseSuppression::kHigh, 默认值
            3. webrtc::NoiseSuppression::kVeryHigh
    */
    public void setNoiseSuppressionParameter(int level){
        mAudioEffectInterface.setNoiseSuppressionParameter(mAudioProcessingID, level);
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
    public void setGainControlParameter(int targetLevel, int compressionGrain, int mode){
        mAudioEffectInterface.setGainControlParameter(mAudioProcessingID, targetLevel, compressionGrain, mode);
    }

    /* EchoCancellation回声消除：
        0. webrtc::EchoCancellation::kLowSuppression
        1. webrtc::EchoCancellation::kModerateSuppression
        2. webrtc::EchoCancellation::kHighSuppression
    */
    public void setEchoCancellationLevel(int level){
        mAudioEffectInterface.setEchoCancellationParameter(mAudioProcessingID, level);
    }

    /* EchoCancellationMobile回声消除：
        0. webrtc::EchoControlMobile::kQuietEarpieceOrHeadset
        1. webrtc::EchoControlMobile::kLoudSpeakerphone
    */
    public void setEchoCancellationMobileParameter(int level){
        mAudioEffectInterface.setEchoCancellationMobileParameter(mAudioProcessingID, level);
    }

    /* VoiceDetection语音活动检测：
        0. webrtc::VoiceDetection::kVeryLowLikelihood
        1. webrtc::VoiceDetection::kHighLikelihood
    */
    public void setVoiceDetectionLikeLiHood(int likelihood){
        mAudioEffectInterface.setVoiceDetectionParameter(mAudioProcessingID, likelihood);
    }


    public int audioProcessReverseStream(short[] audioBuffers){
        return mAudioEffectInterface.audioProcessReverseStream(mAudioProcessingID, mAudioNeareFrameID, audioBuffers);
    }

    public int audioProcessStream(short[] audioBuffers){
        return mAudioEffectInterface.audioProcessStream(mAudioProcessingID, mAudioFarFrameID, audioBuffers);
    }

    public void audioEffectDestroy(){
        mAudioEffectInterface.audioProcessingDestroy(mAudioNeareFrameID, mAudioFarFrameID);
    }
}
