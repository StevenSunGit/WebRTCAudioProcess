package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectInterface {
    static {
        System.loadLibrary("");
    }

    public native long audioProcessingCreate();
    public native long extendedFilterCreate();
    public native long delayAgnosticCreate();
}
