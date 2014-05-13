package parsleyj.utils;

import java.awt.Color;
import java.awt.Graphics;

public class GraphicsUtils {
	
	public static void fillBlurredBorderRect(Graphics g, Color c, int x, int y, int width, int height, int horizBlurSize, int vertBlurSize)
	{
		Color previousColor = g.getColor();
		g.setColor(c);
		g.fillRect(x, y, width, height);
		if (vertBlurSize > 0)
		{
			int vertAlphaStep = previousColor.getAlpha() / vertBlurSize;
			for (int i = 1; i <= vertBlurSize; ++i)
			{
				g.setColor(setColorAlpha(previousColor, g.getColor().getAlpha() - vertAlphaStep));
				g.drawLine(x, y-i, x + width, y-i);
				g.drawLine(x, y+height+i-1, x+width, y+height+i-1);
			}
		}
		g.setColor(previousColor);
	}
	
	public static Color setColorAlpha(Color c, int alpha)
	{
		return new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
	}
}
