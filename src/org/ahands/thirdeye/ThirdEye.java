package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

public class ThirdEye {
	static Ellipse2D container;
	static Point origin;

	public static void main(String[] args) throws AWTException, InterruptedException {
		final String camPath = "/dev/video0";

		if (!new File(camPath).exists()) {

			final JFrame devWaitFrame = new JFrame("No devices found...");
			devWaitFrame.setVisible(true);
			devWaitFrame.setBounds(20, 20, 300, 150);
			devWaitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			while (true) {
				System.out.printf("Waiting for device %s...\n", camPath);
				Thread.sleep(1500);
				if (new File(camPath).exists()) {
					break;
				}
			}
		}

		final Cam cam = new Cam(camPath);
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
		initImages(dot);
		final Graphics2D g2d = (Graphics2D) frame.getRootPane().getGraphics();
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
			if ((dotLocation == null) || (container == null) || (origin == null)) {
				initImages(dot);
				smoothRob.setOrigin(origin);
				g2d.drawImage(bImg, null, 0, 0);

				try {
					System.err.printf("cont %f,%f\n", container.getCenterX(), container.getY());
				} catch (Exception e) {
					// TODO: handle exception
				}
				continue;
			}

			g2d.drawImage(bImg, null, 0, 0);
			g2d.drawImage(dot.getDotImg(), null, W, 0);

			// Container
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

	private static void initImages(Dot dot) {
		try {
			container = null;
			origin = null;
			container = (dot.getRoundContainer());
			origin = dot.getAverage();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
