package com.apidech.stupidhack7;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.apidech.stupidhack7.worker.ClockWorker;
import com.apidech.stupidhack7.worker.VolumeMonitorWorker;

public class StupidHack7 {

	private StupidHack7Gui gui;
	private int targetCountdown = 60 * 15; // Default 15 mins
	private int currentCountdown = targetCountdown;
	private int targetVolumePercentage = 50;

	private Process playerProcess;
	private int currentVolumePercentage;
	
	private ArrayList<String> resourcesVideo;
	
	public StupidHack7() {
		this.resourcesVideo = new ArrayList<String>();
//		this.resourcesVideo.add("alert.mp4");
//		this.resourcesVideo.add("alert2.mp4");
		this.resourcesVideo.add("alert3.mp4");
	}

	public void start() {
		gui = new StupidHack7Gui(this);
		gui.start();

		// Extract resource file
		for(String file : this.resourcesVideo) {
			extractFile(file);
		}

		ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
		worker.scheduleAtFixedRate(new VolumeMonitorWorker(this), 200, 200, TimeUnit.MILLISECONDS);

		ScheduledExecutorService clockWorker = Executors.newSingleThreadScheduledExecutor();
		clockWorker.scheduleWithFixedDelay(new ClockWorker(this), 1000, 1000, TimeUnit.MILLISECONDS);
		
//		loadConfig();
	}

	public StupidHack7Gui getGui() {
		return gui;
	}

	public int getTargetCountdown() {
		return targetCountdown;
	}

	public void setTargetCountdown(int targetCountdown) {
		this.targetCountdown = targetCountdown;
//		saveConfig();
	}

	public int getCurrentCountdown() {
		return currentCountdown;
	}

	public void setCurrentCountdown(int currentCountdown) {
		this.currentCountdown = currentCountdown;
		getGui().setCountdownString(getTimeAsString(currentCountdown));
	}

	public void extractFile(String resource) {
		if (new File(resource).exists()) {
			return;
		}
		String resourcePath = "com/apidech/stupidhack7/resources/" + resource;

		try (InputStream inputStream = StupidHack7.class.getClassLoader().getResourceAsStream(resourcePath);
				OutputStream outputStream = new FileOutputStream(resource)) {

			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			System.out.println("Resource file extracted successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String randomAlert() {
		return this.resourcesVideo.get(new Random().nextInt(this.resourcesVideo.size()));
	}

	public void killPlayerProcess() {
		if (playerProcess != null && playerProcess.isAlive()) {
			playerProcess.destroyForcibly();
		}
		playerProcess = null;
	}

	public void setPlayerProcess(Process playerProcess) {
		this.playerProcess = playerProcess;
	}

	public static String getTimeAsString(int time) {
		StringBuilder sb = new StringBuilder();

		int days, hours, minutes, seconds;

		days = (int) Math.ceil(time / 86400);
		time = time - (days * 86400);

		hours = (int) Math.ceil(time / 3600);
		time = time - (hours * 3600);

		minutes = (int) Math.ceil(time / 60);
		time = time - (minutes * 60);

		seconds = time;

		if (days > 0) {

			if (days == 1) {
				sb.append(days);
				sb.append(" day ");
			} else {
				sb.append(days);
				sb.append(" days ");
			}
		}

		if (hours > 0) {

			if (hours == 1) {
				sb.append(hours);
				sb.append(" hour ");
			} else {
				sb.append(hours);
				sb.append(" hours ");
			}
		}

		if (minutes > 0) {
			if (minutes == 1) {
				sb.append(minutes);
				sb.append(" minute ");
			} else {
				sb.append(minutes);
				sb.append(" minutes ");
			}
		}

		if (seconds > 0) {
			if (minutes > 0 || hours > 0) {
				sb.append("and ");
			}

			if (seconds == 1) {
				sb.append(seconds);
				sb.append(" second");
			} else {
				sb.append(seconds);
				sb.append(" seconds");
			}
		}
		if (sb.length() == 0)
			return "0 Sec";
		return sb.toString();
	}

	public int getTargetVolumePercentage() {
		return targetVolumePercentage;
	}

	public void setTargetVolumePercentage(int targetVolumePercentage) {
		this.targetVolumePercentage = targetVolumePercentage;
//		saveConfig();
	}

	public int getCurrentVolumePercentage() {
		return currentVolumePercentage;
	}

	public void setCurrentVolumePercentage(int currentVolumePercentage) {
		this.currentVolumePercentage = currentVolumePercentage;
	}
}
