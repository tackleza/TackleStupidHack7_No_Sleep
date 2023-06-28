package com.apidech.stupidhack7.worker;

import java.io.IOException;

import com.apidech.stupidhack7.StupidHack7;

public class ClockWorker implements Runnable{

	private StupidHack7 instance;
	public ClockWorker(StupidHack7 instance) {
		this.instance = instance;
	}
	
	@Override
	public void run() {
		if(instance.getCurrentCountdown() > 0) {
			instance.getGui().showResetBtn(false);
			//Check sound level
			if(instance.getCurrentVolumePercentage() > instance.getTargetVolumePercentage()) { //Reset
				instance.setCurrentCountdown(instance.getTargetCountdown());
			}
			else {
				instance.setCurrentCountdown(instance.getCurrentCountdown()-1);
			}
			instance.setCurrentVolumePercentage(0);
		}
		else {
			instance.getGui().showResetBtn(true);
			String[] cmd = {"vlc", "--fullscreen", "--repeat", instance.randomAlert()};
			try {
				Process process = Runtime.getRuntime().exec(cmd);
				instance.setPlayerProcess(process);
				process.waitFor();
				instance.setCurrentCountdown(instance.getTargetCountdown());
			} catch (IOException e) {} catch (InterruptedException e) {
				e.printStackTrace();
			}
			instance.getGui().showResetBtn(false);
		}
	}
}
