package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectUtils {
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

    static {
        System.loadLibrary("");
    }

    public AudioEffectUtils(){
        AudioProcessingId = AudioProcessingCreate();
        ExtendedFilterId = ExtendedFilterCreate();
        ExtendedFilterId = DelayAgnosticCreate();
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
