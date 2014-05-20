package petris.gui.effects;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class BlurFilter2 implements Filter {
	public BufferedImage processImage(BufferedImage image) {
		
		/*float[] blurMatrix = { 0.0f / 1.0f, 1.0f / 8.0f, 0.0f / 1.0f, 
				1.0f / 8.0f, 1.0f / 2.0f, 1.0f / 8.0f, 
				0.0f / 1.0f, 1.0f / 8.0f, 0.0f / 1.0f };*/

		float[] matrix = new float[16];
		for (int i = 0; i < 16; i++)
			matrix[i] = 1.0f/16.0f;
		BufferedImageOp blurFilter = new ConvolveOp(new Kernel(4, 4, matrix),
				ConvolveOp.EDGE_NO_OP, null);
		return blurFilter.filter(image, null);
	}
}
