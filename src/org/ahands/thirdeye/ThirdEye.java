package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;

import javax.swing.JFrame;

import org.ahands.thirdeye.controllers.MouseDragger;
import org.ahands.thirdeye.controllers.RocketLauncherControl;
import org.ahands.thirdeye.controllers.SmoothRob;
import org.ahands.thirdeye.gui.LiveSettings;
import org.ahands.thirdeye.input.Cam;

public class ThirdEye {
	static Ellipse2D deadZone;
	static Point origin;
	public static String camPath = "/dev/video0";
	public static final int deadzoneSize = 8;

	public static void main(String[] args) throws AWTException, InterruptedException {

		new Thread(new LiveSettings()).start();

		boolean flipped = true;

		// final JFrame frame = new JFrame("ThirdEye");
		// frame.setSize(400, 300);
		// frame.setVisible(true);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		dot.fastFindDot();

		int W = 640;
		int H = 480;
		// frame.setSize((W * 2) + 20, H + 40);
		// frame.setSize(W * 2, H + settings.getHeight());

		Point dotLocation = new Point(0, 0);
		// final Graphics2D g2d = (Graphics2D) frame.getRootPane().getGraphics();
		final SmoothRob smoothRob = new SmoothRob(origin, deadzoneSize + (deadzoneSize / 2));
		final MouseDragger dragger = new MouseDragger(bImg.getWidth(), bImg.getHeight());
		final RocketLauncherControl rLaunch = new RocketLauncherControl(W / 2, H / 2);
		smoothRob.setFlipped(flipped);

		Shape box = dot.getOldRect();
		final Color avgColor = new Color(0xdd00ff00, true);
		final Color containerColor = new Color(0xaa0000ff, true);
		final Color boxColor = new Color(0xaaff00ff, true);

		long time = 1;

		while (true) {
			final long start = Calendar.getInstance().getTimeInMillis();
			// W = frame.getWidth() / 2;
			// H = frame.getHeight();

			// g2d.setPaint(avgColor);
			// g2d.drawString(String.format("%s %.2f", "fps: ", 1000 / (float) time), 0, 10);

			if (!cam.getCamDevice().equals(camPath)) {
				cam.setCamDevice(camPath);
			}

			try {
				if (flipped) {
					bImg = cam.getFlippedImg();
				} else {
					bImg = cam.getImg();
				}
				// bImg = cam.getGreyImg();
			} catch (Exception e) {
				e.printStackTrace();
				cam.setCamDevice(camPath);
				continue;
			}

			dot.setCamImg(bImg);
			dot.fastFindDot();
			// dot.multiPass(30);

			final Point avg = dot.getAverage();
			final Point med = dot.getMedian();
			try {
				dotLocation = new Point((avg.x + med.x) / 2, (avg.y + med.y) / 2);
			} catch (NullPointerException e) {
				dotLocation = null;
			}

			// draw image
			final BufferedImage scaleBImg = new BufferedImage(160, 120, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D scaleG2D = scaleBImg.createGraphics();
			scaleG2D.drawImage(bImg, null, 0, 0);

			if ((dotLocation == null) || (deadZone == null) || (origin == null)) {
				dot.setOldRect(null);
				initImages(dotLocation);
				smoothRob.setOrigin(origin);
				smoothRob.setFlipped(flipped);
				time = Calendar.getInstance().getTimeInMillis() - start;

				// g2d.drawImage(scaleBImg, 0, 0, W, H, null);
				// g2d.drawString("dot: not found", 0, 25);
				continue;
			}

			// g2d.drawImage(dot.getDotImg(), W, 0, W, H, null);

			// Container
			// scaleG2D.setPaint(containerColor);
			// scaleG2D.fill(deadZone);

			// Circle
			final Shape circleMedian = new Ellipse2D.Float(dotLocation.x - 2, dotLocation.y - 2, 5, 5);
			scaleG2D.setPaint(avgColor);
			scaleG2D.fill(circleMedian);

			// g2d.drawString("dot: " + dotLocation.x + ", " + dotLocation.y, 0, 25);

			// Box
			box = dot.getOldRect();
			if (box != null) {
				scaleG2D.setPaint(boxColor);
				scaleG2D.draw(box);
			}

			// g2d.drawImage(scaleBImg, 0, 0, W, H, null);
			scaleG2D.dispose();

			// if (dotLocation != origin) {
			// smoothRob.moveMouse(dotLocation);
			// // smoothRob.smoothMouseMove(dotLocation);
			// }
			// rLaunch.move(dotLocation);

			dragger.move(dotLocation);

			time = Calendar.getInstance().getTimeInMillis() - start;
		}
	}

	private static void initImages(Point dotLocation) {
		try {
			deadZone = null;
			origin = null;
			deadZone = new Ellipse2D.Float(dotLocation.x - 10, dotLocation.y - 10, deadzoneSize * 4, deadzoneSize * 4);
			origin = dotLocation;
		} catch (Exception e) {
		}
	}
}
