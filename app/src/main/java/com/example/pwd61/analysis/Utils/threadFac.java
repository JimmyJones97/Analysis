package com.example.pwd61.analysis.Utils;

import java.util.concurrent.ThreadFactory;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:threadFac
 * Created by pwd61 on 2019/4/19 15:19
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class threadFac implements ThreadFactory {
    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setPriority(10);
        return thread;
    }
}
