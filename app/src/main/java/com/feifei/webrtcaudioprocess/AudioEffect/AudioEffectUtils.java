package com.feifei.webrtcaudioprocess.AudioEffect;

public class AudioEffectUtils {
    private AudioEffectInterface audioEffectInterface;

    private int mSampleChannel = 0;
    private int mSampleRate = 0;

    private int mNSLevel = -1;
    private int mGCMode = -1;
    private int mECLevel = -1;
    private int mVDLikeLiHood = -1;

    private long audioFrameID = 0;
    private long extendedFilterID = 0;
    private long delayAgnosticID = 0;
    private long audioProcessingID = 0;

    public AudioEffectUtils(){
        audioEffectInterface = new AudioEffectInterface();
    }

    public static int getMinBufferSize(int frequency){
        return 10 * frequency / 1000;
    }

    /* NoiseSuppression噪声抑制：
        0. webrtc::NoiseSuppression::kLow
        1. webrtc::NoiseSuppression::kModerate
        2. webrtc::NoiseSuppression::kHigh
        3. webrtc::NoiseSuppression::kVeryHigh
     */
    public void setNoiseSuppressionLevel(int level){
        mNSLevel = level;
    }

    /* GainControl增益控制：
        0. webrtc::GainControl::kAdaptiveAnalog
        1. webrtc::GainControl::kAdaptiveDigital
        2. webrtc::GainControl::kFixedDigital
    */
    public void setGainControlMode(int mode){
        mGCMode = mode;
    }

    /* EchoCancellation回声消除：
        0. webrtc::EchoCancellation::kLowSuppression
        1. webrtc::EchoCancellation::kModerateSuppression
        2. webrtc::EchoCancellation::kHighSuppression
    */
    public void setEchoCancellationLevel(int level){
        mECLevel = level;
    }

    /* VoiceDetection语音活动检测：
        0. webrtc::VoiceDetection::kVeryLowLikelihood
    */
    public void setVoiceDetectionLikeLiHood(int likelihood){
        mVDLikeLiHood = likelihood;
    }

    public void audioEffectInit(int sampleChannel, int sampleRate){
        audioFrameID = audioEffectInterface.audioFrameCreate(sampleChannel, sampleRate);
        extendedFilterID = audioEffectInterface.extendedFilterCreate();
        delayAgnosticID = audioEffectInterface.delayAgnosticCreate();
        audioProcessingID = audioEffectInterface.audioProcessingCreate(extendedFilterID, delayAgnosticID, mNSLevel, mGCMode, mECLevel, mVDLikeLiHood);
    }

    public int audioProcessStream(short[] audioBuffers){
        return audioEffectInterface.audioProcessStream(audioProcessingID, audioFrameID, audioBuffers);
    }

    public void audioEffectDestroy(){
        audioEffectInterface.audioProcessingDestroy(audioFrameID, extendedFilterID, delayAgnosticID);
    }
}
