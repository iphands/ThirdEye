package org.ahands.thirdeye;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class Cam {

	File camDevice;
	FrameGrabber fg;
	int width = 640;
	int height = 480;
	ByteBuffer bb;
	byte[] b;

	public Cam(String dev) {
		this.camDevice = new File(dev);
		int w = 640, h = 480, std = V4L4JConstants.STANDARD_WEBCAM, channel = 0, qty = 60;
		try {
			VideoDevice vd = new VideoDevice(dev);
			this.fg = vd.getJPEGFrameGrabber(w, h, channel, std, qty);
			this.fg.startCapture();
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getFlippedImg() {
		BufferedImage bufferedImage = this.getImg();
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-bufferedImage.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);

		return bufferedImage;
	}

	public BufferedImage getImg() {
		try {
			bb = fg.getFrame();
			b = new byte[bb.limit()];
			bb.get(b);
			return ImageIO.read(new ByteArrayInputStream(b));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}