package com.ehelpy.brihaspati4.voip;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class voip_key implements Runnable {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    static long sym_key;
    public static byte[] secret_ke1 = null;
    private SecretKey secret_Key = null;
    public voip_key(long key)
    {
        sym_key = key;

    }

    public void run()
    {
        try {
            byte[] sym_byte_key = longtobyte_bytetolongarray.longtobytearraysymkey(sym_key);
            secret_Key = new SecretKeySpec(sym_byte_key, "AES");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized(this)
        {
            notifyAll();
        }
    }

    public synchronized SecretKey get()
    throws InterruptedException
    {
        while (secret_Key == null)
            wait();

        return secret_Key;
    }//return secret_Key;
}
