package com.feifei.webrtcaudioprocess.AudioEffectEngine;

public class NoiseSuppressionBean {
    private boolean mEnable = false;
    public int mLevel = -1;

    public boolean isEnable() {
        return mEnable;
    }

    public void setEnable(boolean enable) {
        this.mEnable = enable;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        this.mLevel = level;
    }
}
