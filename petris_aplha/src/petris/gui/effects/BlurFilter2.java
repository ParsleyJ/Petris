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

		float[] matrix = new float[25];
		for (int i = 0; i < 25; i++)
			matrix[i] = 1.0f/25.0f;
		BufferedImageOp blurFilter = new ConvolveOp(new Kernel(5, 5, matrix),
				ConvolveOp.EDGE_NO_OP, null);
		return blurFilter.filter(image, null);
	}
}
