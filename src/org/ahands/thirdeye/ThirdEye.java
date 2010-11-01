package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class ThirdEye {
	public static void main(String[] args) throws AWTException {

		Cam cam;
		try {
			cam = new Cam("/dev/video0");
		} catch (V4L4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Dot dot = new Dot();
		final Robot rob = new Robot();
		final JFrame frame = new JFrame("Image");
		frame.setBounds(0, 0, 640 * 2, 480);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Graphics2D g2d = (Graphics2D) frame.getRootPane().getGraphics();

		boolean isInBox = true;
		Point dotLocation = new Point(0, 0);
		BufferedImage bImg;

		while (true) {
			bImg = cam.getImg();
			g2d.drawImage(bImg, null, 0, 0);

			dot.setCamImg(bImg);
			dot.findDot();
			g2d.drawImage(dot.getDotImg(), null, 641, 0);

			dotLocation = dot.getMedian();
			if (isInBox) {

			}
		}
	}
}
