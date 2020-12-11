package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectUtils {
    /* 采样率 */
    private int mFrequency = 0;
    /* 声道数 */
    private int mChannels = 0;
    /* 量化位数 */
    private int mAudioFormat = 0;

    /* 初始化底层接口 */
    private AudioEffectInterface mAudioEffectInterface;
    private long AudioProcessingId = 0;
    private long ExtendedFilterId = 0;
    private long DelayAgnosticId = 0;

    /* 噪声抑制 */
    private boolean mNoiseSuppressionEnable = false;
    private int mNoiseSuppressionLevel = -1;

    /* 回声消除 */
    private boolean mEchoCancellationEnable = false;
    private int mEchoCancellationLevel = -1;

    /* 自动增益控制 */
    private boolean mAutomaticGainControlEnable = false;
    private int mAutomaticGainControlMode = -1;

    /* 语音活动检测 */
    private boolean mVoiceActivityDetectionEnable = false;
    private int mVoiceActivityDetectionLevel = -1;

    public AudioEffectUtils(int frequency, int channels, int audioFormat){
        mFrequency = frequency;
        mChannels = channels;
        mAudioFormat = audioFormat;

        mAudioEffectInterface = new AudioEffectInterface();
    }

    public int AudioEffecInit(){
        AudioProcessingId = audioProcessingCreate();
        ExtendedFilterId = extendedFilterCreate();
        ExtendedFilterId = delayAgnosticCreate();

        return 1;
    }

    public void setNoiseSuppressionLevel(int level){
        mNoiseSuppressionEnable = true;
        mNoiseSuppressionLevel = level;
    }

    public void setEchoCancellationLevel(int level){
        mEchoCancellationEnable = true;
        mEchoCancellationLevel = level;
    }

    public void setAutomaticGainControlMode(int mode){
        mAutomaticGainControlEnable = true;
        mAutomaticGainControlMode = mode;
    }

    public void setVoiceActivityDetectionLevel(int level){
        mVoiceActivityDetectionEnable = true;
        mVoiceActivityDetectionLevel = level;
    }

    private long audioProcessingCreate(){
        return mAudioEffectInterface.audioProcessingCreate();
    }

    private long extendedFilterCreate(){
        return mAudioEffectInterface.extendedFilterCreate();
    }

    private long delayAgnosticCreate(){
        return mAudioEffectInterface.delayAgnosticCreate();
    }

}
