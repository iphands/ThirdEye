package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

public class ThirdEye {
	static Ellipse2D container;
	static Point origin;
	public static String camPath = "/dev/video0";
	public static final int deadzoneSize = 4;

	public static void main(String[] args) throws AWTException, InterruptedException {
		boolean flipped = true;
		final JFrame frame = new JFrame("ThirdEye");
		frame.setSize(400, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// LiveSettings settings = new LiveSettings();
		// frame.add(settings, BorderLayout.SOUTH);
		if (!new File(camPath).exists()) {
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

		BufferedImage bImg;
		try {
			if (flipped) {
				bImg = cam.getFlippedImg();
			} else {
				bImg = cam.getImg();
			}
		} catch (Exception e) {
			bImg = null;
		}
		dot.setCamImg(bImg);
		// dot.fastFindDot();
		dot.findDot();

		final int W = bImg.getWidth();
		final int H = bImg.getHeight();
		frame.setSize(W * 2, H);
		// frame.setSize(W * 2, H + settings.getHeight());

		Point dotLocation = new Point(0, 0);
		final Graphics2D g2d = (Graphics2D) frame.getRootPane().getGraphics();
		final SmoothRob smoothRob = new SmoothRob(origin, deadzoneSize + (deadzoneSize / 2));
		final RocketLauncherControl rLaunch = new RocketLauncherControl(W / 2, H / 2);
		smoothRob.setFlipped(flipped);

		final Color avgColor = new Color(0xaa00ff00, true);
		final Color containerColor = new Color(0x660000ff, true);

		while (true) {

			if (!cam.getCamDevice().equals(camPath)) {
				cam.setCamDevice(camPath);
			}

			try {
				if (flipped) {
					bImg = cam.getFlippedImg();
				} else {
					bImg = cam.getImg();
				}
			} catch (Exception e) {
				e.printStackTrace();
				cam.setCamDevice(camPath);
				continue;
			}

			dot.setCamImg(bImg);
			dot.findDot();

			final Point avg = dot.getAverage();
			final Point med = dot.getMedian();
			try {
				dotLocation = new Point((avg.x + med.x) / 2, (avg.y + med.y) / 2);
			} catch (NullPointerException e) {
				dotLocation = null;
			}

			if ((dotLocation == null) || (container == null) || (origin == null)) {
				initImages(dotLocation);
				smoothRob.setOrigin(origin);
				smoothRob.setFlipped(flipped);
				g2d.drawImage(bImg, null, 0, 0);
				continue;
			}

			g2d.drawImage(bImg, null, 0, 0);
			g2d.drawImage(dot.getDotImg(), null, W, 0);

			// Container
			g2d.setPaint(containerColor);
			g2d.fill(container);

			// Circle
			final Shape circleMedian = new Ellipse2D.Float(dotLocation.x - 2, dotLocation.y - 2, 10, 10);
			g2d.setPaint(avgColor);
			g2d.fill(circleMedian);

			// Box
			final Rectangle box = dot.getOldRect();
			if (box != null) {
				g2d.setPaint(avgColor);
				g2d.draw(box);
			}

			if (dotLocation != origin) {
				// smoothRob.moveMouse(dotLocation);
				// smoothRob.smoothMouseMove(dotLocation);
			}
			rLaunch.move(dotLocation);
		}
	}

	private static void initImages(Point dotLocation) {
		try {
			container = null;
			origin = null;
			container = new Ellipse2D.Float(dotLocation.x - 10, dotLocation.y - 10, deadzoneSize * 4, deadzoneSize * 4);
			origin = dotLocation;
		} catch (Exception e) {
		}
	}
}
