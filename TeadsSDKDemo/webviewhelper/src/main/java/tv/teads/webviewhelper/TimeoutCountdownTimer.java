/*
 * Copyright (c) Teads 2017.
 */

package tv.teads.webviewhelper;

import android.os.CountDownTimer;

/**
 * Timeout utility to have a timeout when needed. It managed a {@link #isTimeout()} to check for passed timeout.
 * <p>
 * Created by Benjamin Volland on 28/08/2017.
 */
public abstract class TimeoutCountdownTimer extends CountDownTimer {

    private boolean mIsTimeout;

    public TimeoutCountdownTimer(int timeoutDuration) {
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

    public boolean isTimeout() {
        return mIsTimeout;
    }

    protected abstract void onTimeout();
}