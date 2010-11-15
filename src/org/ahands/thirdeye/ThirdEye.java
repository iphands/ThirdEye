package org.ahands.thirdeye;

import java.awt.AWTException;

import org.ahands.thirdeye.gui.LiveSettings;

public class ThirdEye {
	public static void main(String[] args) throws AWTException, InterruptedException {
		new Thread(new LiveSettings()).start();
		// new Thread(new MainLoop()).start();
	}
}
