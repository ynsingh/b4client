package com.ehelpy.brihaspati4.authenticate;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function is debug class just to enable programmer to  debug the package
// The level varies from 0 to 5 as per requirement of the person working on it
public class debug_level {
    private static  int level ;
    public  static String print_out = null;
    public static void debug(int passed_level, String print) {
        String path = properties_access.read_property("client.properties","level" );
        level = Integer.parseInt(path.trim());
        if (level<=passed_level) {
            System.out.println(print);
        }
        else {
            return;
        }
    }
}
