package com.ehelpy.brihaspati4.mail.b4mail;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.*;

public class GetPlatformDetails {
    private static final Logger b4maillog = LogManager.getLogger(GetPlatformDetails.class);
    private String os = System.getProperty("os.name");  //Windows or linux or MAC
    private String osbitVersion = System.getProperty("os.arch");    // x86 or ppc etc
    private String jvmbitVersion = System.getProperty("sun.arch.data.model");   //32 bit or 64bit
    private String javaVersion = System.getProperty("java.vm.version");
    private String sysUserName = System.getProperty("user.name");

    public String getOs() {
        this.os = os;
        return os;
    }

    public String getOsbitVersion() {
        this.osbitVersion = osbitVersion;
        return osbitVersion;
    }

    public String getJvmbitVersion() {
        this.jvmbitVersion = jvmbitVersion;
        return jvmbitVersion;
    }

    public String getJavaVersion() {
        this.javaVersion=javaVersion;
        return javaVersion;
    }

    public String getSysUserName() {
        this.sysUserName=sysUserName;
        return sysUserName;
    }
    public GetPlatformDetails(String os, String osbitVersion, String jvmbitVersion) {
        b4maillog.debug(os + " : " + osbitVersion + " : " + jvmbitVersion+" bit");
        for(Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
            System.out.println(String.format("%s = %s", e.getKey(), e.getValue()));
        }
    }
}
