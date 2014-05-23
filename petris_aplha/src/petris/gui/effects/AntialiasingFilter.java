package petris.gui.effects;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class AntialiasingFilter implements Filter {

	@Override
	public BufferedImage processImage(BufferedImage image) {
		float[] blurMatrix = { 0f/1f, 1f/12f, 0f/1f, 
				 			   1f/12f, 2f/3f, 1f/12f,
							   0f/1f, 1f/12f, 0f/1f };
		BufferedImageOp blurFilter = new ConvolveOp(new Kernel(3, 3, blurMatrix),
				ConvolveOp.EDGE_NO_OP, null);
		return blurFilter.filter(image, null);
	}

}
