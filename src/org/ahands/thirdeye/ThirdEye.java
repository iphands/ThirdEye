package org.ahands.thirdeye;

import org.ahands.thirdeye.gui.LiveSettings;

public class ThirdEye {
	public static void main(String[] args) {
		new Thread(new LiveSettings()).start();
		// new Thread(new MainLoop()).start();
	}
}
