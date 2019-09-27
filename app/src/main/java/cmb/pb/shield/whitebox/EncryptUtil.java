package cmb.pb.shield.whitebox;

/**************************************************************************
 * project:Analysis
 * Email: 
 * file:EncryptUtil
 * Created by pwd61 on 9/6/2019 5:50 PM
 * description:
 *
 *
 *
 *
 *
 ***************************************************************************/

public class EncryptUtil {
    static public native String decryptcbc(String str);

    static {
        /**
         *
         *  假面
         *  <P>
         *
         *  </P>
         *
         */
        System.loadLibrary("wb");
        //System.loadLibrary(StringObfuse.decode("140F"));
    }

}
