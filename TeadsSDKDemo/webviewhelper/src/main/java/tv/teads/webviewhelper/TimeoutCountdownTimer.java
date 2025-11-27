package tv.teads.webviewhelper;

import android.os.CountDownTimer;

/**
 * Timeout utility to have a timeout when needed. It manages a {@link #isTimeout()} to check for passed timeout.
 */
public abstract class TimeoutCountdownTimer extends CountDownTimer {

    private boolean mIsTimeout;

    TimeoutCountdownTimer(int timeoutDuration) {
        super(timeoutDuration, timeoutDuration);
        mIsTimeout = false;
    }

    /**
     * Starts the countdown and resets the timeout flag.
     * Use this method instead of {@link #start()} to ensure proper timeout state.
     */
    public CountDownTimer startCountdown() {
        mIsTimeout = false;
        return start();
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