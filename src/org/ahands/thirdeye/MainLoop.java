package org.ahands.thirdeye;

import java.io.IOException;

import org.ahands.thirdeye.input.Cam;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class MainLoop implements Runnable {
	@Override
	public void run() {
		final Cam cam = new Cam("/dev/video0");
		while (true) {
			try {
				cam.getFlippedImg();

			} catch (V4L4JException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
