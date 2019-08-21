Java.perform(function(){
	var log = Java.use("cmb.pb.shield.StringObfuse");
	log.decode.implementation = function(a){
	     console.log('Params : '+a  );
	     console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()))
	}
});
