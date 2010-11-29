package org.ahands.thirdeye;

import org.ahands.thirdeye.gui.MainGUI;

public class ThirdEye {
	public static void main(String[] args) {
		new Thread(MainGUI.getInstance()).start();
		// new Thread(new MainLoop()).start();
	}
}
