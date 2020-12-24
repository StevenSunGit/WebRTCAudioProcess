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

    public static int get10msBufferInByte(int frequency, int audioFormat){
        return 80 * frequency / 1000 * audioFormat / 8;
    }

    public int audioResampleInit(int inFreq, int outFreq, int channel){
        intputFrequency = inFreq;
        outputFrequency = outFreq;
        channels = channel;

        audioResampleID = mAudioResampleInterface.audioResampleInit(intputFrequency, outputFrequency, channels);
        return 1;
    }

    public int audioResamplePush(short[] inputBuffer, int inLength, short[] outputBuffer, int outputLength){
        return mAudioResampleInterface.audioResamplePush(audioResampleID, inputBuffer, inLength, outputBuffer, outputLength, 16000/44100);
    }

    public void audioResampleDestroy(){
        mAudioResampleInterface.audioResampleDestroy(audioResampleID);
    }

    public int audioResampleReset(int inFreq, int outFreq, int channels){
       return mAudioResampleInterface.audioResampleReset(audioResampleID, inFreq, outFreq, channels);
    }

    public int audioResampleResetIfNeeded(int inFreq, int outFreq, int channels){
        return mAudioResampleInterface.audioResampleResetIfNeeded(audioResampleID, inFreq, outFreq, channels);
    }
}
