package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class AutomaticGainControl {
    private boolean mEnable = false;
    private int mMode = -1;

    public boolean isEnable() {
        return mEnable;
    }

    public void setEnable(boolean enable) {
        this.mEnable = enable;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        this.mMode = mode;
    }
}
