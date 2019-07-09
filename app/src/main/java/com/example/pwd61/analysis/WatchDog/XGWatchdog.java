package com.example.pwd61.analysis.WatchDog;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;



import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.util.Random;


import static com.example.pwd61.analysis.Utils.MyLog.LogEx;
import static com.example.pwd61.analysis.Utils.encrypt.MD5_str;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:XGWatchdog
 * Created by pwd61 on 2019/4/19 13:55
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class XGWatchdog {
    public static Integer CURRENT_WD_VERSION = Integer.valueOf(2);
    private static final String LIB_FULL_NAME = "libxguardian.so";
    private static final String LIB_NAME = "xguardian";
    public static final String TAG = "xguardian";
    private static String WatchdogPath = "";
    private static int defaultWatchdogPort = 55550;
    private static Handler handler = null;
    private static volatile XGWatchdog instance = null;
    private static Random random = new Random();
    private static final String watchdogPortName = MD5_str("com.tencent.tpnsWatchdogPort");
    private Context context = null;
    volatile boolean isStarted = false;


    private XGWatchdog(Context context) {
        try {
            this.context = context.getApplicationContext();
            //m.c(this.context);
            HandlerThread handlerThread = new HandlerThread("XGWatchdog.thread");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        } catch (Throwable th) {
            //com.tencent.android.tpush.a.a.c("xguardian", "init XGWatchdog error", th);
        }
    }

    public static XGWatchdog getInstance(Context context) {
        if (instance == null) {
            synchronized (XGWatchdog.class) {
                if (instance == null) {
                    instance = new XGWatchdog(context);
                }
            }
        }
        return instance;
    }

    public static int getRandomInt(int i) {
        return random.nextInt(i);
    }

    public static int getRandomPort() {
        return getRandomInt(1000) + 55000;
    }



    public void sendHeartbeat2Watchdog(String str) {
        sendHeartbeat2Watchdog(str, null);
    }

    interface inCall {
        void hanldMsg(String str);
    }

    class Heartbeat implements Runnable {
        final String m_content;
        final inCall m_incall;
        final XGWatchdog m_Xdog;

        public Heartbeat(XGWatchdog xdog, String str, inCall incall) {
            this.m_content = str;
            this.m_incall = incall;
            this.m_Xdog = xdog;
        }

        @Override
        public void run() {
            try {
                String content = this.m_Xdog.directSendContent(this.m_content);
                if (this.m_incall != null) {
                    this.m_incall.hanldMsg(content);
                }
            } catch (Throwable th) {

            }
        }
    }

    public void sendHeartbeat2Watchdog(String str, inCall abVar) {
        if (handler != null) {
            handler.post(new Heartbeat(this, str, abVar));
        }
    }

    private String directSendContent(String str) {
        DataOutputStream dataOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        Socket socket;
        DataOutputStream dataOutputStream2;
        ByteArrayOutputStream byteArrayOutputStream2;
        String str2 = null;
        //todo:开关研究还没有
//        if (com.tencent.android.tpush.service.a.a.a(m.e()).y == 0 || !p.h(this.context)) {
//            return null;
//        }
        BufferedReader bufferedReader = null;
        Socket socket2;
        try {
            socket2 = new Socket("127.0.0.1", 1024/*getWatchdogPort()*/);
            try {
                socket2.setSoTimeout(2000);
                DataInputStream dataInputStream = new DataInputStream(socket2.getInputStream());
                dataOutputStream = new DataOutputStream(socket2.getOutputStream());
                if (str == null) {
                    try {
                        str = "xgapplist:"  ;
                    } catch (Throwable th2) {
                        th = th2;
                        socket = socket2;
                        dataOutputStream2 = dataOutputStream;
                        byteArrayOutputStream2 = null;
                        if (socket != null) {
                        }
                        if (null != null) {
                        }
                        if (byteArrayOutputStream2 != null) {
                        }
                        if (dataOutputStream2 != null) {
                        }
                        throw th;
                    }
                }
                dataOutputStream.write(TpnsSecurity.oiSymmetryEncrypt2Byte(str));
                dataOutputStream.flush();
                byteArrayOutputStream = new ByteArrayOutputStream();
            } catch (Throwable th3) {
                th = th3;
                byteArrayOutputStream2 = null;
                socket = socket2;
                dataOutputStream2 = null;
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        LogEx("xguardian", "close socket failed " + e.getMessage());
                    }
                }
                if (null != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e2) {
                    }
                }
                if (byteArrayOutputStream2 != null) {
                    try {
                        byteArrayOutputStream2.close();
                    } catch (IOException e3) {
                    }
                }
                if (dataOutputStream2 != null) {
                    try {
                        dataOutputStream2.close();
                    } catch (Exception e4) {
                    }
                }
                throw th;
            }
            try {
                String str3;
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = socket2.getInputStream().read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                if (byteArrayOutputStream.toByteArray().length > 0) {
                    str3 = new String(TpnsSecurity.oiSymmetryDecrypt2Byte(byteArrayOutputStream.toByteArray()));
                } else {
                    str3 = null;
                }
                if (socket2 != null) {
                    try {
                        socket2.close();
                    } catch (Exception e5) {
                        LogEx("xguardian", "close socket failed " + e5.getMessage());
                    }
                }
                if (null != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e6) {
                    }
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e7) {
                    }
                }
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                        str2 = str3;
                    } catch (Exception e8) {
                        str2 = str3;
                    }
                } else {
                    str2 = str3;
                }
            } catch (Throwable th4) {
                Throwable th5 = th4;
                socket = socket2;
                dataOutputStream2 = dataOutputStream;
                byteArrayOutputStream2 = byteArrayOutputStream;
                th = th5;
                if (socket != null) {
                }
                if (null != null) {
                }
                if (byteArrayOutputStream2 != null) {
                }
                if (dataOutputStream2 != null) {
                }
                throw th;
            }
        } catch (Throwable th6) {
            th = th6;
            byteArrayOutputStream2 = null;
            dataOutputStream2 = null;
            socket = null;
            if (socket != null) {
            }
            if (null != null) {
            }
            if (byteArrayOutputStream2 != null) {
            }
            if (dataOutputStream2 != null) {
            }

        }

        if (str2 == null) {
            return "";
        }
        return str2.replace("|", "").replace("/", "").replace("&", "").replace(" ", "");
    }

    class ComparedInfo implements Comparable {
        public String a = "";
        public float b = 1.0f;
        public long c = 0;

        @Override
        public int compareTo(Object o) {
            ComparedInfo comparedInfo = (ComparedInfo) o;
            int retval = Float.compare(this.b, comparedInfo.b);
            if (retval > 0) {
                return -1;
            } else if (retval < 0) {
                return 1;
            } else {

                return 0;
            }
        }

        @NonNull
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("pkgName:").append(this.a).append(",accid:").append(this.c).append(",ver:").append(this.b);
            return stringBuilder.toString();

        }
    }




}
