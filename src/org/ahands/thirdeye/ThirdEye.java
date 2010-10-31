package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class ThirdEye {
	public static void main(String[] args) throws AWTException {

		final Cam cam = new Cam("/dev/video/null");
		final Dot dot = new Dot();
		final Robot rob = new Robot();

		boolean isInBox = true;
		Point dotLocation = new Point(0, 0);
		BufferedImage bImg;

		while (true) {
			bImg = cam.getImg();
			dot.setImg(bImg);
			dot.findDot();
			dotLocation = dot.getMedian();
			if (isInBox) {

			}
		}
	}
}
