package com.apidech.stupidhack7.worker;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.apidech.stupidhack7.StupidHack7;

public class VolumeMonitorWorker implements Runnable{

	private StupidHack7 instance;
	private TargetDataLine line = null;
	
	public VolumeMonitorWorker(StupidHack7 instance) {
		this.instance = instance;
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Microphone not supported.");
            System.exit(0);
        }
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
        try {
			line.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
        line.start();
	}
	
	@Override
	public void run() {
		byte[] buffer = new byte[line.getBufferSize()];
        
        int bytesRead = line.read(buffer, 0, buffer.length);
        double volume = calculateVolume(buffer, bytesRead);
        
        // Clamp the volume within the defined range
        double clampedVolume = Math.max(0, Math.min(volume, 80));

        // Map the clamped volume to a percentage scale
        double percentage = (clampedVolume - 0) / (80 - 0) * 100;
        
        if(percentage > instance.getCurrentVolumePercentage()) {
        	instance.setCurrentVolumePercentage((int) percentage);
        }
        instance.getGui().setVol(percentage);
	}
	
	private static double calculateVolume(byte[] buffer, int bytesRead) {
        long sum = 0;

        for (int i = 0; i < bytesRead; i += 2) {
            short sample = (short) ((buffer[i + 1] << 8) | buffer[i]);
            sum += Math.abs(sample);
        }

        double average = sum / (bytesRead / 2.0);
        double volume = Math.log10(average) * 20;
        return volume;
    }
}
