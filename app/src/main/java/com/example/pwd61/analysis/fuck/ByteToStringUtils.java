package com.example.pwd61.analysis.fuck;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:ByteToStringUtils
 * Created by pwd61 on 2019/4/11 16:30
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/

public class ByteToStringUtils {
    public static final int c = 19;
    public int b;
    static final /* synthetic */ boolean h = false;
    private static final byte[] i = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
    private static final byte[] j = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
    int d;
    public final boolean e;
    public final boolean f;
    public final boolean g;
    private final byte[] k;
    private int l;
    private final byte[] m;

    public static byte[] a = null;

    public ByteToStringUtils(int arg4, byte[] arg5) {
        boolean v1 = true;
        //this.a = null;
        boolean v0 = (arg4 & 1) == 0;
        this.e = v0;
        v0 = (arg4 & 2) == 0;
        this.f = v0;
        if ((arg4 & 4) == 0) {
            v1 = false;
        }

        this.g = v1;
        byte[] v0_1 = (arg4 & 8) == 0 ? this.i : this.j;
        this.m = v0_1;
        this.k = new byte[2];
        this.d = 0;
        int v0_2 = this.f ? 19 : -1;
        this.l = v0_2;
    }

    public final boolean a(byte[] arg11, int arg12, int arg13, boolean arg14) {
        int v5;
        int v3;
        int v2;
        byte[] v6 = this.m;
        byte[] v7 = this.a;
        int v4 = 0;
        int v1 = this.l;
        int v8 = arg13 + arg12;
        int v0 = -1;
        switch (this.d) {
            case 0: {
                v2 = arg12;
                v3 = v0;
                break;
            }
            case 1: {
                if (arg12 + 2 > v8) {
                    v2 = arg12;
                    v3 = v0;
                    break;
                }

                v2 = arg12 + 1;
                v0 = (this.k[0] & 255) << 16 | (arg11[arg12] & 255) << 8 | arg11[v2] & 255;
                this.d = 0;
                ++v2;
                v3 = v0;
                break;
            }
            case 2: {
                if (arg12 + 1 > v8) {
                    v2 = arg12;
                    v3 = v0;
                    break;
                }

                v2 = arg12 + 1;
                v0 = (this.k[0] & 255) << 16 | (this.k[1] & 255) << 8 | arg11[arg12] & 255;
                this.d = 0;
                v3 = v0;
                break;
            }
            default: {
                v2 = arg12;
                v3 = v0;
                break;
            }
        }

        if (v3 != -1) {
            v7[0] = v6[v3 >> 18 & 63];
            v7[1] = v6[v3 >> 12 & 63];
            v7[2] = v6[v3 >> 6 & 63];
            v0 = 4;
            v7[3] = v6[v3 & 63];
            --v1;
            if (v1 == 0) {
                if (this.g) {
                    v0 = 5;
                    v7[4] = 13;
                }

                v4 = v0 + 1;
                v7[v0] = 10;
                v5 = 19;
            } else {
                v5 = v1;
                v4 = v0;
            }
        } else {
            v5 = v1;
        }

        while (v2 + 3 <= v8) {
            v0 = (arg11[v2] & 255) << 16 | (arg11[v2 + 1] & 255) << 8 | arg11[v2 + 2] & 255;
            v7[v4] = v6[v0 >> 18 & 63];
            v7[v4 + 1] = v6[v0 >> 12 & 63];
            v7[v4 + 2] = v6[v0 >> 6 & 63];
            v7[v4 + 3] = v6[v0 & 63];
            v2 += 3;
            v1 = v4 + 4;
            v0 = v5 - 1;
            if (v0 == 0) {
                if (this.g) {
                    v0 = v1 + 1;
                    v7[v1] = 13;
                } else {
                    v0 = v1;
                }

                v4 = v0 + 1;
                v7[v0] = 10;
                v5 = 19;
                continue;
            }

            v5 = v0;
            v4 = v1;
        }

        if (v2 - this.d == v8 - 1) {
            v1 = 0;
            if (this.d > 0) {
                v1 = 1;
                v0 = this.k[0];
            } else {
                v0 = arg11[v2];
                ++v2;
            }

            v3 = (v0 & 255) << 4;
            this.d -= v1;
            v1 = v4 + 1;
            v7[v4] = v6[v3 >> 6 & 63];
            v0 = v1 + 1;
            v7[v1] = v6[v3 & 63];
            if (this.e) {
                v1 = v0 + 1;
                v7[v0] = 61;
                v0 = v1 + 1;
                v7[v1] = 61;
            }

            if (this.f) {
                if (this.g) {
                    v7[v0] = 13;
                    ++v0;
                }

                v7[v0] = 10;
                ++v0;
            }

            v4 = v0;
        } else {
            if (v2 - this.d == v8 - 2) {
                v1 = 0;
                if (this.d > 1) {
                    v1 = 1;
                    v0 = this.k[0];
                } else {
                    v0 = arg11[v2];
                    ++v2;
                }

                int v9 = (v0 & 255) << 10;
                if (this.d > 0) {
                    v0 = this.k[v1];
                    ++v1;
                } else {
                    v0 = arg11[v2];
                    ++v2;
                }

                v0 = (v0 & 255) << 2 | v9;
                this.d -= v1;
                v1 = v4 + 1;
                v7[v4] = v6[v0 >> 12 & 63];
                v3 = v1 + 1;
                v7[v1] = v6[v0 >> 6 & 63];
                v1 = v3 + 1;
                v7[v3] = v6[v0 & 63];
                if (this.e) {
                    v0 = v1 + 1;
                    v7[v1] = 61;
                } else {
                    v0 = v1;
                }

                if (this.f) {
                    if (this.g) {
                        v7[v0] = 13;
                        ++v0;
                    }

                    v7[v0] = 10;
                    ++v0;
                }

                v4 = v0;
                if (!this.h && this.d != 0) {
                    throw new AssertionError();
                }

                if (!this.h && v2 != v8) {
                    throw new AssertionError();
                }

                this.b = v4;
                this.l = v5;
                return true;
            }

            if (!this.f) {
                if (!this.h && this.d != 0) {
                    throw new AssertionError();
                }

                if (!this.h && v2 != v8) {
                    throw new AssertionError();
                }

                this.b = v4;
                this.l = v5;
                return true;
            }

            if (v4 <= 0) {
                if (!this.h && this.d != 0) {
                    throw new AssertionError();
                }

                if (!this.h && v2 != v8) {
                    throw new AssertionError();
                }

                this.b = v4;
                this.l = v5;
                return true;
            }

            if (v5 == 19) {
                if (!this.h && this.d != 0) {
                    throw new AssertionError();
                }

                if (!this.h && v2 != v8) {
                    throw new AssertionError();
                }

                this.b = v4;
                this.l = v5;
                return true;
            }

            if (this.g) {
                v0 = v4 + 1;
                v7[v4] = 13;
            } else {
                v0 = v4;
            }

            v4 = v0 + 1;
            v7[v0] = 10;

            if (!this.h && this.d != 0) {
                throw new AssertionError();
            }

            if (!this.h && v2 != v8) {
                throw new AssertionError();
            }

            this.b = v4;
            this.l = v5;
            return true;

        }
        return false;
    }

    public final int a(int arg2) {
        return arg2 * 8 / 5 + 10;
    }

}


