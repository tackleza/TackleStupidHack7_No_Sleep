package com.apidech.stupidhack7;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;

public class StupidHack7Gui {

	private StupidHack7 instance;

	private JFrame frmStupidHack;
	private JProgressBar currentVolBar;
	private JSpinner time;
	private JComboBox<String> timeUnit;
	private JLabel countdownText;
	private JButton btnReset;
	private JScrollBar targetSound;
	private JLabel currentPercentageVol;

	/**
	 * Create the application.
	 */
	public StupidHack7Gui(StupidHack7 instance) {
		this.instance = instance;
	}

	public void start() {
		initialize();
		frmStupidHack.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStupidHack = new JFrame();
		frmStupidHack.setTitle("Stupid Hack 7 - Programmer ห้ามพัก ห้ามหลับ ห้ามตาย | By Tackle4826 @The โง่ Hackathon ครั้งที่ 7");
		frmStupidHack.setBounds(100, 100, 819, 676);
		frmStupidHack.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStupidHack.getContentPane().setLayout(null);

		JLabel lblDeveloper = new JLabel("Developer ห้ามพัก ห้ามหลับ ห้ามตาย");
		lblDeveloper.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeveloper.setFont(new Font("Layiji MaHaNiYom V1.41", Font.BOLD, 48));
		lblDeveloper.setBounds(12, 11, 784, 42);
		frmStupidHack.getContentPane().add(lblDeveloper);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(
				new ImageIcon(StupidHack7Gui.class.getResource("/com/apidech/stupidhack7/resources/no-sleep.jpg")));
		lblNewLabel.setBounds(153, 65, 500, 331);
		frmStupidHack.getContentPane().add(lblNewLabel);

		this.currentVolBar = new JProgressBar();
		currentVolBar.setStringPainted(true);
		currentVolBar.setBounds(131, 408, 548, 23);
		frmStupidHack.getContentPane().add(currentVolBar);

		timeUnit = new JComboBox<String>();
		timeUnit.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Second (วินาที)", "Minute (นาที)", "Hour (ชั่วโมง)" }));
		timeUnit.setSelectedIndex(1);
		timeUnit.setBounds(414, 540, 182, 26);
		timeUnit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTarget();
			}
		});
		frmStupidHack.getContentPane().add(timeUnit);

		JSeparator separator = new JSeparator();
		separator.setBounds(82, 498, 642, 2);
		frmStupidHack.getContentPane().add(separator);

		this.countdownText = new JLabel("Countdown");
		countdownText.setFont(new Font("Dialog", Font.BOLD, 16));
		countdownText.setHorizontalAlignment(SwingConstants.CENTER);
		countdownText.setBounds(217, 569, 387, 33);
		frmStupidHack.getContentPane().add(countdownText);

		JLabel lblTimeSettings = new JLabel("Time Settings (ตั้งค่าเวลา)");
		lblTimeSettings.setFont(new Font("Dialog", Font.BOLD, 20));
		lblTimeSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeSettings.setBounds(131, 498, 548, 30);
		frmStupidHack.getContentPane().add(lblTimeSettings);

		this.time = new JSpinner(new SpinnerNumberModel(15, 1, 999999, 1));
		this.time.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateTarget();
			}
		});
		this.time.setBounds(278, 542, 124, 23);
		frmStupidHack.getContentPane().add(time);

		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.killPlayerProcess();
			}
		});
		btnReset.setForeground(Color.WHITE);
		btnReset.setBackground(new Color(224, 27, 36));
		btnReset.setBounds(356, 608, 105, 27);
		btnReset.setVisible(false);
		frmStupidHack.getContentPane().add(btnReset);

		currentPercentageVol = new JLabel("...");
		currentPercentageVol.setFont(new Font("Dialog", Font.BOLD, 20));
		currentPercentageVol.setHorizontalAlignment(SwingConstants.CENTER);
		currentPercentageVol.setBounds(131, 469, 548, 17);
		frmStupidHack.getContentPane().add(currentPercentageVol);
		
		targetSound = new JScrollBar();
		targetSound.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				instance.setTargetVolumePercentage(targetSound.getValue());
				currentPercentageVol
				.setText("??? % / " + instance.getTargetVolumePercentage() + " %");
			}
		});
		targetSound.setValue(50);
		targetSound.setOrientation(JScrollBar.HORIZONTAL);
		targetSound.setBounds(131, 439, 548, 23);
		frmStupidHack.getContentPane().add(targetSound);
	}

	public void setVol(double vol) {
		this.currentVolBar.setValue((int) vol);
		this.currentPercentageVol
				.setText(String.format("%.2f", vol) + " % / " + instance.getTargetVolumePercentage() + " %");
		if(vol > instance.getTargetVolumePercentage()) {
			setProgressBarColor(Color.BLUE);
		}
		else {
			setProgressBarColor(Color.RED);
		}
	}

	public void updateTarget() {
		int unit = 0;
		switch (this.timeUnit.getSelectedIndex()) {
		case 0:
			unit = 1;
			break; // Second
		case 1:
			unit = 60;
			break; // Minute
		case 2:
			unit = 3600;
			break; // Hour
		default:
			unit = 1;
		}

		int timeInput = (int) this.time.getValue();
		int total = unit * timeInput;

		instance.setTargetCountdown(total);
		instance.setCurrentCountdown(total); // Reset
	}

	public void setCountdownString(String message) {
		countdownText.setText(message);
	}

	public void showResetBtn(boolean isShow) {
		btnReset.setVisible(isShow);
	}
	
	public void setProgressBarColor(Color color) {
		currentVolBar.setForeground(color);
	}
	
//	public void setTimeUnitIndex(int index) {
//		timeUnit.setSelectedIndex(index);
//	}
//	
//	public int getTimeUnitIndex() {
//		return timeUnit.getSelectedIndex();
//	}
//	
//	public void setTargetVolumeBar(int value) {
//		targetSound.setValue(value);
//	}
//	
//	public int getTargetVolumeBar() {
//		return targetSound.getValue();
//	}
//	
//	public void setTimeGUIValue(int value) {
//		time.setValue(value);
//	}
//	
//	public int getTimeGUIValue() {
//		return (int) time.getValue();
//	}
}
