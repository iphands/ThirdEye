package org.ahands.thirdeye;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImgUtils {
	public BufferedImage flipImg(final BufferedImage bImg) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-bImg.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		return op.filter(bImg, null);
	}
}
