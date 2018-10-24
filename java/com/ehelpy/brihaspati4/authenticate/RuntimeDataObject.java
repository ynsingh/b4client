package com.ehelpy.brihaspati4.authenticate ;
//package com.ehelpy.brihaspati4.scss.utlis;

/* License - Read License.txt in root directory of this codebase.
* Design - YNSingh (2016)
* Implementation -Chetna (2016).
* Date, Change description -  .
* Copyright 2016 : YNSingh
*/
//package com.ehelpy.brihaspati4.scss.utlis;

/* License - Read License.txt in root directory of this codebase.
* Design - YNSingh (2016)
* Implementation -Chetna (2016).
* Date, Change description -  .
* Copyright 2016 : YNSingh
*/

import java.util.Properties;
import java.io.InputStream;

public class RuntimeDataObject {
    private static Properties prop=null;

    public static RuntimeDataObject getController() {
        RuntimeDataObject obj = null;
        if(obj==null) {
            obj=new RuntimeDataObject();
            obj.start();
        }
        return obj;
    }

    private void start() {
        prop=new Properties();
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("resources/config/b4scss.properties");
            prop.load(inputStream);
        } catch(Exception e) { }
    }

    public static String getMasterUrl() {
        return prop.getProperty("MasterServerURL");
    }
}
