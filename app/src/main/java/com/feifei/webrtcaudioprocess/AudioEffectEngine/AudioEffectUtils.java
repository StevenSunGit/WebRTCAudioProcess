package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectUtils {
    private long AudioProcessingId = 0;
    private long ExtendedFilterId = 0;
    private long DelayAgnosticId = 0;

    /* 噪声抑制 */
    private NoiseSuppressionBean mNoiseSuppressionBean = null;
    /* 回声消除 */
    private EchoCancellationBean mEchoCancellationBean = null;

    static {
        System.loadLibrary("");
    }

    public AudioEffectUtils(){
        AudioProcessingId = AudioProcessingCreate();
        ExtendedFilterId = ExtendedFilterCreate();
        ExtendedFilterId = DelayAgnosticCreate();
    }

    public void setNoiseSuppressionLevel(int level){
        mNoiseSuppressionBean = new NoiseSuppressionBean();
        mNoiseSuppressionBean.setEnable(true);
        mNoiseSuppressionBean.setLevel(level);
    }

    public void setEchoCancellationBean(int level){
        mEchoCancellationBean = new EchoCancellationBean();
        mEchoCancellationBean.setEnable(true);
        mEchoCancellationBean.setEnable(true);
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
