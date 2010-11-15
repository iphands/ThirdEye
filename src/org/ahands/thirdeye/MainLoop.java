package org.ahands.thirdeye;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import org.ahands.thirdeye.controllers.MouseDragger;
import org.ahands.thirdeye.controllers.RocketLauncherControl;
import org.ahands.thirdeye.controllers.SmoothRob;
import org.ahands.thirdeye.input.Cam;

public class MainLoop implements Runnable {

	private Ellipse2D deadZone;
	private Point origin;
	private String camPath = "/dev/video0";
	private final int deadzoneSize = 8;
	private static Graphics2D nonScaleG2d;

	public static Graphics2D getNonScaleG2d() {
		return nonScaleG2d;
	}

	@Override
	public void run() {

		boolean flipped = true;

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
		dot.fastFindDot();

		int W = 640;
		int H = 480;

		Point dotLocation = new Point(0, 0);

		final BufferedImage nonScaleBImg = new BufferedImage(160, 120, BufferedImage.TYPE_INT_ARGB);
		nonScaleG2d = nonScaleBImg.createGraphics();

		SmoothRob smoothRob;
		MouseDragger dragger;
		try {
			smoothRob = new SmoothRob(origin, deadzoneSize + (deadzoneSize / 2));
			dragger = new MouseDragger(bImg.getWidth(), bImg.getHeight());
		} catch (AWTException e1) {
			e1.printStackTrace();
			return;
		}

		final RocketLauncherControl rLaunch = new RocketLauncherControl(W / 2, H / 2);
		smoothRob.setFlipped(flipped);

		Shape box = dot.getOldRect();
		final Color avgColor = new Color(0xdd00ff00, true);
		final Color containerColor = new Color(0xaa0000ff, true);
		final Color boxColor = new Color(0xaaff00ff, true);

		long time = 1;

		while (true) {
			final long start = Calendar.getInstance().getTimeInMillis();
			nonScaleG2d.setPaint(avgColor);
			nonScaleG2d.drawString(String.format("%s %.2f", "fps: ", 1000 / (float) time), 0, 10);

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

				nonScaleG2d.drawImage(scaleBImg, 0, 0, W, H, null);
				nonScaleG2d.drawString("dot: not found", 0, 25);
				continue;
			}

			nonScaleG2d.drawImage(dot.getDotImg(), W, 0, W, H, null);

			// Container
			scaleG2D.setPaint(containerColor);
			scaleG2D.fill(deadZone);

			// Circle
			final Shape circleMedian = new Ellipse2D.Float(dotLocation.x - 2, dotLocation.y - 2, 5, 5);
			scaleG2D.setPaint(avgColor);
			scaleG2D.fill(circleMedian);

			nonScaleG2d.drawString("dot: " + dotLocation.x + ", " + dotLocation.y, 0, 25);

			// Box
			box = dot.getOldRect();
			if (box != null) {
				scaleG2D.setPaint(boxColor);
				scaleG2D.draw(box);
			}

			nonScaleG2d.drawImage(scaleBImg, 0, 0, W, H, null);
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

	private void initImages(Point dotLocation) {
		try {
			this.deadZone = null;
			this.origin = null;
			this.deadZone = new Ellipse2D.Float(dotLocation.x - 10, dotLocation.y - 10, deadzoneSize * 4,
					deadzoneSize * 4);
			this.origin = dotLocation;
		} catch (Exception e) {
		}
	}
}
