package com.feifei.webrtcaudioprocess.AudioEffect;

public class AudioEffectInterface {
    static {
        System.loadLibrary("audioeffect");
    }

    public native long audioFrameCreate(int sampleChannel, int sampleRate);
    public native long extendedFilterCreate();
    public native long delayAgnosticCreate();
    public native long audioProcessingCreate(long extendedFilterID, long delayAgnosticID, int ns, int gc, int ec, int vd);
    public native void audioProcessingDestroy(long audioFrameID, long extendedFilterID, long delayAgnosticID);
    public native int audioProcessReverseStream(long audioProcessingID, long audioFrameID, short[] audioBuffers);
    public native int audioProcessStream(long audioProcessingID, long audioFrameID, short[] audioBuffers);
}
