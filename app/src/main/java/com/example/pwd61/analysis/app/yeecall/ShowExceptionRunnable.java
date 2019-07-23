package com.example.pwd61.analysis.app.yeecall;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:ShowExceptionRunnable
 * Created by pwd61 on 2019/7/16 9:05
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class ShowExceptionRunnable implements Runnable {
    private final Runnable runnable;

    public ShowExceptionRunnable(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("invalid argument: ori=null");
        }
        this.runnable = runnable;
    }

    public void run() {
        try {
            this.runnable.run();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
