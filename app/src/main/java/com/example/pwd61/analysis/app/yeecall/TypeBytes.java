package com.example.pwd61.analysis.app.yeecall;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:TypeBytes
 * Created by pwd61 on 2019/7/16 9:58
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/
public class TypeBytes {
    static final HashMap<String, Class<?>> a = new HashMap();

    public static byte[] a(int i) {
        return new byte[]{(byte) ((i >>> 24) & 255), (byte) ((i >>> 16) & 255), (byte) ((i >>> 8) & 255), (byte) ((i >>> 0) & 255), (byte) 0, (byte) 0, (byte) 0, (byte) 1};
    }

    public static int a(byte[] bArr) {
        if (bArr == null) {
            throw new RuntimeException("Bytes is empty!!!");
        }
        byte b = bArr[bArr.length - 1];
        if (b == (byte) 1 && bArr.length == 8) {
            int i = 0;
            for (int i2 = 0; i2 < 4; i2++) {
                i = (i << 8) | (bArr[i2] & 255);
            }
            return i;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Type cast exception, ");
        stringBuilder.append(b);
        stringBuilder.append(" type to int, len=");
        stringBuilder.append(bArr.length);
        throw new RuntimeException(stringBuilder.toString());
    }

    public static byte[] a(long j) {
        return new byte[]{(byte) ((int) ((j >>> 56) & 255)), (byte) ((int) ((j >>> 48) & 255)), (byte) ((int) ((j >>> 40) & 255)), (byte) ((int) ((j >>> 32) & 255)), (byte) ((int) ((j >>> 24) & 255)), (byte) ((int) ((j >>> 16) & 255)), (byte) ((int) ((j >>> 8) & 255)), (byte) ((int) ((j >>> 0) & 255)), (byte) 0, (byte) 0, (byte) 0, (byte) 2};
    }

    public static long b(byte[] bArr) {
        if (bArr == null) {
            throw new RuntimeException("byte array is empty!!!");
        } else if (bArr[bArr.length - 1] == (byte) 2 && bArr.length == 12) {
            long j = 0;
            int i = 0;
            while (i < 8) {
                i++;
                j = (j << 8) | ((long) (bArr[i] & 255));
            }
            return j;
        } else {
            throw new RuntimeException("Type cast exception, unknown type to long");
        }
    }

    public static byte[] a(float f) {
        byte[] r0 = new byte[8];
        int floatToIntBits = Float.floatToIntBits(f);
        r0[0] = (byte) ((floatToIntBits >>> 24) & 255);
        r0[1] = (byte) ((floatToIntBits >>> 16) & 255);
        r0[2] = (byte) ((floatToIntBits >>> 8) & 255);
        r0[3] = (byte) ((floatToIntBits >>> 0) & 255);
        r0[4] = (byte) 0;
        r0[5] = (byte) 0;
        r0[6] = (byte) 0;
        r0[7] = (byte) 3;
        return r0;
    }

    public static float c(byte[] bArr) {
        if (bArr == null) {
            throw new RuntimeException("byte array is empty!!!");
        } else if (bArr[bArr.length - 1] == (byte) 3 && bArr.length == 8) {
            int i = 0;
            for (int i2 = 0; i2 < 4; i2++) {
                i = (i << 8) | (bArr[i2] & 255);
            }
            return Float.intBitsToFloat(i);
        } else {
            throw new RuntimeException("Type cast exception, unknown type to float");
        }
    }

    public static byte[] a(String str) {
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        byte[] bArr = new byte[(length + 1)];
        System.arraycopy(bytes, 0, bArr, 0, length);
        bArr[bArr.length - 1] = (byte) 4;
        return bArr;
    }

    public static String d(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        if (bArr[bArr.length - 1] == (byte) 4) {
            return new String(bArr, 0, bArr.length - 1);
        }
        throw new RuntimeException("Type cast exception, unknown type to java.lang.String");
    }

    public static byte[] a(Serializable serializable) throws Throwable {
        Throwable e;
        String name;
        StringBuilder stringBuilder;
        Throwable th;
        if (serializable == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream anonymousClass1;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                anonymousClass1 = new ObjectOutputStream(byteArrayOutputStream) {
                    public void writeUTF(String str) {
                        if (str == null) {
                            str = "";
                        }
                        try {
                            super.writeUTF(str);
                        } catch (IOException E) {
                            E.printStackTrace();
                        }
                    }
                };
                try {
                    anonymousClass1.writeObject(serializable);
                    anonymousClass1.flush();
                    byte[] toByteArray = byteArrayOutputStream.toByteArray();
                    CloseUitls.a(byteArrayOutputStream);
                    CloseUitls.a(anonymousClass1);
                    return toByteArray;
                } catch (IOException e2) {
                    e = e2;
                    try {
                        name = serializable.getClass().getName();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("IOException when writing serializable object of ");
                        stringBuilder.append(name);
                        throw new RuntimeException(stringBuilder.toString(), e);
                    } catch (Throwable th2) {
                        th = th2;
                        CloseUitls.a(byteArrayOutputStream);
                        CloseUitls.a(anonymousClass1);
                        throw th;
                    }
                }
            } catch (IOException e3) {
                Throwable th3 = e3;
                anonymousClass1 = null;
                e = th3;
                name = serializable.getClass().getName();
                stringBuilder = new StringBuilder();
                stringBuilder.append("IOException when writing serializable object of ");
                stringBuilder.append(name);
                throw new RuntimeException(stringBuilder.toString(), e);
            } catch (Throwable th4) {
                th = th4;
                anonymousClass1 = null;
                CloseUitls.a(byteArrayOutputStream);
                CloseUitls.a(anonymousClass1);
                throw th;
            }
        } catch (IOException e4) {
            anonymousClass1 = null;
            e = e4;

            name = serializable.getClass().getName();
            stringBuilder = new StringBuilder();
            stringBuilder.append("IOException when writing serializable object of ");
            stringBuilder.append(name);
            throw new RuntimeException(stringBuilder.toString(), e);
        } catch (Throwable th5) {
            th = th5;
            byteArrayOutputStream = null;
            anonymousClass1 = null;
            CloseUitls.a(byteArrayOutputStream);
            CloseUitls.a(anonymousClass1);
            throw th;
        }
    }

    public static Serializable e(byte[] bArr) throws Throwable {
        Closeable closeable = null;
        Throwable e;
        Closeable closeable2 = null;
        if (bArr != null) {
            int length = bArr.length;
            if (length != 0) {
                try {
                    ByteArrayInputStream byteArrInputStream = new ByteArrayInputStream(bArr);
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrInputStream);
                        try {
                            Serializable serializable = (Serializable) objectInputStream.readObject();
                            CloseUitls.a(byteArrInputStream);
                            CloseUitls.a(objectInputStream);
                            return serializable;
                        } catch (IOException e2) {
                            closeable = byteArrInputStream;
                            closeable2 = objectInputStream;
                            e = e2;
                            throw new RuntimeException("IOException when reading serializable object", e);
                        }
                    } catch (IOException e3) {

                        throw new RuntimeException("IOException when reading serializable object", e3);
                    }
                } catch (ClassNotFoundException e6) {
                    e = e6;
                    length = 0;
                    throw new RuntimeException("ClassNotFoundException when writing serializable object", e);
                } catch (Throwable th2) {
                    e = th2;
                    CloseUitls.a(closeable2);
                    CloseUitls.a(closeable);
                    throw e;
                }
            }
        }
        return null;
    }

    public static byte[] a(Externalizable externalizable) throws Throwable {
        Throwable e;
        String name;
        StringBuilder stringBuilder;
        Throwable th;
        if (externalizable == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream anonymousClass2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                anonymousClass2 = new ObjectOutputStream(byteArrayOutputStream) {
                    public void writeUTF(String str) {
                        if (str == null) {
                            str = "";
                        }
                        try {
                            super.writeUTF(str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                try {
                    anonymousClass2.writeUTF(externalizable.getClass().getName());
                    externalizable.writeExternal(anonymousClass2);
                    anonymousClass2.flush();
                    byte[] toByteArray = byteArrayOutputStream.toByteArray();
                    CloseUitls.a(byteArrayOutputStream);
                    CloseUitls.a(anonymousClass2);
                    return toByteArray;
                } catch (IOException e2) {
                    e = e2;
                    try {
                        name = externalizable.getClass().getName();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("IOException when writing serializable object of ");
                        stringBuilder.append(name);
                        throw new RuntimeException(stringBuilder.toString(), e);
                    } catch (Throwable th2) {
                        th = th2;
                        CloseUitls.a(byteArrayOutputStream);
                        CloseUitls.a(anonymousClass2);
                        throw th;
                    }
                }
            } catch (IOException e3) {
                Throwable th3 = e3;
                anonymousClass2 = null;
                e = th3;
                name = externalizable.getClass().getName();
                stringBuilder = new StringBuilder();
                stringBuilder.append("IOException when writing serializable object of ");
                stringBuilder.append(name);
                throw new RuntimeException(stringBuilder.toString(), e);
            } catch (Throwable th4) {
                th = th4;
                anonymousClass2 = null;
                CloseUitls.a(byteArrayOutputStream);
                CloseUitls.a(anonymousClass2);
                throw th;
            }
        } catch (IOException e4) {

            e = e4;

            name = externalizable.getClass().getName();
            stringBuilder = new StringBuilder();
            stringBuilder.append("IOException when writing serializable object of ");
            stringBuilder.append(name);
            throw new RuntimeException(stringBuilder.toString(), e);
        } catch (Throwable th5) {
            th = th5;
            byteArrayOutputStream = null;

            CloseUitls.a(byteArrayOutputStream);
            CloseUitls.a(anonymousClass2);
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0049 A:{ExcHandler: ClassNotFoundException (e java.lang.ClassNotFoundException), Splitter:B:9:0x0014} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0047 A:{ExcHandler: InstantiationException (e java.lang.InstantiationException), Splitter:B:9:0x0014} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0045 A:{ExcHandler: IllegalAccessException (e java.lang.IllegalAccessException), Splitter:B:9:0x0014} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:29:0x0045, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:30:0x0047, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:31:0x0049, code skipped:
            r0 = e;
     */
    /* JADX WARNING: Missing block: B:32:0x004c, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:33:0x004d, code skipped:
            r6 = r1;
            r1 = r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Externalizable f(byte[] bArr) throws Throwable {
        Throwable e;
        Externalizable externalizable;
        StringBuilder stringBuilder;
        Throwable e2;
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        String str = "";
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        String readUTF;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
            try {
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                try {
                    Class cls;
                    readUTF = objectInputStream.readUTF();
                    synchronized (a) {
                        cls = (Class) a.get(readUTF);
                        if (cls == null) {
                            cls = Class.forName(readUTF);
                            a.put(readUTF, cls);
                        }
                    }
                    Externalizable externalizable2 = (Externalizable) cls.newInstance();
                    externalizable2.readExternal(objectInputStream);
                    CloseUitls.a(byteArrayInputStream);
                    CloseUitls.a(objectInputStream);
                    return externalizable2;
                } catch (IOException e3) {
                    e = e3;
                } catch (ClassNotFoundException e4) {
                } catch (InstantiationException e5) {
                } catch (IllegalAccessException e6) {
                }
            } catch (IOException e7) {
                String str2 = str;
                e = e7;
                externalizable = null;
                readUTF = str2;
                stringBuilder = new StringBuilder();
                stringBuilder.append("IOException when reading Externalizable object - ");
                stringBuilder.append(readUTF);
                throw new RuntimeException(stringBuilder.toString(), e);
            } catch (Throwable e72222) {
                e2 = e72222;
                objectInputStream = null;
                CloseUitls.a(byteArrayInputStream);
                CloseUitls.a(objectInputStream);
                throw e2;
            }
        } catch (IOException e722222) {
            readUTF = str;
            e = e722222;
            externalizable = null;
            stringBuilder = new StringBuilder();
            stringBuilder.append("IOException when reading Externalizable object - ");
            stringBuilder.append(readUTF);
            throw new RuntimeException(stringBuilder.toString(), e);
        } catch (ClassNotFoundException e7222222) {
            e2 = e7222222;
            externalizable = null;
            throw new RuntimeException("ClassNotFoundException when writing Externalizable object", e2);
        } catch (InstantiationException e72222222) {
            e2 = e72222222;
            externalizable = null;
            throw new RuntimeException("InstantiationException when writing Externalizable object", e2);
        } catch (IllegalAccessException e722222222) {
            byteArrayInputStream = null;
            e2 = e722222222;

            throw new RuntimeException("IllegalAccessException when writing Externalizable object", e2);
        } catch (Throwable th) {
            e2 = th;
            CloseUitls.a(byteArrayInputStream);
            CloseUitls.a(objectInputStream);
            throw e2;
        }
        return null;
    }

    public static byte[] b(Serializable serializable) throws Throwable {
        ObjectOutputStream anonymousClass3 = null;
        Throwable e;
        String name;
        StringBuilder stringBuilder;
        Throwable th;
        if (serializable == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream;
        GZIPOutputStream gZIPOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                try {
                    anonymousClass3 = new ObjectOutputStream(gZIPOutputStream) {
                        public void writeUTF(String str) {
                            if (str == null) {
                                str = "";
                            }
                            try {
                                super.writeUTF(str);
                            } catch (IOException E) {
                                E.printStackTrace();
                            }
                        }
                    };
                } catch (IOException e2) {
                    Throwable th2 = e2;
                    anonymousClass3 = null;
                    e = th2;
                    try {
                        name = serializable.getClass().getName();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("IOException when writing serializable object of ");
                        stringBuilder.append(name);
                        throw new RuntimeException(stringBuilder.toString(), e);
                    } catch (Throwable th3) {
                        th = th3;
                        CloseUitls.a(byteArrayOutputStream);
                        CloseUitls.a(gZIPOutputStream);
                        CloseUitls.a(anonymousClass3);
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    anonymousClass3 = null;
                    CloseUitls.a(byteArrayOutputStream);
                    CloseUitls.a(gZIPOutputStream);
                    CloseUitls.a(anonymousClass3);
                    throw th;
                }
                try {
                    anonymousClass3.writeObject(serializable);
                    CloseUitls.a(anonymousClass3);
                    CloseUitls.a(gZIPOutputStream);
                    CloseUitls.a(byteArrayOutputStream);
                    byte[] toByteArray = byteArrayOutputStream.toByteArray();
                    CloseUitls.a(byteArrayOutputStream);
                    CloseUitls.a(gZIPOutputStream);
                    CloseUitls.a(anonymousClass3);
                    return toByteArray;
                } catch (IOException e3) {
                    e = e3;
                    name = serializable.getClass().getName();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("IOException when writing serializable object of ");
                    stringBuilder.append(name);
                    throw new RuntimeException(stringBuilder.toString(), e);
                }
            } catch (IOException e4) {
                anonymousClass3 = null;
                e = e4;

                name = serializable.getClass().getName();
                stringBuilder = new StringBuilder();
                stringBuilder.append("IOException when writing serializable object of ");
                stringBuilder.append(name);
                throw new RuntimeException(stringBuilder.toString(), e);
            } catch (Throwable th5) {
                th = th5;
                gZIPOutputStream = null;

                CloseUitls.a(byteArrayOutputStream);
                CloseUitls.a(gZIPOutputStream);
                CloseUitls.a(anonymousClass3);
                throw th;
            }
        } catch (IOException e5) {
            gZIPOutputStream = null;

            e = e5;

            name = serializable.getClass().getName();
            stringBuilder = new StringBuilder();
            stringBuilder.append("IOException when writing serializable object of ");
            stringBuilder.append(name);
            throw new RuntimeException(stringBuilder.toString(), e);
        } catch (Throwable th6) {
            th = th6;
            byteArrayOutputStream = null;

            CloseUitls.a(byteArrayOutputStream);
            CloseUitls.a(gZIPOutputStream);
            CloseUitls.a(anonymousClass3);
            throw th;
        }
    }

    public static Serializable g(byte[] bArr) throws Throwable {
        Throwable e;
        ObjectInputStream closeable;
        Closeable closeable2;
        Closeable closeable3 = null;
        if (bArr != null) {
            int length = bArr.length;
            if (length != 0) {
                try {
                    GZIPInputStream gZIPInputStream;
                    ByteArrayInputStream byteArrayInputStream = null;
                    byteArrayInputStream = new ByteArrayInputStream(bArr);
                    try {
                        gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
                    } catch (IOException e2) {
                        e = e2;
                        closeable3 = byteArrayInputStream;
                        byteArrayInputStream = null;
                        throw new RuntimeException("IOException when reading serializable object", e);
                    } catch (Throwable th) {
                        e = th;
                        closeable = null;
                        closeable3 = byteArrayInputStream;
                        CloseUitls.a(closeable3);
                        CloseUitls.a(closeable);
                        throw e;
                    }
                    try {
                        closeable = new ObjectInputStream(gZIPInputStream);
                        try {
                            Serializable serializable = (Serializable) closeable.readObject();

                            CloseUitls.a(gZIPInputStream);
                            CloseUitls.a(closeable);
                            return serializable;
                        } catch (IOException e4) {
                            throw new RuntimeException("IOException when reading serializable object", e4);
                        } catch (ClassNotFoundException e42) {
                            throw new RuntimeException("ClassNotFoundException when writing serializable object", e42);
                        } catch (Throwable e422) {
                            e = e422;
                            CloseUitls.a(closeable3);
                            CloseUitls.a(closeable);
                            throw e;
                        }
                    } catch (IOException e5) {

                        e = e5;
                        closeable = null;

                        throw new RuntimeException("IOException when reading serializable object", e);
                    } catch (ClassNotFoundException e52) {
                        e = e52;
                        closeable = null;
                        throw new RuntimeException("ClassNotFoundException when writing serializable object", e);
                    } catch (Throwable e522) {
                        e = e522;
                        closeable = null;
                        CloseUitls.a(closeable3);
                        CloseUitls.a(closeable);
                        throw e;
                    }
                } catch (IOException e6) {
                    e = e6;
                    closeable = null;
                    throw new RuntimeException("IOException when reading serializable object", e);
                } catch (ClassNotFoundException e7) {
                    e = e7;
                    length = 0;
                    throw new RuntimeException("ClassNotFoundException when writing serializable object", e);
                } catch (Throwable th2) {
                    e = th2;
                    CloseUitls.a(closeable3);
                    throw e;
                }
            }
        }
        return null;
    }

    public static byte[] b(Externalizable externalizable) throws Throwable {
        ObjectOutputStream anonymousClass4;
        Throwable e;
        String name;
        StringBuilder stringBuilder;
        Throwable th;
        if (externalizable == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream;
        GZIPOutputStream gZIPOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                try {
                    anonymousClass4 = new ObjectOutputStream(gZIPOutputStream) {
                        public void writeUTF(String str) {
                            if (str == null) {
                                str = "";
                            }
                            try {
                                super.writeUTF(str);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                } catch (IOException e2) {
                    Throwable th2 = e2;
                    anonymousClass4 = null;
                    e = th2;
                    try {
                        name = externalizable.getClass().getName();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("IOException when writing serializable object of ");
                        stringBuilder.append(name);
                        throw new RuntimeException(stringBuilder.toString(), e);
                    } catch (Throwable th3) {
                        th = th3;
                        CloseUitls.a(byteArrayOutputStream);
                        CloseUitls.a(gZIPOutputStream);
                        CloseUitls.a(anonymousClass4);
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    anonymousClass4 = null;
                    CloseUitls.a(byteArrayOutputStream);
                    CloseUitls.a(gZIPOutputStream);
                    CloseUitls.a(anonymousClass4);
                    throw th;
                }
                try {
                    anonymousClass4.writeUTF(externalizable.getClass().getName());
                    externalizable.writeExternal(anonymousClass4);
                    CloseUitls.a(anonymousClass4);
                    CloseUitls.a(gZIPOutputStream);
                    CloseUitls.a(byteArrayOutputStream);
                    byte[] toByteArray = byteArrayOutputStream.toByteArray();
                    CloseUitls.a(byteArrayOutputStream);
                    CloseUitls.a(gZIPOutputStream);
                    CloseUitls.a(anonymousClass4);
                    return toByteArray;
                } catch (IOException e3) {
                    e = e3;
                    name = externalizable.getClass().getName();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("IOException when writing serializable object of ");
                    stringBuilder.append(name);
                    throw new RuntimeException(stringBuilder.toString(), e);
                }
            } catch (IOException e4) {
                anonymousClass4 = null;
                e = e4;

                name = externalizable.getClass().getName();
                stringBuilder = new StringBuilder();
                stringBuilder.append("IOException when writing serializable object of ");
                stringBuilder.append(name);
                throw new RuntimeException(stringBuilder.toString(), e);
            } catch (Throwable th5) {
                th = th5;
                gZIPOutputStream = null;

                CloseUitls.a(byteArrayOutputStream);
                CloseUitls.a(gZIPOutputStream);

                throw th;
            }
        } catch (IOException e5) {
            gZIPOutputStream = null;
            e = e5;
            name = externalizable.getClass().getName();
            stringBuilder = new StringBuilder();
            stringBuilder.append("IOException when writing serializable object of ");
            stringBuilder.append(name);
            throw new RuntimeException(stringBuilder.toString(), e);
        } catch (Throwable th6) {
            th = th6;
            byteArrayOutputStream = null;
            CloseUitls.a(byteArrayOutputStream);
            throw th;
        }
    }

    public static Externalizable h(byte[] bArr)throws  Throwable {
        GZIPInputStream gZIPInputStream=null;
        Throwable e;
        Externalizable externalizable;
        Throwable e2;
        Externalizable externalizable2;
        ObjectInputStream closeable=null;
        Throwable th;
        if (bArr != null) {
            int length = bArr.length;
            if (length != 0) {
                try {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                    try {
                        gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
                    } catch (IOException e3) {
                        e = e3;
                        externalizable = null;
                        e2 = e;
                        externalizable2 = externalizable;
                        throw new RuntimeException("IOException when reading Externalizable object", e2);
                    } catch (Throwable th2) {
                        e = th2;
                        closeable = null;
                        e2 = e;

                        CloseUitls.a(byteArrayInputStream);
                        CloseUitls.a(gZIPInputStream);
                        CloseUitls.a(closeable);
                        throw e2;
                    }
                    try {
                        closeable = new ObjectInputStream(gZIPInputStream);
                        try {
                            Class cls;
                            String readUTF = closeable.readUTF();
                            synchronized (a) {
                                cls = (Class) a.get(readUTF);
                                if (cls == null) {
                                    cls = Class.forName(readUTF);
                                    a.put(readUTF, cls);
                                }
                            }
                            Externalizable externalizable3 = (Externalizable) cls.newInstance();
                            externalizable3.readExternal(closeable);
                            CloseUitls.a(gZIPInputStream);
                            CloseUitls.a(closeable);
                            return externalizable3;
                        } catch (IOException e7) {
                            e2 = e7;
                        } catch (ClassNotFoundException e8) {
                            e2 = e8;
                            throw new RuntimeException("ClassNotFoundException when writing Externalizable object", e2);
                        } catch (InstantiationException e9) {
                            e2 = e9;
                            throw new RuntimeException("InstantiationException when writing Externalizable object", e2);
                        } catch (IllegalAccessException e10) {
                            e2 = e10;
                            throw new RuntimeException("IllegalAccessException when writing Externalizable object", e2);
                        }
                    } catch (IOException e11) {
                        th = e11;
                        externalizable = null;
                        e2 = th;
                        throw new RuntimeException("IOException when reading Externalizable object", e2);
                    }  catch (Throwable e112222) {
                        th = e112222;
                        closeable = null;
                        e2 = th;
                        CloseUitls.a(gZIPInputStream);
                        CloseUitls.a(closeable);
                        throw e2;
                    }
                } catch (IOException e12) {
                    e = e12;
                    externalizable = null;
                    e2 = e;
                    externalizable2 = externalizable;
                    throw new RuntimeException("IOException when reading Externalizable object", e2);
                } catch (ClassNotFoundException e13) {
                    e = e13;
                    externalizable = null;
                    e2 = e;
                    externalizable2 = externalizable;
                    throw new RuntimeException("ClassNotFoundException when writing Externalizable object", e2);
                } catch (InstantiationException e14) {
                    e = e14;
                    externalizable = null;
                    e2 = e;
                    externalizable2 = externalizable;
                    throw new RuntimeException("InstantiationException when writing Externalizable object", e2);
                } catch (IllegalAccessException e15) {
                    e = e15;

                    throw new RuntimeException("IllegalAccessException when writing Externalizable object", e);
                } catch (Throwable th3) {
                    e2 = th3;

                    CloseUitls.a(gZIPInputStream);
                    CloseUitls.a(closeable);
                    throw e2;
                }
            }
        }
        return null;
    }

    public static byte[] a(JSONArray jSONArray) {
        if (jSONArray == null) {
            return null;
        }
        String jSONArray2 = jSONArray.toString();
        if (jSONArray2 != null) {
            return jSONArray2.getBytes();
        }
        return null;
    }

    public static JSONArray i(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            return new JSONArray(new String(bArr));
        } catch (JSONException unused) {
            return null;
        }
    }

    public static byte[] a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        String jSONObject2 = jSONObject.toString();
        if (jSONObject2 != null) {
            return jSONObject2.getBytes();
        }
        return null;
    }

    public static JSONObject j(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            return new JSONObject(new String(bArr));
        } catch (JSONException unused) {
            return null;
        }
    }

}
