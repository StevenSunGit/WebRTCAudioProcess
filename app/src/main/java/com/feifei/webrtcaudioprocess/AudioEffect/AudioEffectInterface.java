package com.feifei.webrtcaudioprocess.AudioEffect;

public class AudioEffectInterface {
    static {
        System.loadLibrary("audio-effect");
    }

    public native long extendedFilterCreate();
    public native long delayAgnosticCreate();
    public native long audioProcessingCreate(long extendedFilterID, long delayAgnosticID);
}
