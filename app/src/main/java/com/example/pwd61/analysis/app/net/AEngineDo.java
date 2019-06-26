package com.example.pwd61.analysis.app.net;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:AEngineDo
 * Created by pwd61 on 2019/6/25 17:54
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public abstract class AEngineDo implements iEngineDo {
    private static Boolean sSkyAopMarkFiled;
    protected STATUS status;
    protected ionNotify c;
    protected ionNotify d;
    protected Object e;

    public AEngineDo(ionNotify hVar, ionNotify hVar2) {
        this.status = STATUS.a;
        this.e = null;
        this.d = hVar;
        this.c = hVar2;

    }

    @Override
    public boolean isPending() {
        return STATUS.a.equals(this.status);
    }

    public boolean isDone() {
        return STATUS.c.equals(this.status);
    }
    public boolean isAbort() {
        return STATUS.d.equals(this.status);
    }
    public boolean isRunning() {
        return STATUS.b.equals(this.status);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:21:?, code skipped:
            return;
     */
    public void a(STATUS r6) {

    }

    @Override
    public ionNotify d() {
        return this.c;
    }

    public void a(ionNotify hVar) {
        if (isPending()) {
            this.c = hVar;
        }
    }

    public ionNotify e() {
        return this.d;
    }

    public void b(ionNotify hVar) {

        if (isPending()) {
            this.d = hVar;
        }
    }

    public Object j() {

        return this.e;
    }

    public void a(Object obj) {
        if (isPending()) {
            this.e = obj;
        }
    }

}

