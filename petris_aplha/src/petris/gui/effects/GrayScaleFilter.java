package petris.gui.effects;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;

public class GrayScaleFilter implements Filter {

	@Override
	public BufferedImage processImage(BufferedImage image) {
		BufferedImageOp filter = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		return filter.filter(image, null);
	}

}
