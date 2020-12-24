package com.feifei.webrtcaudioprocess.AudioResample;

public class AudioResampleInterface {
    static {
        System.loadLibrary("audioresample");
    }

    public native long audioResampleInit(int inFreq, int outFreq, int channels);
}
