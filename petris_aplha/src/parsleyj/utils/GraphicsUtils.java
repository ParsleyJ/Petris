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
				g.drawLine(x, y-i, x + width-1, y-i);
				g.drawLine(x, y+height+i-1, x+width-1, y+height+i-1);
			}
		}
		
		if (horizBlurSize > 0)
		{
			g.setColor(previousColor);
			int vertAlphaStep = previousColor.getAlpha() / vertBlurSize;
			for (int i = 1; i <= horizBlurSize; ++i)
			{
				g.setColor(setColorAlpha(previousColor, g.getColor().getAlpha() - vertAlphaStep));
				g.drawLine(x-i, y, x-i, y+height-1);
				g.drawLine(x+width+i-1, y, x+width+i-1, y+height-1);
			}
		}
	
			
	}
	
	public static int[] getColorSteppers(Color c1, Color c2, int dividend)
	{
		int r=0, g=0, b=0, a=0;
		r= (c2.getRed() - c1.getRed()) /dividend;
		g= (c2.getGreen() - c1.getGreen()) /dividend;
		b= (c2.getBlue() - c1.getBlue()) /dividend;
		a= (c2.getAlpha() - c1.getAlpha())  /dividend;
		return new int[]{r, g, b, a};
	}
	
	public enum GradientMode { HORIZONTAL, VERTICAL, DOWNLEFT, UPLEFT, UPRIGHT, DOWNRIGHT, CENTER}
	
	public static void fillGradientRect(Graphics g, Color c1, Color c2, int x, int y, int width, int height, GradientMode mode)
	{
		switch(mode)
		{
		case VERTICAL:
		{
			int [] step = getColorSteppers(c1,c2,height);
			for(int i = 0; i < height; ++i)
			{
				g.setColor(new Color(c1.getRed()+step[0]*i, c1.getGreen()+step[1]*i, c1.getBlue()+step[2]*i, c1.getAlpha()+step[3]*i));
				g.drawLine(x, y+i, x+width-1, y+i);
			}
			break;
		}
		case HORIZONTAL:
		{
			int [] step = getColorSteppers(c1,c2,width);
			for(int i = 0; i < width; ++i)
			{
				g.setColor(new Color(c1.getRed()+step[0]*i, c1.getGreen()+step[1]*i, c1.getBlue()+step[2]*i, c1.getAlpha()+step[3]*i));
				g.drawLine(x+i, y, x+i, y+height-1);
			}
			break;
		}
		case DOWNLEFT:
		{
			if (height == width)
			{
				int [] step = getColorSteppers(c1,c2,width*2);
				Color halfColor = new Color(c2.getRGB());
				for(int i = 0; i < width-1; ++i)
				{
					halfColor = new Color(c1.getRed()+step[0]*i, c1.getGreen()+step[1]*i, c1.getBlue()+step[2]*i, c1.getAlpha()+step[3]*i);
					g.setColor(halfColor);
					g.drawLine(x+width-1-i, y, x+width-1, y+i);
				}
				step = getColorSteppers(halfColor,c2,width);
				for(int i = 0; i < width; ++i)
				{ 
					g.setColor(new Color(halfColor.getRed()+step[0]*i, halfColor.getGreen()+step[1]*i, halfColor.getBlue()+step[2]*i, halfColor.getAlpha()+step[3]*i));
					g.drawLine(x+width-1-i, y+height-1, x, y+i);
				}
			}
			break;
		}
		case DOWNRIGHT:
		{
			if (height == width)
			{
				int [] step = getColorSteppers(c1,c2,width*2);
				Color halfColor = new Color(c2.getRGB());
				for(int i = 0; i < width-1; ++i)
				{
					halfColor = new Color(c1.getRed()+step[0]*i, c1.getGreen()+step[1]*i, c1.getBlue()+step[2]*i, c1.getAlpha()+step[3]*i);
					g.setColor(halfColor);
					g.drawLine(x+i, y, x, y+i);
				}
				step = getColorSteppers(halfColor,c2,width);
				for(int i = 0; i < width; ++i)
				{ 
					g.setColor(new Color(halfColor.getRed()+step[0]*i, halfColor.getGreen()+step[1]*i, halfColor.getBlue()+step[2]*i, halfColor.getAlpha()+step[3]*i));
					g.drawLine(x+width-1, y+i, x+i, y+height-1);
				}
			}
			break;
		}
		case CENTER:
		{
			if (height == width)
			{
				int [] step = getColorSteppers(c1,c2,width/2);
				for(int i = 0; i <= width/2; ++i)
				{
					g.setColor(new Color(c1.getRed()+step[0]*i, c1.getGreen()+step[1]*i, c1.getBlue()+step[2]*i, c1.getAlpha()+step[3]*i));
					g.drawRect(x+width/2-i, y+height/2-i, i*2, i*2);
				}
			}
			break;
		}
		}			
	}
	
	public static void fillBlurredGlitchRect(Graphics g, Color c, int x, int y, int width, int height, int horizBlurSize, int vertBlurSize)
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
		if (horizBlurSize > 0)
		{
			int vertAlphaStep = previousColor.getAlpha() / vertBlurSize;
			for (int i = 1; i <= horizBlurSize; ++i)
			{
				g.setColor(setColorAlpha(previousColor, g.getColor().getAlpha() - vertAlphaStep));
				g.drawLine(x-i, y, x-i, y+height-1);
				g.drawLine(x+width+i, y, x+width+i, y+height-1);
			}
		}
			
	}
	
	public static Color setColorAlpha(Color c, int alpha)
	{
		if (alpha>255 || alpha < 0) return c;
		return new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
	}
}
