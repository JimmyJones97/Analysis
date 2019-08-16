Java.perform(function(){
	var log = Java.use("com.imvu.core.Logger");
	log.d.implementation = function(a,b ){
	     console.log('Params : '+a+'  ||  '+b  );
	     console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()))
	}
});
