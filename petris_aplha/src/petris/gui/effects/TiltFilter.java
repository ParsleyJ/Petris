package petris.gui.effects;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;


public class TiltFilter implements Filter {

	public BufferedImage processImage(BufferedImage image) {
		
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(1));
		BufferedImageOp filter =  new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return filter.filter(image, null);

	}

}
