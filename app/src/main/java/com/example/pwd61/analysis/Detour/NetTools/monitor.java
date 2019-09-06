package com.example.pwd61.analysis.Detour.NetTools;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Proxy;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.example.pwd61.analysis.Utils.utils.Logd;
import static com.example.pwd61.analysis.Utils.utils.dumpStack;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:monitor
 * Created by pwd61 on 2019/5/16 16:08
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class monitor {
    static private String TARGET_APP = "FUCKCMB";
    static private String[] TARGET_APPS = new String[]{"com.yek.lafaso", "com.vipshop.vswxk", "com.baidu.BaiduMap", "com.example.t1", "com.baidu.fb", "com.tencent.mm", "com.tencent.mtt", "com.nq.mdm"};
    static private SharedPreferences msp = null;
    static private Application mApp = null;
    static private String LOG_FILENAME = "_test_network";
    static private boolean NETWORK = true;
    static private boolean HTTP_DATA = true;
    static private boolean SOCKET_DATA = true;
    static private boolean HTTP_RESPONSE = false;


    static public void doHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        //if (!Arrays.asList(TARGET_APPS).contains(lpparam.packageName)) return;

        //TARGET_APP = lpparam.packageName;
        if (lpparam.appInfo == null ||
                (lpparam.appInfo.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0) {
            return;
        } else if (true) {///lpparam.isFirstApplication
            mLog("target", lpparam.packageName);
            //todo:网络监控开始
            if (NETWORK) {
                findAndHookConstructor(InetSocketAddress.class, String.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("网络地址", param.args[0] + ":" + param.args[1]);
                        super.beforeHookedMethod(param);
                    }
                });

                //
                findAndHookMethod("java.net.DatagramSocket", lpparam.classLoader, "send", DatagramPacket.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        DatagramPacket d = (DatagramPacket) param.args[0];
                        if (SOCKET_DATA) {
                            mLog("udp发送", d.getAddress() + ":" + d.getPort() + ":" + new String(d.getData()));
                        } else {
                            mLog("udp发送", d.getAddress() + ":" + d.getPort());
                        }
                        super.beforeHookedMethod(param);
                    }
                });
                findAndHookMethod("java.net.DatagramSocket", lpparam.classLoader, "createSocket", int.class, InetAddress.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("udp监听", ((InetAddress) param.args[1]).toString() + ":" + (Integer) param.args[0]);
                        super.beforeHookedMethod(param);
                    }
                });
                findAndHookMethod("java.net.DatagramSocket", lpparam.classLoader, "bind", SocketAddress.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("udp监听", ((SocketAddress) param.args[0]).toString());
                        super.beforeHookedMethod(param);
                    }
                });
                findAndHookConstructor(DatagramSocket.class, SocketAddress.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("udp监听", ((SocketAddress) param.args[0]).toString());
                        super.beforeHookedMethod(param);
                    }
                });

                //
                findAndHookMethod("android.webkit.WebView", lpparam.classLoader, "loadUrl", String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        String d = (String) param.args[0];
                        mLog("webview", d);
                        super.beforeHookedMethod(param);
                    }
                });
                findAndHookMethod("android.webkit.WebView", lpparam.classLoader, "loadUrl", String.class, Map.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        String d = (String) param.args[0];
                        if (HTTP_DATA) {
                            Map d1 = (Map) param.args[1];
                            mLog("webview", d + ":" + d1.toString());
                        } else {
                            mLog("webview", d);
                        }

                        super.beforeHookedMethod(param);
                    }
                });
                findAndHookMethod("android.webkit.WebView", lpparam.classLoader, "postUrl", String.class, byte[].class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        String d = (String) param.args[0];
                        if (HTTP_DATA) {
                            String d1 = new String((byte[]) param.args[1]);
                            mLog("webview", d + ":" + d1);
                        } else {
                            mLog("webview", d);
                        }

                        super.beforeHookedMethod(param);
                    }
                });

                //
                if (SOCKET_DATA) {
                    findAndHookMethod("java.io.OutputStream", lpparam.classLoader, "write", byte[].class, int.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            byte[] d = (byte[]) param.args[0];
                            mLog("socketdata", new String(d));
                            super.beforeHookedMethod(param);
                        }
                    });
                    findAndHookMethod("java.io.OutputStream", lpparam.classLoader, "write", byte[].class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            byte[] d = (byte[]) param.args[0];
                            String dat=new String(d);

                            if(dat.length()>100) {
                                mLog("socketdata1", dat);
                                dumpStack();
                            }
                            super.beforeHookedMethod(param);
                        }
                    });
                }
                findAndHookMethod("java.nio.channels.SocketChannel", lpparam.classLoader, "open", SocketAddress.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("tcp连接", ((SocketAddress) param.args[0]).toString());
                        super.beforeHookedMethod(param);
                    }
                });

                findAndHookMethod("java.net.Socket", lpparam.classLoader, "startupSocket", InetAddress.class, int.class, InetAddress.class, int.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("tcp连接", ((InetAddress) param.args[0]).toString() + ":" + (Integer) param.args[1]);
                        super.beforeHookedMethod(param);
                    }
                });
                findAndHookMethod("java.net.Socket", lpparam.classLoader, "connect", SocketAddress.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("tcp连接", ((SocketAddress) param.args[0]).toString());
                        super.beforeHookedMethod(param);
                    }
                });
                findAndHookConstructor(ServerSocket.class, int.class, int.class, InetAddress.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("tcp监听", ((InetAddress) param.args[0]).toString());
                        super.beforeHookedMethod(param);
                    }
                });
                findAndHookMethod("java.net.ServerSocket", lpparam.classLoader, "bind", SocketAddress.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("tcp监听", ((SocketAddress) param.args[0]).toString());
                        super.beforeHookedMethod(param);
                    }
                });

                //
                findAndHookMethod("java.net.URL", lpparam.classLoader, "openConnection", java.net.Proxy.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        URL url = (URL) param.thisObject;
                        mLog("urlconnp", url.toString() + ":" + ((Proxy) param.args[0]).toString());
                        super.beforeHookedMethod(param);
                    }
                });
                if (HTTP_DATA) {
                    findAndHookMethod("java.net.URLConnection", lpparam.classLoader, "setRequestProperty", String.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            mLog("urlconnheader", (String) param.args[0] + ":" + (String) param.args[1]);
                            super.beforeHookedMethod(param);
                        }

                    });
                    findAndHookMethod("java.net.URLConnection", lpparam.classLoader, "addRequestProperty", String.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            mLog("urlconnheader", (String) param.args[0] + ":" + (String) param.args[1]);
                            super.beforeHookedMethod(param);
                        }

                    });
                }
                findAndHookMethod("java.net.URL", lpparam.classLoader, "openConnection", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        URL url = (URL) param.thisObject;
//                        url.openConnection()
                        mLog("urlconn", url.toString());
                        super.beforeHookedMethod(param);
                    }
                });
                //
                hookHttpClient(lpparam);

            }//网络监控结束
        }
    }

    private static void mLog(String tag, String text) {
        Logd(tag + ":-->" + text);
    }


    private static void hookHttpClient(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod("org.apache.http.impl.client.AbstractHttpClient", lpparam.classLoader,
                "execute", HttpHost.class, HttpRequest.class, HttpContext.class, new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        //HttpHost host = (HttpHost) param.args[0];
                        HttpRequest request = (HttpRequest) param.args[1];
                        if (request instanceof HttpGet) {
                            HttpGet httpGet = (HttpGet) request;
                            mLog("httpclientGet", httpGet.getURI().toString());
                            if (HTTP_DATA) {
                                Header[] headers = request.getAllHeaders();
                                if (headers != null) {
                                    for (int i = 0; i < headers.length; i++) {
                                        mLog("getHeader", headers[i].getName() + ": " + headers[i].getValue());
                                    }
                                }
                            }
                        } else if (request instanceof HttpPost) {
                            HttpPost httpPost = (HttpPost) request;
                            mLog("httpclientPost", httpPost.getURI().toString());
                            if (HTTP_DATA) {// until get header
                                Header[] headers = request.getAllHeaders();
                                if (headers != null) {
                                    for (int i = 0; i < headers.length; i++) {
                                        mLog("postHeader", headers[i].getName() + ":" + headers[i].getValue());
                                    }
                                }
                                HttpEntity entity = httpPost.getEntity();
                                String contentType = null;
                                if (entity.getContentType() != null) {
                                    contentType = entity.getContentType().getValue();
                                    if (URLEncodedUtils.CONTENT_TYPE.equals(contentType)) {
                                        try {
                                            byte[] data = new byte[(int) entity.getContentLength()];
                                            entity.getContent().read(data);
                                            String content = new String(data, HTTP.DEFAULT_CONTENT_CHARSET);
                                            mLog("postcontent", content);
                                        } catch (IllegalStateException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    } else if (contentType.startsWith(HTTP.DEFAULT_CONTENT_TYPE)) {
                                        try {
                                            byte[] data = new byte[(int) entity.getContentLength()];
                                            entity.getContent().read(data);
                                            String content = new String(data, contentType.substring(contentType.lastIndexOf("=") + 1));
                                            mLog("postcontent", content);
                                        } catch (IllegalStateException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    byte[] data = new byte[(int) entity.getContentLength()];
                                    try {
                                        entity.getContent().read(data);
                                        String content = new String(data, HTTP.DEFAULT_CONTENT_CHARSET);
                                        mLog("postcontent", content);
                                    } catch (IllegalStateException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }//get header
                        } else {
                            HttpEntityEnclosingRequestBase get = (HttpEntityEnclosingRequestBase) request;
                            HttpEntity entity = get.getEntity();
                            mLog("Android-async-http", get.getURI().toString());
                            if (HTTP_DATA) {
                                Header[] headers = request.getAllHeaders();
                                if (headers != null) {
                                    for (int i = 0; i < headers.length; i++) {
                                        mLog("Android-async-httpHeader", headers[i].getName() + ":" + headers[i].getValue());
                                    }
                                }
                                if (entity != null) {
                                    String content = EntityUtils.toString(entity);
                                    mLog("Android-async-httpcontent", content);
                                }
                            }
                        }
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        HttpResponse resp = (HttpResponse) param.getResult();
                        if (resp != null && HTTP_RESPONSE) {
                            mLog("Status Code", "" + resp.getStatusLine().getStatusCode());
                            Header[] headers = resp.getAllHeaders();
                            if (headers != null) {
                                for (int i = 0; i < headers.length; i++) {
                                    mLog("response", headers[i].getName() + ":" + headers[i].getValue());
                                }
                            }

                        }
                        super.afterHookedMethod(param);
                    }
                });
    }

}

