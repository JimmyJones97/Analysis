package com.example.pwd61.analysis.app.net;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:Atask
 * Created by pwd61 on 2019/6/25 17:20
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/

public abstract class Atask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private static final int Numofprocessors = Runtime.getRuntime().availableProcessors();
    private static final Executor threadPool = Executors.newFixedThreadPool(Math.max(Numofprocessors * 4, 15));

    public Atask() {
    }

    public AsyncTask<Params, Progress, Result> Atask(Params... paramsArr) {
        if (Build.VERSION.SDK_INT < 11) {
            return super.execute(paramsArr);
        }
        return super.executeOnExecutor(threadPool, paramsArr);
    }
}
