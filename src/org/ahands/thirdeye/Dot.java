package org.ahands.thirdeye;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Calendar;

public class Dot {

	// 183 239 164
	private Color dotColor = new Color(0xbdf8a8);
	private BufferedImage camImg;
	private BufferedImage dotImg;
	private final int threshold = 20;

	public Color getDotColor() {
		return dotColor;
	}

	public void setDotColor(Color dotColor) {
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

		this.dotImg = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		final long start = Calendar.getInstance().getTimeInMillis();

		int known_r = dotColor.getRed();
		int known_g = dotColor.getGreen();
		int known_b = dotColor.getBlue();
		Color rgb;

		for (int h = camImg.getHeight(), y = 0; y < h; y = y + 2) {
			for (int w = camImg.getHeight(), x = 0; x < w; x = x + 2) {
				rgb = new Color(camImg.getRGB(x, y));
				if (Math.abs(known_g - rgb.getGreen()) <= threshold) {
					if (Math.abs(known_r - rgb.getRed()) <= threshold) {
						if (Math.abs(known_b - rgb.getBlue()) <= threshold) {
							dotImg.setRGB(x, y, 0xff00ff00);
						}
					}
				}
			}
		}
		System.out.println(Calendar.getInstance().getTimeInMillis() - start);
	}

	public Point getMedian() {
		return null;
	}
}
