package org.ahands.thirdeye.input;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FakeCam {

	private int loopIterator = 0;

	public BufferedImage getImg() throws IOException {
		File[] folders = new File("resources/").listFiles();
		BufferedImage bImg;
		if (loopIterator <= folders.length) {
			bImg = ImageIO.read(new File("resources/" + folders[loopIterator].getName() + "/test.png"));
			loopIterator++;
		} else {
			bImg = ImageIO.read(new File("resources/" + folders[0].getName() + "/test.png"));
			loopIterator = 0;
		}
		return bImg;
	}

	public BufferedImage getFlippedImg() throws IOException {
		return getImg();
	}
}
