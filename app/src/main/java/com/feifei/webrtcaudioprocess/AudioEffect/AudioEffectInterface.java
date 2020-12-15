package com.feifei.webrtcaudioprocess.AudioEffect;

public class AudioEffectInterface {
    static {
        System.loadLibrary("audioeffect");
    }

    public native long extendedFilterCreate();
    public native long delayAgnosticCreate();
    public native long audioProcessingCreate(long extendedFilterID, long delayAgnosticID);
    public native void audioProcessingDestroy(long extendedFilterID, long delayAgnosticID);
}
