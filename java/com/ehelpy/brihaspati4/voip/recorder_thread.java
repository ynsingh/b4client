package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import javax.crypto.SecretKey;
import javax.sound.sampled.TargetDataLine;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class recorder_thread extends Thread {
    public TargetDataLine audio_in;
    byte byte_buff[] = new byte[512];
    byte[] data_send,enc_data = new byte[512];
    public InetAddress far_endip;
    public int comn_port;
    public static boolean flag = true;
    public static boolean status = false;
    private SecretKey sec_key = null;
    public recorder_thread(SecretKey sec_key) {
        this.sec_key = sec_key;
    }

    @Override
    public void run() {
        Long pack = 0l;

        try {
            while (voip_start.calling) {
                if(player_thread.din.isClosed()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    audio_in.read(byte_buff, 0, byte_buff.length);
                    DatagramPacket data = new DatagramPacket(byte_buff, byte_buff.length, player_thread.incoming.getAddress(), player_thread.incoming.getPort());
                    data_send = data.getData();
                    debug_level.debug(0,"secret key is"+ sec_key);
                    enc_data = encrypt_msg.encrypt_calldata(sec_key, data_send);
                    data = new DatagramPacket(enc_data, enc_data.length,player_thread.incoming.getAddress(), player_thread.incoming.getPort() );
                    System.out.println("The ip address and socket is"+player_thread.incoming.getAddress()+ player_thread.incoming.getPort());
                    debug_level.debug(1,"send: #"+ pack++);
                    player_thread.din.send(data);
                    debug_level.debug(1, "thread is running");
                }

            }
        }
        catch (IOException e) {
            voip_start.calling = false;
            if(audio_in!=null) audio_in.close();
            player_thread.din.disconnect();
            player_thread.din.close();

        }

        finally {
            debug_level.debug(1,"call in recorder: recorder is stop");
            if(audio_in!=null) audio_in.close();
            status =true;
            if (player_thread.din!= null) player_thread.din.disconnect();
            if (player_thread.din!= null) player_thread.din.close();


            debug_level.debug(1,"call in player: recorder is drain and close");
        }
        status = true;
        debug_level.debug(1, "call in recorder: audio is drain and close");
    }

}
