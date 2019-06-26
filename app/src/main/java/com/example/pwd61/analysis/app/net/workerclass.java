package com.example.pwd61.analysis.app.net;

import java.util.concurrent.Callable;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:workerclass
 * Created by pwd61 on 2019/6/25 17:18
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class workerclass implements iWorkerDo {
    private static Boolean sSkyAopMarkFiled;
    final /* synthetic */ Callable callable;

    public workerclass(Callable callable) {
        this.callable = callable;

    }
    @Override
    public Object docall(Ataskdo jVar, Object obj) throws Exception {
        return this.callable.call();
    }

}
