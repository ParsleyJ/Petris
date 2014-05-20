package petris.gui.effects;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class EdgeFilter implements Filter {

	@Override
	public BufferedImage processImage(BufferedImage image) {
		float[] data = new float[] {-1,-1,-1,
									-1, 8,-1,
									-1,-1,-1};
		BufferedImageOp filter =  new ConvolveOp(new Kernel(3,3,data));
		return filter.filter(image, null);
	}

}
