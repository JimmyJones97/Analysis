package com.example.pwd61.analysis.app.eleme;

import android.os.Build;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:xdeviceinfo
 * Created by pwd61 on 11/5/2019 8:13 AM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class xdeviceinfo {
    public static java.lang.String[] sneer(android.content.Context context, java.lang.String str, java.lang.String str2, java.lang.String str3) {
        try {
            java.lang.String[] O000000o = me.ele.uis.eris.O00000Oo.O00000o0.O000000o(context, str, str2, str3);
            if (O000000o == null) {
                return new java.lang.String[]{"MS4001: null result", "MS4001: null result", "MS4001: null result"};
            } else if (O000000o.length == 3) {
                return O000000o;
            } else {
                return new java.lang.String[]{"MS4002: " + O000000o.length, "MS4002: " + O000000o.length, "MS4002: " + O000000o.length};
            }
        } catch (java.lang.Exception e) {
            java.lang.Exception exception = e;
            return new java.lang.String[]{"MS4003: " + exception.toString(), "MS4003: " + exception.toString(), "MS4003: " + exception.toString()};
        }
    }

    private static android.content.SharedPreferences  SharedPreferences = null;

    public static java.lang.String band() {
        return replacestr(Build.BRAND);
    }
    public  static String model(){
        return replacestr(Build.MODEL);
    }
    public static java.lang.String deviceId(android.content.Context context) {
        return ((android.telephony.TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
    }
    public static java.lang.String macAddress(android.content.Context context) {
         SharedPreferences = context.getSharedPreferences("SharedPreUtil", 0);
        String mac=SharedPreferences.getString("eleme_mac_address", "");
        if (!android.text.TextUtils.isEmpty(mac)) {
            return mac;
        }
        try {
            String macAddress = ((android.net.wifi.WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            if (!android.text.TextUtils.isEmpty(macAddress)) {
                android.content.SharedPreferences.Editor edit = SharedPreferences.edit();
                edit.putString("eleme_mac_address", macAddress.replace(":", "_") );
                edit.commit();
                return macAddress;
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static java.lang.String replacestr(java.lang.String str) {
        if (android.text.TextUtils.isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", "_");
    }

//    public static java.lang.String GetDeviceinfo(android.content.Context context) {
//        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
//        try {
//            java.lang.String deviceId = ((android.telephony.TelephonyManager) context.getSystemService("phone")).getDeviceId();
//            if (!android.text.TextUtils.isEmpty(deviceId)) {
//                stringBuilder.append("imei:").append(deviceId);
//            }
//            deviceId = android.os.Build.SERIAL;
//            if (!android.text.TextUtils.isEmpty(deviceId)) {
//                stringBuilder.append(" serial:").append(deviceId);
//            }
//            deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
//            if (!android.text.TextUtils.isEmpty(deviceId)) {
//                stringBuilder.append(" android_id:").append(deviceId);
//            }
//            stringBuilder.append(" brand:").append(band());
//            stringBuilder.append(" model:").append(model());
//            deviceId = deviceId(context);
//            if (!android.text.TextUtils.isEmpty(deviceId)) {
//                stringBuilder.append(" networkOperator:").append(deviceId);
//            }
//            deviceId =macAddress(context);
//            if (!android.text.TextUtils.isEmpty(deviceId)) {
//                stringBuilder.append(" macAddress:").append(deviceId);
//            }
//            stringBuilder.append(" netType:").append(j(context));
//            deviceId = k(context);
//            if (!android.text.TextUtils.isEmpty(deviceId)) {
//                stringBuilder.append(" simSerialNumber:").append(deviceId);
//            }
//            stringBuilder.append(" simState:").append(l(context));
//            android.util.Pair d = d();
//            if (!(android.text.TextUtils.isEmpty((java.lang.CharSequence) d.first) || android.text.TextUtils.isEmpty((java.lang.CharSequence) d.second))) {
//                stringBuilder.append(" latitude:").append((java.lang.String) d.first);
//                stringBuilder.append(" longitude:").append((java.lang.String) d.second);
//            }
//            java.util.List n = n(context);
//            if (n != null && n.size() > 0) {
//                stringBuilder.append(" cid:").append(((me.ele.android.enet.b.f.a) n.get(0)).b());
//                stringBuilder.append(" lac:").append(((me.ele.android.enet.b.f.a) n.get(0)).a());
//            }
//            deviceId = p(context);
//            if (!android.text.TextUtils.isEmpty(deviceId)) {
//                stringBuilder.append(" wifiList:").append(deviceId);
//            }
//            if (android.bluetooth.BluetoothAdapter.getDefaultAdapter() == null) {
//                stringBuilder.append(" haveBluetooth:false");
//            } else {
//                stringBuilder.append(" haveBluetooth:true");
//            }
//            try {
//                org.json.JSONObject jSONObject = new org.json.JSONObject(a(context));
//                java.util.Iterator keys = jSONObject.keys();
//                while (keys.hasNext()) {
//                    deviceId = (java.lang.String) keys.next();
//                    stringBuilder.append(" ").append(deviceId).append(":").append(jSONObject.get(deviceId).toString());
//                }
//            } catch (java.lang.Exception e) {
//                com.google.devtools.build.android.desugar.runtime.ThrowableExtension.printStackTrace(e);
//            }
//        } catch (java.lang.Exception e2) {
//            com.google.devtools.build.android.desugar.runtime.ThrowableExtension.printStackTrace(e2);
//        }
//        return android.util.Base64.encodeToString(stringBuilder.toString().getBytes(), 2);
//    }


}
