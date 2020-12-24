package com.feifei.webrtcaudioprocess.AudioResample;

public class AudioResampleInterface {
    static {
        System.loadLibrary("audioprocess");
    }

    public native long audioResampleInit(int inFreq, int outFreq, int channels);

    public native int audioResampleReset(long audioResampleID, int inFreq, int outFreq, int channels);
    public native int audioResampleResetIfNeeded(long audioResampleID, int inFreq, int outFreq, int channels);

    public native int audioResamplePush(long audioResampleID, short[] inputBuffer, int inLength, short[] outputBuffer, int maxLength, int outputLength);

    public native int audioResampleDestroy(long audioResampleID);
}
