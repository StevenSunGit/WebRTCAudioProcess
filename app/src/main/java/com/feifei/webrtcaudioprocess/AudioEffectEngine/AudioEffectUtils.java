package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AudioEffectUtils {
    private AudioEffectInterface audioEffectInterface;

    private long extendedFilterID = 0;

    public AudioEffectUtils(){
        audioEffectInterface = new AudioEffectInterface();
    }

    public void AudioEffectInit(){
        extendedFilterID = audioEffectInterface.extendedFilterCreate();
    }
}
