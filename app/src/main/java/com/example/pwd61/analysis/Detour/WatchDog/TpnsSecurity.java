package com.example.pwd61.analysis.Detour.WatchDog;

import com.example.pwd61.analysis.Detour.tools.MyLog;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:TpnsSecurity
 * Created by pwd61 on 2019/4/19 15:03
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class TpnsSecurity {
    private static boolean loadedTpnsSecuritySo = false;
    private static final String tpnsSecurityLibName = "tpnsSecurity";

    public static native byte[] oiSymmetryEncrypt2Byte(String str);
    public static native byte[] oiSymmetryDecrypt2Byte(byte[] bArr);

    static {
        loadedTpnsSecuritySo = false;
        try {
            System.loadLibrary(tpnsSecurityLibName);
            loadedTpnsSecuritySo = true;
        } catch (Throwable th) {
            MyLog.LogEt("tpush", "can not load library,error:", th);
            loadedTpnsSecuritySo = false;
        }
    }

}
