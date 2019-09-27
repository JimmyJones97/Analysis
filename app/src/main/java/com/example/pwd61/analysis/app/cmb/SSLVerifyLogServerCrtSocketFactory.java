package com.example.pwd61.analysis.app.cmb;

import android.content.Context;
import android.webkit.JavascriptInterface;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.pwd61.analysis.Utils.utils.Logd;


/**************************************************************************
 * project:Analysis
 * Email: 
 * file:SSLVerifyLogServerCrtSocketFactory
 * Created by pwd61 on 9/23/2019 1:35 PM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/

/**
 * 解析服务器证书的类，对服务器端的证书serv.crt进行解析，其中server.crt文件是使用
 * openssl生成的自签名的证书
 */
public class SSLVerifyLogServerCrtSocketFactory extends SSLSocketFactory {

    private static final String TAG = "SSLTrustAllSocketFactory";
    private SSLContext mCtx = SSLContext.getInstance("TLS");
    private Context context;


    public SSLVerifyLogServerCrtSocketFactory(String crtName, KeyStore truststore, Context context)
            throws Throwable {
//        super(truststore);
        this.context = context;
        KeyManagerFactory instance = javax.net.ssl.KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        instance.init(truststore, "cert@cmb".toCharArray());

        try {
            InputStream certInputStream = new BufferedInputStream(context.getAssets().open(crtName));
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            final X509Certificate serverCertificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);

            mCtx.init(null, new TrustManager[]{new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            Logd("check trusted");
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String authType) throws CertificateException {
                            if (x509CertificateArr == null) {
                                throw new IllegalArgumentException("checkServerTrusted x509Certificates is null ");
                            } else if (x509CertificateArr.length <= 0) {
                                throw new IllegalArgumentException("checkServerTrusted x509Certificates is null ");
                            } else {
                                try {
                                    javax.net.ssl.TrustManagerFactory instance = javax.net.ssl.TrustManagerFactory.getInstance("X509");
                                    instance.init((java.security.KeyStore) null);
                                    for (javax.net.ssl.TrustManager trustManager : instance.getTrustManagers()) {
                                        ((javax.net.ssl.X509TrustManager) trustManager).checkServerTrusted(x509CertificateArr, authType);
                                    }
                                    try {
                                        String host = new URL(authType).getHost();
                                        Logd("checkDomainName:" + authType + ",host:" + host);
                                        if ("mobile.cmbchina.com".equalsIgnoreCase(host)) {
                                            com.example.pwd61.analysis.app.cmb.SSLVerifyLogServerCrtSocketFactory.this.handleit(x509CertificateArr);
                                        }
                                    } catch (java.lang.Exception e) {
                                        throw new java.security.cert.CertificateException(e);
                                    }

                                } catch (Exception e) {
                                    throw new java.security.cert.CertificateException(e);
                                }


                            }
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
                    },
                    null);
        } catch (Exception ex) {
        }
    }

    @Override
    public Socket createSocket(Socket socket, String host,
                               int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return mCtx.getSocketFactory().createSocket(socket, host, port, autoClose);
    }


    public java.lang.String[] getSupportedCipherSuites() {
        return new java.lang.String[0];
    }


    @Override
    public Socket createSocket() throws IOException {
        return mCtx.getSocketFactory().createSocket();
    }

    //第一个参数是服务器证书的名字例如：server.crt,第二个参数是应用的上下文
    public static SSLSocketFactory getSocketFactory(String crtName, Context context) {
        try {
            if (crtName == null || "".equalsIgnoreCase(crtName)) {
                throw new IllegalArgumentException(" getSocketFactory crtName is null");
            }
            if (context == null) {
                throw new IllegalArgumentException(" getSocketFactory context is null");
            }
            InputStream certInputStream = new BufferedInputStream(context.getAssets().open(crtName));
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate serverCertificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);
            //生成一个保护服务器证书的keystore
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            String alias = serverCertificate.getSubjectX500Principal().getName();
            keyStore.setCertificateEntry(alias, serverCertificate);

            //生成SSLSocketFactory
            SSLSocketFactory factory = new SSLVerifyLogServerCrtSocketFactory(crtName, keyStore, context);
            return factory;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.lang.String[] getDefaultCipherSuites() {
        return new java.lang.String[0];
    }


    public java.net.Socket createSocket(java.lang.String str, int i) throws java.io.IOException {
        while (true) {
            try {
                return this.mCtx.getSocketFactory().createSocket(str, i);
            } catch (java.lang.Exception unused) {
            }
        }
    }

    public java.net.Socket createSocket(java.lang.String str, int i, java.
            net.InetAddress inetAddress, int i2) throws java.io.IOException {
        while (true) {
            try {
                return this.mCtx.getSocketFactory().createSocket(str, i, inetAddress, i2);
            } catch (java.lang.Exception unused) {
            }
        }
    }

    public java.net.Socket createSocket(java.net.InetAddress inetAddress, int i) throws
            java.io.IOException {
        while (true) {
            try {
                return this.mCtx.getSocketFactory().createSocket(inetAddress, i);
            } catch (java.lang.Exception unused) {
            }
        }
    }

    public java.net.Socket createSocket(java.net.InetAddress inetAddress, int i, java.
            net.InetAddress inetAddress2, int i2) throws java.io.IOException {
        while (true) {
            try {
                return this.mCtx.getSocketFactory().createSocket(inetAddress, i, inetAddress2, i2);
            } catch (java.lang.Exception unused) {
            }
        }
    }


    private void handleit(java.security.cert.X509Certificate[] x509CertificateArr) {
        for (int i = 0; i < x509CertificateArr.length; i++) {
            try {
                java.security.MessageDigest instance = java.security.MessageDigest.getInstance("SHA-1");
                instance.update(x509CertificateArr[i].getEncoded());
                java.lang.String a = BaseFunc.hex(instance.digest());
                java.lang.String name = x509CertificateArr[i].getIssuerDN().getName();
                org.json.JSONObject jSONObject = new org.json.JSONObject();
                a = a.toLowerCase(java.util.Locale.getDefault());
                if (!android.text.TextUtils.isEmpty(a)) {
                    jSONObject.put("FingerPrint", a);
                    jSONObject.put("CertIssuer", name);
//                    com.pb.infrastructrue.network.d.a().a(a, jSONObject);
                }
            } catch (java.lang.Exception e) {
//                com.pb.infrastructrue.g.b.a(b, e.getMessage(), e);
            }
        }
    }


}