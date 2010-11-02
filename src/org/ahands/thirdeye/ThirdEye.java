package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ThirdEye {
	public static void main(String[] args) throws AWTException {
		final Cam cam = new Cam("/dev/video0");
		final Dot dot = new Dot();
		final Robot rob = new Robot();
		final JFrame frame = new JFrame("Image");
		BufferedImage bImg;
		bImg = cam.getImg();
		dot.setCamImg(bImg);
		dot.findDot();

		final int W = bImg.getWidth();
		final int H = bImg.getHeight();
		frame.setBounds(0, 0, W * 2, H);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		boolean isInBox = true;
		Point dotLocation = new Point(0, 0);

		final Graphics2D g2d = (Graphics2D) frame.getRootPane().getGraphics();
		// final Rectangle container = (dot.getSimpleContainer());
		final Ellipse2D container = (dot.getRoundContainer());
		final Point origin = dot.getAverage();
		int threshold = 5;
		while (true) {

			// bImg = cam.getFlippedImg();
			bImg = cam.getImg();
			dot.setCamImg(bImg);
			dot.findDot();
			dotLocation = dot.getAverage();

			// bImg.setRGB(dotLocation.x, dotLocation.y, 0xffff0000);
			g2d.drawImage(bImg, null, 0, 0);
			g2d.drawImage(dot.getDotImg(), null, W, 0);

			// Circle
			final Shape circle = new Ellipse2D.Float(dotLocation.x - 2, dotLocation.y - 2, 8, 8);
			g2d.draw(circle);
			g2d.setPaint(Color.blue);
			g2d.fill(circle);

			// Box
			g2d.draw(container);
			g2d.setPaint(Color.yellow);

			if (dotLocation != origin) {
				Point current = MouseInfo.getPointerInfo().getLocation();
				if (dotLocation.x > (origin.x + threshold)) {
					int rate = dotLocation.x - origin.x;
					rob.mouseMove(current.x - (rate), current.y);
				} else if (dotLocation.x < (origin.x - threshold)) {
					int rate = origin.x - dotLocation.x;
					rob.mouseMove(current.x + (rate), current.y);
				}

				current = MouseInfo.getPointerInfo().getLocation();
				if (dotLocation.y > (origin.y + threshold)) {
					int rate = dotLocation.y - origin.y;
					rob.mouseMove(current.x, (current.y + rate));
				} else if (dotLocation.y < (origin.y - threshold)) {
					int rate = origin.y - dotLocation.y;
					rob.mouseMove(current.x, (current.y - rate));
				}
			}
		}
	}
}
