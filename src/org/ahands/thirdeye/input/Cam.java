package org.ahands.thirdeye.input;

import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.ImageFormat;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class Cam {

	private String camDevString;
	FrameGrabber fg;
	int width = 160;
	int height = 120;
	ByteBuffer bb;
	byte[] b;
	private VideoDevice vd;

	public Cam(String dev) {
		this.camDevString = dev;
		int w = width, h = height, std = V4L4JConstants.STANDARD_WEBCAM, channel = 0, qty = 60;
		try {
			this.vd = new VideoDevice(dev);
			this.fg = vd.getJPEGFrameGrabber(w, h, channel, std, qty);
			this.fg.startCapture();
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
	}

	public String getCamDevice() {
		return this.camDevString;
	}

	public void setCamDevice(String dev) {
		this.camDevString = dev;
		this.fg.stopCapture();

		int w = width, h = height, std = V4L4JConstants.STANDARD_WEBCAM, channel = 0, qty = 60;
		try {
			this.vd = new VideoDevice(dev);
			this.fg = vd.getJPEGFrameGrabber(w, h, channel, std, qty);
			this.fg.startCapture();
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getGreyImg() throws V4L4JException, IOException {
		BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		return op.filter(this.getFlippedImg(), null);

	}

	public BufferedImage getFlippedImg() throws V4L4JException, IOException {
		BufferedImage bufferedImage = this.getImg();
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-bufferedImage.getWidth(null), 0);

		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);

		return bufferedImage;
	}

	public Image getSwtImg() throws V4L4JException, IOException {
		bb = fg.getFrame();
		b = new byte[bb.limit()];
		bb.get(b);

		return new Image(Display.getCurrent(), new ImageData(new ByteArrayInputStream(b)));
	}

	public BufferedImage getImg() throws V4L4JException, IOException {
		bb = fg.getFrame();
		b = new byte[bb.limit()];
		bb.get(b);
		return ImageIO.read(new ByteArrayInputStream(b));
	}
}