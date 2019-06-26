package com.example.pwd61.analysis.app.net;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:iEngineDo
 * Created by pwd61 on 2019/6/25 17:28
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public interface iEngineDo {
    public enum STATUS {
        a("PENDING", 0), b("RUNNING", 1),
        c("DONE", 2), d("ABORT", 3);

        private final String stat;
        private final int index;

        private STATUS(String str, int i) {
            stat = str;
            index = i;
        }

    }

    void a(ionNotify hVar);

    void a(Object obj);

    boolean isPending();

    void b(ionNotify hVar);

    boolean isAbort();

    boolean isRunning();

    ionNotify d();

    ionNotify e();

    boolean f();

    void go();

    boolean h();

}
