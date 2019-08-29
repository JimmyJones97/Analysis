'use strict';

// const fs = require("frida-fs");
Java.perform(function(){
	var application = Java.use("cmb.pb.shield.StringObfuse");
	var file = new File("/sdcard/file_new.txt","w");
    var file2 = new File("/sdcard/new.txt","w");
	// application.decode.implementation = function(str){
		// var result =this.decode(str);
	    // console.log('Params : '+str+"||"+result  );
		// file.write("param:"+str+"||"+result+"\n");
		// file.close();
	    // file.write(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()));
		 // return result;
	// }
	
	
	// var pb = Java.use("com.pb.infrastructrue.g.b");
	// pb.a.overload('java.lang.String', 'java.lang.String', 'java.lang.String').implementation = function(str,str2,str3){
	// var result =this.a(str,str2,str3);
	// console.log('Params : '+str+"||"+"str2:"+str2+",str3:"+str3+",ret:"+result  );
	// file.write("param:"+str+"2"+str2+"3"+str3);
	// file.close();
	 // console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()))
	 // return result;
	 // this.handleView(str,str2,str3);
	// }
	// var client = Java.use("cmb.pb.util.ClientBaseFunc.bf");
		// client.a.overload('java.lang.String').implementation = function(str){
		// var result =this.a(str);
	     // console.log('Params : '+str  );
	     // console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()))
		 // return result;
	// }
	
	// var pb = Java.use("com.pb.infrastructrue.g.b");
	// pb.f.overload('java.lang.String', 'java.lang.String' ).implementation = function(str,str2){
		// var result =this.f(str,str2);
		// console.log('FFFFFFFParams : '+str+"||"+"str2:"+str2+",ret:"+result  );
		// file2.write("param:"+str+",2:"+str2 );
		// file.close();
		// file2.write(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()));
		// return result;
	// }
		var pb = Java.use("cmb.pb.j.d");
		pb.a.overload('java.lang.String', 'java.lang.String','java.lang.String' ).implementation = function(str,str2,str3){
		var result =this.a(str,str2,str3);
		console.log('FFFFFFFParams : '+str+"||"+"str2:"+str2+".str3:"+str3+",ret:"+result  );
		// file2.write("param:"+str+",2:"+str2 );
		// file.close();
		// file2.write(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()));
		return result;
	}
	// var client = Java.use("cmb.pb.util.ClientBaseFunc.bf");
		// client.a.overload('java.lang.String').implementation = function(str){
		// var result =this.a(str);
	     // console.log('Params : '+str  );
	     // console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()))
		 // return result;
	// }
	
	
	// var cp = Java.use("com.pb.b.a.b.a");
	// cp.b.overload('java.lang.String' ).implementation = function(str){
		// this.b(str);
		// console.log('bbbParams : '+str+",ret:"  );
		// return
	// }
});
