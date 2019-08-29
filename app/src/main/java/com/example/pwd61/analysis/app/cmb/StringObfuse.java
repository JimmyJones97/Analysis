package com.example.pwd61.analysis.app.cmb;

import java.io.ByteArrayOutputStream;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:StringObfuse
 * Created by pwd61 on 2019/8/22 10:02
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class StringObfuse {
    private static final String KEY = "cmbshield";
    private static final String hexString = "0123456789ABCDEF";

    public StringObfuse() {
//        InstallDex.stub();
    }

    public static String decode(String str) {
        int i;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() / 2);
        for (i = 0; i < str.length(); i += 2) {
            baos.write((hexString.indexOf(str.charAt(i)) << 4) | hexString.indexOf(str.charAt(i + 1)));
        }
        byte[] b = baos.toByteArray();
        int len = b.length;
        int keyLen = KEY.length();
        for (i = 0; i < len; i++) {
            b[i] = (byte) (b[i] ^ KEY.charAt(i % keyLen));
        }
        return new String(b);
    }

}
