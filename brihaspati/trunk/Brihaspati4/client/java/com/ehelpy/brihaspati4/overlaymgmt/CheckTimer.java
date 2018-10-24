package com.ehelpy.brihaspati4.overlaymgmt;

import java.util.Timer;
import java.util.TimerTask;

import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class CheckTimer extends Thread{

	public void run() {
		// TODO Auto-generated method stub
		timer.schedule(startHM, 3000,30000);
	}
	static Timer timer = new Timer();
	static TimerTask startHM = new TimerTask() {
		@Override
		   public void run() {
			
			SysOutCtrl.SysoutSet("Heartbeat monitoring has started", 1);
			HeartbeatMonitoring.heartbeatCheck();
			
				
				
		}
			
		};
	
			
	
}
