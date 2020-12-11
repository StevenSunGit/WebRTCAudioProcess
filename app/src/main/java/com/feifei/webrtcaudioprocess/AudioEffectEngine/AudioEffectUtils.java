package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectUtils {
    static {
        System.loadLibrary("");
    }

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

    public AudioEffectUtils(){
        AudioProcessingId = AudioProcessingCreate();
        ExtendedFilterId = ExtendedFilterCreate();
        ExtendedFilterId = DelayAgnosticCreate();
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

    private long AudioProcessingCreate(){
        return 0;
    }

    private long ExtendedFilterCreate(){
        return 0;
    }

    private long DelayAgnosticCreate(){
        return 0;
    }

}
