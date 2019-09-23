package com.example.pwd61.analysis.app.cmb;

import android.content.Context;

import javax.net.ssl.SSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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
    private SSLContext mCtx;
    private Context context;
    private javax.net.ssl.SSLContext a = javax.net.ssl.SSLContext.getInstance("TLS");



    public SSLVerifyLogServerCrtSocketFactory(String crtName, KeyStore truststore, Context context)
            throws Throwable {
//        super(truststore);
        this.context = context;
        try {
            InputStream certInputStream = new BufferedInputStream(context.getAssets().open(crtName));
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            final X509Certificate serverCertificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);
            mCtx = SSLContext.getInstance("TLS");
            mCtx.init(null, new TrustManager[]{new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
                            if (x509Certificates == null) {
                                throw new IllegalArgumentException("checkServerTrusted x509Certificates is null ");
                            }
                            if (x509Certificates.length < 0) {
                                throw new IllegalArgumentException("checkServerTrusted x509Certificates is null ");
                            }

                            for (X509Certificate cert : x509Certificates) {
                                cert.checkValidity();
                                try {
                                    cert.verify(serverCertificate.getPublicKey());
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (NoSuchProviderException e) {
                                    e.printStackTrace();
                                } catch (SignatureException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }},
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
                return this.a.getSocketFactory().createSocket(str, i);
            } catch (java.lang.Exception unused) {
            }
        }
    }
    public java.net.Socket createSocket(java.lang.String str, int i, java.net.InetAddress inetAddress, int i2) throws java.io.IOException {
        while (true) {
            try {
                return this.a.getSocketFactory().createSocket(str, i, inetAddress, i2);
            } catch (java.lang.Exception unused) {
            }
        }
    }
    public java.net.Socket createSocket(java.net.InetAddress inetAddress, int i) throws java.io.IOException {
        while (true) {
            try {
                return this.a.getSocketFactory().createSocket(inetAddress, i);
            } catch (java.lang.Exception unused) {
            }
        }
    }
    public java.net.Socket createSocket(java.net.InetAddress inetAddress, int i, java.net.InetAddress inetAddress2, int i2) throws java.io.IOException {
        while (true) {
            try {
                return this.a.getSocketFactory().createSocket(inetAddress, i, inetAddress2, i2);
            } catch (java.lang.Exception unused) {
            }
        }
    }

}