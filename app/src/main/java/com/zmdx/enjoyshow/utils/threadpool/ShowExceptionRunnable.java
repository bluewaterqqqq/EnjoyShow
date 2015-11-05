package com.zmdx.enjoyshow.utils.threadpool;

public class ShowExceptionRunnable implements Runnable {
    private final Runnable mOrigin;

    public ShowExceptionRunnable(Runnable ori) {
        if (ori == null) {
            throw new NullPointerException("invalid argument: ori=null");
        }

        mOrigin = ori;
    }

    public ShowExceptionRunnable(Runnable ori, boolean checkDuration) {
        if (ori == null) {
            throw new NullPointerException("invalid argument: ori=null");
        }

        mOrigin = ori;
    }

    @Override
    public void run() {
        try {
            mOrigin.run();
        } catch (Throwable e) {
        } finally {
        }
    }

    @Override
    public String toString() {
        return "ShowExceptionRunnable: {" + mOrigin.toString() + "}";
    }
}