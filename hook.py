# -*- coding: utf-8 -*-

import frida, sys


def on_message(message, data):
    if message['type'] == 'send':
        print("[*] {0}".format(message['payload']))
    else:
        print(message)

"""
    var Base64 = Java.use('com.rytong.emp.security.Base64');
    send("Base64");

    Base64.azbycx.implementation = function (str)
    {
        //console.log(str); 
        var ret = this.azbycx(str);
        //console.log(ret); 
        return ret;
    };
    
       

    function Bytes2Str(bytes)
    {
        var str = "";

        if(bytes == null)
        {
            return str;
        }
        for(var i=0; i< bytes.length; i++)
        {
            var tmp = bytes[i];
            tmp = bytes[i] & 255  
            test = tmp.toString(16)
            if(tmp >= 0x10)
            {
                str +=  "\\r\\n0x"+ test;
            }
            else
            {
                str +=  "\\r\\n0x0"+ test;
            }


        }
        return str;
    }

    function Bytes2StrEx(bytes)
    {
        var str = "";

        if(bytes == null)
        {
            return str;
        }
        for(var i=0; i< bytes.length; i++)
        {
            var tmp = bytes[i];
            tmp = bytes[i] & 255  
            test = tmp.toString(16)
            str +=  test
        }
        return str;
    } 
    
        var FileManager = Java.use('com.rytong.emp.data.FileManager');
    send("saveFileByAD");
    FileManager.saveFileByAD.implementation = function (context,str1,str2)
    { 
        console.log("a.str2: " + str2); 
        var ret = this.saveFileByAD(context,str1,str2);  
        return ret;
    }; 
     
   
    var HttpManager = Java.use('com.rytong.emp.net.HttpManager');  
    send("sendPostRequest");
    HttpManager.sendPostRequest.overload('java.lang.String', 'java.lang.Object', 'boolean', 'java.lang.String',
     'java.lang.String', 'com.rytong.emp.render.EMPThreadPool$Task').implementation = function (str1,obj1,bool1,str2,
     str3,task)
    { 
        console.log("sendPostRequest.str1: " + str1);  
        console.log("sendPostRequest.str2: " + str2); 
        console.log("sendPostRequest.str3: " + str3); 
        var ret = this.sendPostRequest(str1,obj1,bool1,str2,str3,task);
        return ret;
    };


    send("user/make_cert");
    ClientHello.a.overload('android.content.Context', 'java.lang.String', 'java.lang.String').implementation = function (context,str1,str2)
    { 
        console.log("a.str1: " + str1);  
        console.log("a.str2: " + str2); 
        var ret = this.a(context,str1,str2);
        
        return ret;
    };  
    

    
    
    var RSAAdapter = Java.use('com.rytong.emp.security.adapter.RSAAdapter');
    send("RSAAdapter");
    RSAAdapter.verifyCertificate.implementation = function (bArr,str1,context)
    { 
        console.log("verifyCertificate.bArr: " + Bytes2Str(bArr)); 
        console.log("verifyCertificate.str1: " + str1); 
        var ret = this.verifyCertificate(bArr,str1,context);  
        console.log("verifyCertificate.ret: " + ret); 
        return ret;
    }; 
    
        var ClientHello = Java.use('com.rytong.emp.net.ClientHello');
    send("user/hello");
    ClientHello.a.overload('android.content.Context', 'java.lang.String', 'java.lang.String','java.lang.String').implementation = function (context,str1,str2,str3)
    { 
        console.log("a.str1: " + str1);  
        console.log("a.str2: " + str2); 
        console.log("a.str3: " + str3); 
        var ret = this.a(context,str1,str2,str3);
        return ret;
    };
    
    send("user/make_cert");
    ClientHello.a.overload('android.content.Context', 'java.lang.String', 'java.lang.String', '[B', '[B', 'com.rytong.emp.render.EMPThreadPool$Task').implementation = function (context,str1,str2,byte1,byte2,task)
    { 
        console.log("a.str1: " + str1);  
        console.log("a.str2: " + str2); 
        console.log("a.byte1: " + Bytes2Str(byte1));  
        console.log("a.byte2: " + Bytes2Str(byte2)); 
        var ret = this.a(context,str1,str2,byte1,byte2,task);    
        return ret;
    };  
       
  
    ClientHello.f.overload().implementation = function ()
    { 
        send("f");
        var ret = this.f(); 
        console.log("f.ret: " + Bytes2Str(ret)); 
        console.log("f.ret: " + ret.length); 
        return ret;
    };  
       

    ClientHello.c.overload('android.content.Context', 'java.lang.String').implementation = function (context,str1)
    { 
        send("c");
        console.log("c.str1: " + str1);  
        var ret = this.c(context,str1);  
        console.log("c.ret: " + Bytes2Str(ret)); 
        console.log("c.ret: " + ret.length); 
        if(null != ret)
        {
            console.log("c.ret: " + ret.length); 
        }
        return ret;
    };  
    
  
    ClientHello.a.overload('android.content.Context', '[B', '[B', '[B').implementation = function (context,byte1,byte2,byte3)
    { 
        send("a");
        var ret = this.a(context,byte1,byte2,byte3);  
        console.log("old.ret: " + Bytes2Str(ret)); 
        if(null != ret)
        {
            console.log("old.ret: " + ret.length); 
        }
       
        return ret;
    };  
        
    send("h");
    ClientHello.h.implementation = function ()
    { 
        var ret = this.h();  
        console.log("h.ret: " + Bytes2Str(ret)); 
        console.log("h.ret: " + ret.length); 
        return ret;
    };
    
   
    ClientHello.e.overload('[B').implementation = function (byte1)
    { 
        send("e");
        console.log("e.byte1: " + Bytes2Str(byte1)); 
        var ret = this.e(byte1);  
        console.log("e.ret: " + Bytes2Str(ret)); 
        console.log("e.ret: " + ret.length); 
        return ret;
    };
      
 
    ClientHello.j.implementation = function ()
    {
        send("j");
        var ret = this.j();  
        console.log("j.ret: " + Bytes2Str(ret)); 
        if(null != ret)
        {
            console.log("j.ret: " + ret.length); 
        } 
        return ret;
    };  
     

    var OfflineController = Java.use('com.rytong.emp.data.offline.OfflineController');
    send("getHashFromClient");
    OfflineController.getHashFromClient.implementation = function ()
    { 
        var ret = this.getHashFromClient();
        console.log("getHashFromClient.ret: " + ret); 
        return ret;
    };
    
    send("getHashFromOptServer");
    OfflineController.getHashFromOptServer.implementation = function ()
    { 
        var ret = this.getHashFromOptServer();
        console.log("getHashFromOptServer.ret: " + ret); 
        return ret;
    };
    
    send("getHashFromH5");
    OfflineController.getHashFromH5.implementation = function ()
    { 
        var ret = this.getHashFromH5();
        console.log("getHashFromH5.ret: " + ret); 
        return ret;
    };
    
    
    var HttpManager = Java.use('com.rytong.emp.net.HttpManager');  
    send("sendPostRequest");
    HttpManager.sendPostRequest.overload('java.lang.String', 'java.lang.Object', 'boolean', 'java.lang.String',
     'java.lang.String', 'com.rytong.emp.render.EMPThreadPool$Task').implementation = function (str1,obj1,bool1,str2,
     str3,task)
    { 
        console.log("sendPostRequest.str1: " + str1);  
        console.log("sendPostRequest.str1 len: " + str1.length);  
        console.log("sendPostRequest.str2: " + str2); 
        console.log("sendPostRequest.str3: " + str3); 
        var ret = this.sendPostRequest(str1,obj1,bool1,str2,str3,task);
        return ret;
    };



    var g = Java.use('com.newland.app.util.g');  
    g.zW.implementation = function ()
    { 
        send("zW()");
        var ret = this.zW();
        console.log("zW.ret: " + ret); 
        return ret;
    };

    var g = Java.use('com.newland.app.util.g');  
    g.cD.implementation = function (strkey)
    { 
        send("cD()");
        console.log("cD.strkey: " + strkey); 
        var ret = this.cD(strkey);
        console.log("cD.ret: " + ret); 
        return ret;
    }; 


    var CertUtil = Java.use('com.cib.sm.comunication.CertUtil');  
    CertUtil.encryptByPublicKeyNoPadding.implementation = function (byte1, strkey)
    { 
        send("encryptByPublicKeyNoPadding()");
        console.log("encryptByPublicKeyNoPadding.strkey: " + strkey); 
        var ret = this.encryptByPublicKeyNoPadding(byte1, strkey);
        console.log("encryptByPublicKeyNoPadding.ret: " + ret); 
        return ret;
    };


"""
java_hook_code = """
Java.perform(function () 
{

    var EncrySm2ClientServiceImpl = Java.use('com.cib.sm.EncrySm2ClientServiceImpl');  
    EncrySm2ClientServiceImpl.PkEncryptEPin.implementation = function (sm2PublicKeyX,sm2PublicKeyY,pws,userid)
    { 
        send("PkEncryptEPin");
        console.log("PkEncryptEPin.sm2PublicKeyX: " + sm2PublicKeyX);
        console.log("PkEncryptEPin.sm2PublicKeyY: " + sm2PublicKeyY);
        console.log("PkEncryptEPin.pws: " + pws);
        console.log("PkEncryptEPin.userid: " + userid); 
        var ret = this.PkEncryptEPin(sm2PublicKeyX,sm2PublicKeyY,pws,userid);
        console.log("PkEncryptEPin.ret: " + ret); 
        return ret;
    };
    
    EncrySm2ClientServiceImpl.encrypt.implementation = function (pws,userid)
    { 
        send("transfer");
        console.log("transfer.pws: " + Bytes2StrEx(pws));
        console.log("transfer.userid: " + Bytes2StrEx(userid)); 
        var ret = this.encrypt(pws, userid);
        
        return ret;
    };
    
    
    var Util = Java.use('com.cib.sm.util.Util');  
    Util.hexToByte.implementation = function (password)
    { 
        send("hexToByte");
        console.log("hexToByte.password: " + password);
        var ret = this.hexToByte(password);
        console.log("hexToByte.ret: " + Bytes2StrEx(ret)); 
        return ret;
    };
    
    var g = Java.use('com.newland.app.util.g');  
    g.decrypt.implementation = function (data,key)
    { 
        send("decrypt");
        console.log("decrypt.data: " + data);
        console.log("decrypt.key: " + key);
        var ret = this.decrypt(data,key);
        console.log("decrypt.ret: " + ret); 
        return ret;
    };
    
    var g = Java.use('com.newland.app.util.g');  
    g.encrypt.implementation = function (data,key)
    { 
        send("encrypt");
        console.log("encrypt.data: " + data);
        console.log("encrypt.key: " + key);
        var ret = this.encrypt(data,key);
        console.log("encrypt.ret: " + ret); 
        return ret;
    };
    
    

    function Bytes2StrEx(bytes)
    {
        var str = "";
	    if(bytes == null)
	    {
		    return str;
	    }
	    for(var i=0; i < bytes.length; i++)
	    {
		    var tmp = bytes[i];
		    tmp = bytes[i] & 255  
		    test = tmp.toString(16)
		    if(tmp >= 0x10)
		    {
		    	str +=  "0x"+ test + ",";
		    }
		    else
		    {
			    str +=  "0x0"+ test + ",";
		    }           
	    }
	    return str;
    };

    function Bytes2Str(bytes)
    {
        var str = "";
	    if(bytes == null)
	    {
		    return str;
	    }
	    for(var i=0; i< bytes.length; i++)
	    {
		    var tmp = bytes[i];
		    tmp = bytes[i] & 255  
		    test = tmp.toString(16)
		    if(tmp >= 0x10)
		    {
		    	str +=  "0x"+ test + ",";
		    }
		    else
		    {
			    str +=  "0x0"+ test + ",";
		    }           
	    }
	    return str;
    };
    
});
"""

native_hook_code = """
    var addr = Module.findBaseAddress("libart.so");   
   
  
    console.log("addr: " + addr); 
   
    var exports = Module.enumerateExportsSync("libart.so");
    
    for(i = 0; i < exports.length; i++)
    {  
        if("_ZN3art3Dbg8GoActiveEv" == exports[i].name)
        {
            console.log("name" + exports[i].name); 
            var address = exports[i].address;
            console.log("address: " + address); 
                
            var addr_pointer = new NativePointer(address).sub(1);
            
            var buffer = Memory.readByteArray(addr_pointer, 32);
            console.log(buffer); 
           
            var memory = new Array(12);
            memory[0]= 0xDF;
            memory[1]= 0xF8;
            memory[2]= 0xC4;
            memory[3]= 0x39;
            memory[4]= 0x4D;    
            memory[5]= 0xF8;
            memory[6]= 0x24;
            memory[7]= 0x4D;
            memory[8]= 0xCD;
            memory[9]= 0xE9;
            memory[10]= 0x03;
            memory[11]= 0x78;
            Memory.writeByteArray(addr_pointer, memory);
            
            addr_pointer= new NativePointer(address).sub(1);
            buffer = Memory.readByteArray(addr_pointer, 32);
            console.log(buffer);
        }    
    }    
"""

"""
u0_a163   27396 831   2111608 286984 SyS_epoll_ 00e61ced00 S com.cib.cibmb
u0_a163   27446 831   1773644 110556 SyS_epoll_ 00e61ced00 S com.cib.cibmb:CMCoreService
u0_a163   27502 831   1786072 121468 SyS_epoll_ 00e61ced00 S com.cib.cibmb:remote
"""

src = """
Java.perform(function(){
    var clazz = Java.use("com.tencent.mm.a.g");
    send("开始");

    clazz.u.implementation = function(param)
    {
        console.log("param" + param); 
        var ret = this.u(param); 
        return ret;
    };
});
"""


if __name__ == "__main__":
    javaHookCode = r"hook.js"
    nativeHookCode = r"nativeHook.js"

    # if file:
    process = frida.get_device_manager().enumerate_devices()[-1].attach("com.cib.cibmb") # CMCoreService push com.tencent.mm  com.cib.cibmb
    # process = frida.get_usb_device().attach("com.ss.android.ugc.aweme")

    script = process.create_script(java_hook_code) # native_hook_code java_hook_code
    script.on('message', on_message)
    script.load()
    sys.stdin.read()

    #
    # rdev = frida.get_remote_device()
    # processes = rdev.enumerate_processes()
    #
    # for process in processes:
    #     print process

    # frida.get_usb_device().attach("com.tencent.mm")
    #
    #

    #     buf = None;
    #     try:
    #         file = open(nativeHookCode)
    #         buf = file.read();
    #
    #     except IOError:
    #         print "[-] 文件没有找到 ..."
    # else:
    #     print("file notFound")


