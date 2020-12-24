package com.feifei.webrtcaudioprocess.AudioEffect;

public class AudioEffectInterface {
    static {
        System.loadLibrary("audioprocess");
    }

    public native long audioNearFrameCreate(int sampleChannel, int sampleRate);
    public native long audioFarFrameCreate(int sampleChannel, int sampleRate);
    public native long audioProcessingCreate(long nearFrameID, long farFrameID);

    public native int setStreamDelayMs(int time);

    public native int setHighPassFilterParameter(long audioProcessingID, boolean enbale);
    public native int setNoiseSuppressionParameter(long audioProcessingID, int level);
    public native int setGainControlParameter(long audioProcessingID, int targetLevel, int compressionGrain, int mode);
    public native int setEchoCancellationParameter(long audioProcessingID, int level);
    public native int setEchoCancellationMobileParameter(long audioProcessingID, int level);
    public native int setVoiceDetectionParameter(long audioProcessingID, int likelihood);

    public native void audioProcessingDestroy(long nearFrameID, long farFrameID);
    public native int audioProcessStream(long audioProcessingID, long nearFrameID, short[] audioBuffers);
    public native int audioProcessReverseStream(long audioProcessingID, long farFrameID, short[] audioBuffers);
}
