package com.example.pwd61.analysis.app.cmb;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:PBSSLCertDataMgr
 * Created by pwd61 on 9/23/2019 5:33 PM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class PBSSLCertDataMgr {
//    private static com.pb.infrastructrue.network.d b = null;
    private java.util.HashMap<java.lang.String, org.json.JSONObject> a = new java.util.HashMap();

    static {
//        cmb.shield.InstallDex.stub();
    }

//    public static synchronized com.pb.infrastructrue.network.d a() {
//        com.pb.infrastructrue.network.d dVar;
//        synchronized (com.pb.infrastructrue.network.d.class) {
//            if (b == null) {
//                b = new com.pb.infrastructrue.network.d();
//            }
//            dVar = b;
//        }
//        return dVar;
//    }

    public java.lang.String b() {
        try {
            org.json.JSONArray jSONArray = new org.json.JSONArray();
            for (java.lang.String str : this.a.keySet()) {
                jSONArray.put((org.json.JSONObject) this.a.get(str));
            }
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            jSONObject.put("SSLCertData", jSONArray);
            return jSONObject.toString();
        } catch (java.lang.Exception e) {
            return "";
        }
    }

    public void a(java.lang.String str, org.json.JSONObject jSONObject) {
        while (true) {
            if (!(android.text.TextUtils.isEmpty(str) || this.a.containsKey(str))) {
//                com.pb.infrastructrue.g.b.e("X509Certificate", "thumbPrint:" + str + ",certData:" + jSONObject.toString());
                this.a.put(str, jSONObject);
            }
            try {
                return;
            } catch (java.lang.Exception unused) {
            }
        }
    }

}
