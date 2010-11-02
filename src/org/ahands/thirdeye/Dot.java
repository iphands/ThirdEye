package org.ahands.thirdeye;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dot {

	// 183 239 164
	// private Color dotColor = new Color(0xbdf8a8);
	// 0x54593b yellow
	// 0x655146 pink 463930
	// highlight 595a3b 646c43 7c8e54
	// private Color[] dotColor = { new Color(0x595a3b), new Color(0x646c43), new Color(0x7c8e54) };
	private Color[] dotColor = { new Color(0xfc7c55) };
	private List<Point> foundList = new ArrayList<Point>();
	private BufferedImage camImg;
	private BufferedImage dotImg;
	private final int threshold = 33;

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

	public void findDot() {
		if (camImg == null) {
			return;
		}

		foundList.clear();
		this.dotImg = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		final long start = Calendar.getInstance().getTimeInMillis();

		Color rgb;
		for (Color color : dotColor) {
			int known_r = color.getRed();
			int known_g = color.getGreen();
			int known_b = color.getBlue();

			for (int h = camImg.getHeight(), y = 0; y < h; y = y + 1) {
				for (int w = camImg.getHeight(), x = 0; x < w; x = x + 1) {
					rgb = new Color(camImg.getRGB(x, y));
					// dotImg.setRGB(x, y, rgb.getRGB());
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
		System.out.println(Calendar.getInstance().getTimeInMillis() - start);
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

	public Point getAverage() {
		try {
			int x = 0, y = 0;
			for (Point point : foundList) {
				x = x + point.x;
				y = y + point.y;
			}

			final int len = foundList.size();
			return new Point((x / len), (y / len));
		} catch (ArithmeticException e) {
			return new Point(0, 0);
		}
	}
}
