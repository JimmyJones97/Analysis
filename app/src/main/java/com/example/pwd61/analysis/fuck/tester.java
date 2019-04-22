package com.example.pwd61.analysis.fuck;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:tester
 * Created by pwd61 on 2019/4/11 16:30
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class tester {
    public static byte[] c(byte[] bArr, int i, int i2, int i3) {
        ByteToStringUtils dVar = new ByteToStringUtils(i3, null);
        int i4 = (i2 / 3) * 4;
        if (!dVar.e) {
            switch (i2 % 3) {
                case 0 /*0*/:
                    break;
                case 1 /*1*/:
                    i4 += 2;
                    break;
                case 2 /*2*/:
                    i4 += 3;
                    break;
                default:
                    break;
            }
        } else if (i2 % 3 > 0) {
            i4 += 4;
        }
        if (dVar.f && i2 > 0) {
            i4 += (dVar.g ? 2 : 1) * (((i2 - 1) / 57) + 1);
        }
        dVar.a = new byte[i4];
        dVar.a(bArr, i, i2, true);

        //此处直接返回即可，参考JD内部处理，并不影响后期加解密流程和登录流程
        return dVar.a;
//        if (false || dVar.b == i4) {
//            return dVar.a;
//        }
//        throw new AssertionError();
    }
    public static byte[] a(byte[] bArr, int i) {
        int length = bArr.length;
        DByteToStringUtils cVar = new DByteToStringUtils(i, new byte[((length * 3) / 4)]);
        if (!cVar.a(bArr, 0, length, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (cVar.b == cVar.a.length) {
            return cVar.a;
        } else {
            byte[] bArr2 = new byte[cVar.b];
            System.arraycopy(cVar.a, 0, bArr2, 0, cVar.b);
            return bArr2;
        }
    }


}
