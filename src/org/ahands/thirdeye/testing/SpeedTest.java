package org.ahands.thirdeye.testing;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

public class SpeedTest {
	private static Color[] dotColor = { new Color(0xfc7c55), new Color(0xfc7c22), new Color(0xfc7c11),
			new Color(0xfc7c00) };
	private static List<Point> foundList = new ArrayList<Point>();
	private static BufferedImage camImg;
	private static BufferedImage dotImg;
	private static int threshold = 33;

	public static void main(String[] args) throws InterruptedException {
		try {
			camImg = ImageIO.read(new File("resources/test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		findDot();
		try {
			ImageIO.write(dotImg, "png", new File("/tmp/dots/findDot.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		findDot2();
		findDot3();
		try {
			ImageIO.write(dotImg, "png", new File("/tmp/dots/findDot3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		findDot4();

		Thread.sleep(1000);

		int loopLen = 22;
		long fd1 = 0;
		long fd2 = 0;
		long fd3 = 0;
		long fd4 = 0;

		long start;
		System.out.printf("Loop, findDot2(), findDot(), findDot3(), findDot4()\n");

		// for (int j = 0; j < 255; j++) {
		// threshold = j;
		// findDot4();
		// try {
		// ImageIO.write(dotImg, "png", new File("/tmp/dots/findDot3-" + j + ".png"));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		final int sleepSpeed = 1;

		for (int j = 0; j < loopLen; j++) {

			Thread.sleep(sleepSpeed);
			start = Calendar.getInstance().getTimeInMillis();
			for (int i = 0; i < loopLen; i++) {
				findDot();
			}
			fd1 += (Calendar.getInstance().getTimeInMillis() - start);
			System.out.print(j + ", " + (Calendar.getInstance().getTimeInMillis() - start) + ", ");
			// System.out.println(getMedian());
			// System.out.println(getAverage());

			Thread.sleep(sleepSpeed);
			start = Calendar.getInstance().getTimeInMillis();
			for (int i = 0; i < loopLen; i++) {
				findDot2();
			}
			fd2 += (Calendar.getInstance().getTimeInMillis() - start);
			System.out.print((Calendar.getInstance().getTimeInMillis() - start) + ", ");

			Thread.sleep(sleepSpeed);
			start = Calendar.getInstance().getTimeInMillis();
			for (int i = 0; i < loopLen; i++) {
				findDot3();
			}
			fd3 += (Calendar.getInstance().getTimeInMillis() - start);
			System.out.print((Calendar.getInstance().getTimeInMillis() - start) + ", ");

			Thread.sleep(sleepSpeed);
			start = Calendar.getInstance().getTimeInMillis();
			for (int i = 0; i < loopLen; i++) {
				findDot4();
			}
			fd4 += (Calendar.getInstance().getTimeInMillis() - start);
			System.out.println((Calendar.getInstance().getTimeInMillis() - start));

		}

		System.out.printf("%d %d %d %d\n", fd1, fd2, fd3, fd4);
	}

	public static Point getMedian() {

		final List<Double> xList = new ArrayList<Double>();
		final List<Double> yList = new ArrayList<Double>();

		for (final Point point : foundList) {
			xList.add(point.getX());
			yList.add(point.getY());
		}

		Collections.sort(xList);
		Collections.sort(yList);

		return new Point(((xList.get(xList.size() / 2)).intValue()), ((yList.get(yList.size() / 2)).intValue()));
	}

	public static Point getAverage() {
		try {
			int x = 0, y = 0;
			for (final Point point : foundList) {
				x = x + point.x;
				y = y + point.y;
			}

			final int len = foundList.size();
			return new Point((x / len), (y / len));
		} catch (ArithmeticException e) {
			return null;
		}
	}

	public static void findDot() {
		if (camImg == null) {
			return;
		}

		foundList.clear();

		final int h = camImg.getHeight();
		final int w = camImg.getWidth();

		dotImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		Color rgb;
		for (Color color : dotColor) {
			int known_r = color.getRed();
			int known_g = color.getGreen();
			int known_b = color.getBlue();

			for (int y = 0; y < h; y = y + 2) {
				for (int x = 0; x < w; x = x + 2) {
					rgb = new Color(camImg.getRGB(x, y));
					if (Math.abs(known_g - rgb.getGreen()) <= threshold) {
						if (Math.abs(known_r - rgb.getRed()) <= threshold) {
							if (Math.abs(known_b - rgb.getBlue()) <= threshold) {
								foundList.add(new Point(x, y));
								dotImg.setRGB(x, y, 0xff00ff00);
							}
						}
					}
				}
			}
		}
	}

	private static void findDot2() {
		findDot();
	}

	private static void findDot3() {
		if (camImg == null) {
			return;
		}

		foundList.clear();

		final int h = camImg.getHeight();
		final int w = camImg.getWidth();

		dotImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		Color rgb;
		for (final Color color : dotColor) {
			final int known_r = color.getRed();
			final int known_g = color.getGreen();
			final int known_b = color.getBlue();

			for (int y = 0; y < h; y = y + 2) {
				for (int x = 0; x < w; x = x + 2) {
					rgb = new Color(camImg.getRGB(x, y));
					if (Math.abs(known_g - rgb.getGreen()) <= threshold) {
						if (Math.abs(known_r - rgb.getRed()) <= threshold) {
							if (Math.abs(known_b - rgb.getBlue()) <= threshold) {
								foundList.add(new Point(x, y));
								dotImg.setRGB(x, y, 0xff00ff00);
							}
						}
					}
				}
			}
		}
	}

	private static void findDot4() {
		findDot3();
	}
}
