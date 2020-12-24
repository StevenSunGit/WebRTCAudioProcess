package com.feifei.webrtcaudioprocess.AudioResample;

public class AudioResampleUtils {
    private  static String TAG = "AudioResampleUtils";

    private AudioResampleInterface mAudioResampleInterface;

    private long audioResampleID;

    private int intputFrequency;
    private int outputFrequency;
    private int channels;

    public AudioResampleUtils(){
        mAudioResampleInterface = new AudioResampleInterface();
    }

    public int audioResampleInit(int inFreq, int outFreq, int channel){
        intputFrequency = inFreq;
        outputFrequency = outFreq;
        channels = channel;

        audioResampleID = mAudioResampleInterface.audioResampleInit(intputFrequency, outputFrequency, channels);
        return 1;
    }
}
