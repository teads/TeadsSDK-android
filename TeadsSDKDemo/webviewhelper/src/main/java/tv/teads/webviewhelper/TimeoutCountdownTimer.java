/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.webviewhelper;

import android.os.CountDownTimer;

/**
 * Timeout utility to have a timeout when needed. It managed a {@link #isTimeout()} to check for passed timeout.
 */
public abstract class TimeoutCountdownTimer extends CountDownTimer {

    private boolean mIsTimeout;

    TimeoutCountdownTimer(int timeoutDuration) {
        super(timeoutDuration, timeoutDuration);
        mIsTimeout = false;
    }

    @Override
    public void onTick(long l) {
    }

    @Override
    public void onFinish() {
        mIsTimeout = true;
        this.onTimeout();
    }

    boolean isTimeout() {
        return mIsTimeout;
    }

    protected abstract void onTimeout();
}