package com.example.pwd61.analysis.app.yeecall;

import android.os.SystemClock;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:SSProfiler
 * Created by pwd61 on 2019/7/16 13:53
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class SSProfiler {
    private a[] a = new a[this.b];
    private int b;

    /* compiled from: SSProfiler */
    public class a {
        private long b;
        private long c;
        private long d;
        private long e;
        private long f;
        private long g;

        protected a() {
        }

        /* Access modifiers changed, original: 0000 */
        public void a(long j) {
            this.c = j;
            if (this.b != 0) {
                j = this.b;
            }
            this.b = j;
        }

        /* Access modifiers changed, original: 0000 */
        public long b(long j) {
            long j2 = j - this.c;
            this.d += j2;
            if (this.e == 0 || j2 < this.e) {
                this.e = j2;
            }
            if (this.f == 0 || j2 > this.f) {
                this.f = j2;
            }
            this.g++;
            return j2;
        }

        /* Access modifiers changed, original: 0000 */
        public long a() {
            return this.d;
        }
    }

    public SSProfiler(int i) {
        this.b = i;
        for (i = 0; i < this.b; i++) {
            this.a[i] = new a();
        }
    }

    public void a(int i) {
        if (i < this.b) {
            this.a[i].a(SystemClock.uptimeMillis());
        }
    }

    public long b(int i) {
        return i < this.b ? this.a[i].b(SystemClock.uptimeMillis()) : 0;
    }

    public float c(int i) {
        return i < this.b ? (float) this.a[i].a() : Float.intBitsToFloat(1);
    }

}
