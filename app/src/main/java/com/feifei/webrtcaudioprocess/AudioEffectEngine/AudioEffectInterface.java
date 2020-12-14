package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectInterface {
    static {
        System.loadLibrary("audio-effect");
    }

    public native long extendedFilterCreate();
}
