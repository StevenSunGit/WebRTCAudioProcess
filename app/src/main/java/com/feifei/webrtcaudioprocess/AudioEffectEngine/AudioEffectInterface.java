package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectInterface {
    static {
        System.loadLibrary("");
    }

    public native long AudioProcessingCreate();
}
