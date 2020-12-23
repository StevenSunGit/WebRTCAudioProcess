package com.feifei.webrtcaudioprocess.AudioEffect;

public class AudioEffectInterface {
    static {
        System.loadLibrary("audioeffect");
    }

    public native long audioNearFrameCreate(int sampleChannel, int sampleRate);
    public native long audioFarFrameCreate(int sampleChannel, int sampleRate);
    public native long audioProcessingCreate(long nearFrameID, long farFrameID, int ns, int gc, int ec, int vd);



    public native void audioProcessingDestroy(long audioFrameID, long extendedFilterID, long delayAgnosticID);
    public native int audioProcessReverseStream(long audioProcessingID, long audioFrameID, short[] audioBuffers);
    public native int audioProcessStream(long audioProcessingID, long audioFrameID, short[] audioBuffers);
}
