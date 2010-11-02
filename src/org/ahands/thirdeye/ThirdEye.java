package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ThirdEye {
	public static void main(String[] args) throws AWTException {
		final Cam cam = new Cam("/dev/video0");
		final Dot dot = new Dot();
		final JFrame frame = new JFrame("ThirdEye");
		BufferedImage bImg;
		bImg = cam.getImg();
		dot.setCamImg(bImg);
		dot.findDot();

		final int W = bImg.getWidth();
		final int H = bImg.getHeight();
		frame.setBounds(0, 0, W * 2, H);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Point dotLocation = new Point(0, 0);

		final Graphics2D g2d = (Graphics2D) frame.getRootPane().getGraphics();
		final Ellipse2D container = (dot.getRoundContainer());
		final Point origin = dot.getAverage();
		final int threshold = 5;
		final SmoothRob smoothRob = new SmoothRob(origin, threshold + (threshold / 2));
		final Color dotColor = new Color(0xdd00ff00, true);
		final Color containerColor = new Color(0xaa0000ff, true);
		while (true) {

			// bImg = cam.getFlippedImg();
			bImg = cam.getImg();
			dot.setCamImg(bImg);
			dot.findDot();
			dotLocation = dot.getAverage();

			g2d.drawImage(bImg, null, 0, 0);
			g2d.drawImage(dot.getDotImg(), null, W, 0);

			// Box
			g2d.setPaint(containerColor);
			g2d.fill(container);

			// Circle
			final Shape circle = new Ellipse2D.Float(dotLocation.x - 2, dotLocation.y - 2, 10, 10);
			g2d.setPaint(dotColor);
			g2d.fill(circle);

			if (dotLocation != origin) {
				smoothRob.moveMouse(dotLocation);
				// smoothRob.smoothMouseMove(dotLocation);
			}
		}
	}
}
