package petris.gui.effects;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class BrightUpFilter implements Filter {

	@Override
	public BufferedImage processImage(BufferedImage image) {
		float[] data = new float[] {0,0,0,
									0,2,0,
									0,0,0};
		BufferedImageOp filter =  new ConvolveOp(new Kernel(3,3,data));
		return filter.filter(image, null);
	}

}
