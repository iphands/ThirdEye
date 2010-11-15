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
			// this.fg = vd.getJPEGFrameGrabber(w, h, channel, std, qty);
			this.fg = vd.getRawFrameGrabber(w, h, 0, 0);
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
			// this.fg = vd.getJPEGFrameGrabber(w, h, channel, std, qty);
			this.fg = vd.getRawFrameGrabber(w, h, channel, std);
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
		
		ImageData[] il = new ImageLoader().load(new ByteArrayInputStream(b));
		
		return new Image(Display.getCurrent(),
	}

	public BufferedImage getImg() throws V4L4JException, IOException {
		bb = fg.getFrame();
		b = new byte[bb.limit()];
		bb.get(b);
		return ImageIO.read(new ByteArrayInputStream(b));
	}

	private ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel
					.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel
					.getPixelSize(), palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel
					.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

}