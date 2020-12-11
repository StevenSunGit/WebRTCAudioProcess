package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectInterface {
    static {
        System.loadLibrary("");
    }

    public native long extendedFilterCreate();
    public native long delayAgnosticCreate();
    public native long audioProcessingCreate();
}
