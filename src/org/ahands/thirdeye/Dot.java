package org.ahands.thirdeye;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Dot {

	private Color[] dotColor = { new Color(0xffec5332) };
	// private Color[] dotColor = { new Color(0xff583e27) };

	private List<Point> foundList = new ArrayList<Point>();
	private BufferedImage camImg;
	private BufferedImage dotImg;
	private final int threshold = 33;

	private Rectangle oldRect;

	public Color[] getDotColor() {
		return dotColor;
	}

	public void setDotColor(Color[] dotColor) {
		this.dotColor = dotColor;
	}

	public BufferedImage getDotImg() {
		return this.dotImg;
	}

	public BufferedImage getCamImg() {
		return camImg;

	}

	public int getThreshold() {
		return threshold;
	}

	public void setDotImg(BufferedImage dotImg) {
		this.dotImg = dotImg;
	}

	public void setCamImg(BufferedImage camImg) {
		this.camImg = camImg;
	}

	public void fastFindDot() {
		// final long start = Calendar.getInstance().getTimeInMillis();
		if (oldRect != null) {
			// System.out.print("fast ");
			try {
				findDot(oldRect.x, oldRect.y, oldRect.width + oldRect.x, oldRect.height + oldRect.y);
			} catch (ArrayIndexOutOfBoundsException e) {
			}

			if (foundList.size() < 1) {
				oldRect = null;
				fastFindDot();
			}
		} else {
			// System.out.print("norm ");
			final int h = camImg.getHeight();
			final int w = camImg.getWidth();
			findDot(0, 0, w, h);

		}
		// System.out.println(Calendar.getInstance().getTimeInMillis() - start);
	}

	public void findDot(final int startX, final int startY, final int w, final int h) {
		if (camImg == null) {
			return;
		}

		foundList.clear();
		this.dotImg = new BufferedImage(camImg.getWidth(), camImg.getHeight(), BufferedImage.TYPE_INT_RGB);

		Color rgb;
		final int skip = 2;
		for (final Color color : dotColor) {
			final int known_r = color.getRed();
			final int known_g = color.getGreen();
			final int known_b = color.getBlue();

			for (int y = startY; y < h; y = y + skip) {
				for (int x = startX; x < w; x = x + skip) {
					// System.out.println(x + " " + y);
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

	public Rectangle getSimpleContainer() {
		final Point p = getAverage();
		return new Rectangle(p.x - 10, p.y - 10, 20, 20);
	}

	public Ellipse2D getRoundContainer() {
		final Point p = getAverage();
		return new Ellipse2D.Float(p.x - 10, p.y - 10, 20, 20);
	}

	public Rectangle getContainer() {
		int min_x = 9000;
		int max_x = -1;
		int min_y = 9000;
		int max_y = -1;

		for (Point point : foundList) {
			if (point.x < min_x) {
				min_x = point.x;
			} else if (point.x > max_x) {
				max_x = point.x;
			}

			if (point.y < min_y) {
				min_y = point.y;
			} else if (point.y > max_y) {
				max_y = point.y;
			}
		}

		return new Rectangle(min_x, min_y, (max_x - min_x), (max_y - min_y));
	}

	// public Rectangle getBox() {
	// try {
	// final List<Double> xList = new ArrayList<Double>();
	// final List<Double> yList = new ArrayList<Double>();
	//
	// for (final Point point : foundList) {
	// xList.add(point.getX());
	// yList.add(point.getY());
	// }
	//
	// Collections.sort(xList);
	// Collections.sort(yList);
	//
	// int x = xList.get(0).intValue();
	// int y = yList.get(0).intValue();
	// int w = xList.get(xList.size() - 1).intValue() - x;
	// int h = yList.get(yList.size() - 1).intValue() - y;
	//
	// final int extraPix = 20;
	// final Rectangle rect = new Rectangle(x - extraPix, y - extraPix, w + (extraPix * 2), h + (extraPix * 2));
	// this.setOldRect(rect);
	// return rect;
	// } catch (IndexOutOfBoundsException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	public Point getMedian() {
		try {
			final List<Double> xList = new ArrayList<Double>();
			final List<Double> yList = new ArrayList<Double>();

			for (final Point point : foundList) {
				xList.add(point.getX());
				yList.add(point.getY());
			}

			Collections.sort(xList);
			Collections.sort(yList);

			final int x = xList.get(0).intValue();
			final int y = yList.get(0).intValue();
			final int w = xList.get(xList.size() - 1).intValue() - x;
			final int h = yList.get(yList.size() - 1).intValue() - y;

			final int extraPix = 20;
			final Rectangle rect = new Rectangle(x - extraPix, y - extraPix, w + (extraPix * 2), h + (extraPix * 2));
			this.setOldRect(rect);

			return new Point(((xList.get(xList.size() / 2)).intValue()), ((yList.get(yList.size() / 2)).intValue()));
		} catch (Exception e) {
			return null;
		}
	}

	public Point getAverage() {
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

	public void setOldRect(Rectangle oldRect) {
		this.oldRect = oldRect;
	}

	public Rectangle getOldRect() {
		return oldRect;
	}
}
