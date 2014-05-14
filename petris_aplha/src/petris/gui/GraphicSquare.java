package petris.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GraphicsUtils.GradientMode;
import petris.Item;
import petris.gui.RainbowColor;
import petris.gui.RainbowColor.Phase;



public class GraphicSquare extends Item{
	
	private Color bgColor;
	private Color bordColor;
	public int borderSize;
	public int squareHeight = 30;
	public int squareWidth = 30;
	private SquareStyle style;
	public static RainbowColor rainbowSlow = new RainbowColor(1);
	public static RainbowColor rainbowFast = new RainbowColor(8);
	public static RainbowColor rainbowFast2 = new RainbowColor(8,Phase.GREEN);
	public static RainbowColor rainbowVeryFast = new RainbowColor(32);
	
	
	
	public enum SquareStyle {
		noBorder, SimpleBorder, Border3d, Cartoon, RoundSquareBorder, 
		Circle, CircleBorder, Octagon, Medieval, BlurredOutBorder, BlurredInBorder,
		SquareMadness, Granular, VerticalGradient, HorizontalGradient, DownLeftGradient, 
		DownRightGradient, CenterGradient, Rainbow, Rainbow2, ColorMadness, LucyInTheSky,
		GhostSquare, Glitchy, YouNeedGlasses, CityLights, Rotatuille, Smiley
	}
	
	public GraphicSquare()//Default ctor
	{
		
		bgColor = new Color(0,0,0);
		borderSize = 0;
		style = SquareStyle.noBorder;
	}
	
	public GraphicSquare(int h, int w, Color c, int d)
	{
		bgColor = c;
		squareHeight = h;
		squareWidth = w;
		borderSize = d;
		style = SquareStyle.noBorder;
	}
	
	public GraphicSquare(int x, int y, int h, int w, Color c, Color b, int d)
	{
		locX = x;
		locY = y;
		bgColor = c;
		squareHeight = h;
		squareWidth = w;
		borderSize = d;
		bordColor = b;
		style = SquareStyle.noBorder;
	}
	
	public GraphicSquare(int x, int y, int h, int w, Color c, Color b, int d, SquareStyle sqs)
	{
		locX = x;
		locY = y;
		bgColor = c;
		squareHeight = h;
		squareWidth = w;
		borderSize = d;
		bordColor = b;
		style = sqs;
		
	}
	
	public GraphicSquare(GraphicSquare s)//Copy ctor
	{
		this.bgColor = s.bgColor;//it references the same color (for now)
		this.bordColor = s.bordColor;
		this.borderSize = s.borderSize;
		this.squareHeight = s.squareHeight;
		this.squareWidth = s.squareWidth;
		this.style = s.style;
	}
	
	public void setBGColor(Color col)
	{
		bgColor = col;
	}
	public Color getBGColor()
	{
		return bgColor;
	}
	
	public int getDeepness()
	{
		return borderSize;
	}
	public void setDeepness(int d)
	{
		borderSize = d;
	}
	public void paint(Graphics g)
	{
		switch(style)
		{
		case noBorder:
		{
			g.setColor(bgColor);
			g.fillRect(locX,locY,squareWidth,squareHeight);
			break;
		}
		case SimpleBorder:
		{
			g.setColor(bgColor);
			g.fillRect(locX,locY,squareWidth,squareHeight);
			g.setColor(bordColor.darker().darker());
			g.drawRect(locX, locY, squareWidth, squareHeight);
			break;
		}
		case Border3d:
		{
			g.setColor (bgColor);
	        g.fillRect(locX + borderSize - 1, locY + borderSize - 1, 
	        		squareWidth - borderSize, squareHeight - borderSize);
	        Color bord = new Color(bordColor.getRGB());
	        Color bord2 = new Color(bordColor.getRGB());
	        for (int i = 1; i <= borderSize; ++i)
	        {
	        	for (int j = 1; j <= i; ++j)
	        	{
	        		bord = brighter(bord,10);
	        	}
	        	g.setColor(bord);
	        	g.drawLine(locX + i - 1, locY + squareHeight - i, locX + i - 1, locY + i - 1);
	        	g.drawLine(locX + i - 1, locY + i - 1, locX + squareWidth - i, locY + i - 1);
	        	for (int j = 1; j <= i; ++j)
	        	{
	        		bord2 = darker(bord2,10);
	        	}
	        	g.setColor(bord2);
	        	g.drawLine(locX + i, locY + squareHeight - i, 
	        			locX + squareWidth - i, locY + squareHeight - i);
	        	g.drawLine(locX + squareWidth - i, locY + squareHeight - i,
	        			locX + squareWidth - i, locY + i);
	        }
	        
	        break;
		}
		case Cartoon:
		{
			g.setColor(bgColor);
			g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			break;
		}
		case RoundSquareBorder:
		{
			g.setColor(bgColor);
			g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			g.setColor(bordColor.darker().darker());
			g.drawRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			break;
		}
		case Circle:
		{
			g.setColor(bgColor);
			//g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			g.fillOval(locX, locY, squareWidth, squareHeight);
			break;
		}
		case CircleBorder:
		{
			g.setColor(bgColor);
			//g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			g.fillOval(locX, locY, squareWidth, squareHeight);
			g.setColor(bordColor.darker().darker());
			g.drawOval(locX, locY, squareWidth, squareHeight);
			break;
		}
		case Octagon:
		{
			g.setColor(bgColor);
			int margin = (int)((4 + Math.sqrt(16+8*squareHeight*squareHeight))/4);
			int xp[] = { locX + margin, locX + squareHeight -margin, locX, locX,
						locX + squareHeight -margin, locX +margin, locX + squareWidth, locX + squareWidth};
			int yp[] = { locY + squareHeight, locY + squareHeight, locY + margin, locY + squareHeight -margin,
						locY, locY, locY + squareHeight - margin, locY +margin}; 
			g.fillPolygon(xp, yp, 8);
			break;
		}
		case Medieval:
		{
			g.setColor(bgColor);
			int margin = (int)((4 + Math.sqrt(16+8*squareHeight*squareHeight))/4);
			int xp[] = { locX + margin, locX + squareHeight -margin, locX + squareHeight, locX+squareHeight,
						locX + squareHeight -margin, locX +margin, locX, locX};
			int yp[] = { locY, locY, locY +  margin, locY + squareHeight -margin,
						locY + squareHeight, locY + squareHeight, locY + squareHeight - margin, locY +margin}; 
			g.fillPolygon(xp, yp, 8);
			break;
		}
		case YouNeedGlasses:
		{
			/*GraphicsUtils.fillGradientRect(g, bgColor, new Color(0,0,0,0), locX -squareWidth/6, locY-squareHeight/6, 
					squareWidth+squareHeight/3, squareHeight+squareHeight/3, GradientMode.CENTER);*/
			GraphicsUtils.fillGradientRect(g, bgColor, GraphicsUtils.setColorAlpha(bgColor, 50), locX -squareWidth/6, locY-squareHeight/6, 
					squareWidth+squareHeight/3, squareHeight+squareHeight/3, GradientMode.CENTER);
			
			break;
		}
		case CityLights:
		{
			GraphicsUtils.fillGradientRect(g, bgColor, new Color(0,0,0,0), locX -squareWidth/6, locY-squareHeight/6, 
					squareWidth+squareHeight/3, squareHeight+squareHeight/3, GradientMode.CENTER);
			
			
			break;
		}
		case Glitchy:
		{
			GraphicsUtils.fillBlurredGlitchRect(g, bgColor, locX, locY, squareWidth, squareHeight, borderSize, borderSize);
			g.setColor(bgColor);
			g.fillRect(locX,locY,squareWidth,squareHeight);
			break;
		}
		case BlurredOutBorder:
		{
			g.setColor(bgColor);
			GraphicsUtils.fillBlurredBorderRect(g, bgColor, locX, locY, squareWidth, squareHeight, borderSize, borderSize);
			break;
		}
		case BlurredInBorder:
		{
			g.setColor(bgColor);
			GraphicsUtils.fillBlurredBorderRect(g, bgColor, locX+borderSize, locY+borderSize, squareWidth-borderSize, squareHeight-borderSize, borderSize, borderSize);
			break;
		}
		case SquareMadness:
		{
			g.setColor(bgColor);
			g.fillRect(locX, locY, squareWidth/3, squareHeight/3);
			g.fillRect(locX+squareWidth/3, locY, squareWidth/3, squareHeight/3);
			g.fillRect(locX+squareWidth*2/3, locY, squareWidth/3, squareHeight/3);
			g.fillRect(locX, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.fillRect(locX+squareWidth/3, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.fillRect(locX+squareWidth*2/3, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.fillRect(locX, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			g.fillRect(locX+squareWidth/3, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			g.fillRect(locX+squareWidth*2/3, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			g.setColor(bordColor.darker().darker());
			g.drawRect(locX, locY, squareWidth/3, squareHeight/3);
			g.drawRect(locX+squareWidth/3, locY, squareWidth/3, squareHeight/3);
			g.drawRect(locX+squareWidth*2/3, locY, squareWidth/3, squareHeight/3);
			g.drawRect(locX, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.drawRect(locX+squareWidth/3, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.drawRect(locX+squareWidth*2/3, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.drawRect(locX, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			g.drawRect(locX+squareWidth/3, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			g.drawRect(locX+squareWidth*2/3, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			
			break;
		}
		case Granular:
		{
			g.setColor(bgColor);
			g.fillOval(locX, locY, squareWidth/3, squareHeight/3);
			g.fillOval(locX+squareWidth/3, locY, squareWidth/3, squareHeight/3);
			g.fillOval(locX+squareWidth*2/3, locY, squareWidth/3, squareHeight/3);
			g.fillOval(locX, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.fillOval(locX+squareWidth/3, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.fillOval(locX+squareWidth*2/3, locY+squareHeight/3, squareWidth/3, squareHeight/3);
			g.fillOval(locX, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			g.fillOval(locX+squareWidth/3, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			g.fillOval(locX+squareWidth*2/3, locY+squareHeight*2/3, squareWidth/3, squareHeight/3);
			
			break;
		}
		case VerticalGradient:
		{
			GraphicsUtils.fillGradientRect(g, bgColor, bgColor.darker().darker(), locX, locY, squareWidth, squareHeight, GradientMode.VERTICAL);			
			break;
		}
		case HorizontalGradient:
		{
			GraphicsUtils.fillGradientRect(g, bgColor, bgColor.darker().darker(), locX, locY, squareWidth, squareHeight, GradientMode.HORIZONTAL);			
			break;
		}
		case DownLeftGradient:
		{
			GraphicsUtils.fillGradientRect(g, bgColor, bgColor.darker().darker(), locX, locY, squareWidth, squareHeight, GradientMode.DOWNLEFT);
			break;
		}
		case DownRightGradient:
		{
			GraphicsUtils.fillGradientRect(g, bgColor, bgColor.darker().darker(), locX, locY, squareWidth, squareHeight, GradientMode.DOWNRIGHT);
			break;
		}
		case CenterGradient:
		{
			//g.setXORMode(Color.white);
			GraphicsUtils.fillGradientRect(g, bgColor, bgColor.darker().darker(), locX, locY, squareWidth, squareHeight, GradientMode.CENTER);
			//g.setPaintMode();
			break;
		}
		case Rainbow:
		{
			g.setColor(GraphicsUtils.setColorAlpha(rainbowFast.getStaticColor(), bgColor.getAlpha()));
			g.fillRect(locX,locY,squareWidth,squareHeight);
			break;
		}
		case Rainbow2:
		{
			GraphicsUtils.fillGradientRect(g, GraphicsUtils.setColorAlpha(rainbowFast.getStaticColor(), bgColor.getAlpha()),
					GraphicsUtils.setColorAlpha(rainbowFast2.getStaticColor(), bgColor.getAlpha()), locX, locY, 
					squareWidth, squareHeight, GradientMode.HORIZONTAL);
			break;
		}
		case ColorMadness:
		{
			g.setColor(GraphicsUtils.setColorAlpha(rainbowVeryFast.getStaticColor(), bgColor.getAlpha()));
			g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			g.setColor(bordColor.darker().darker());
			g.drawRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			break;
		}
		case LucyInTheSky:
		{
			g.setXORMode(rainbowSlow.getStaticColor());
			GraphicsUtils.fillGradientRect(g, bgColor, bgColor.darker().darker(), locX, locY, squareWidth, squareHeight, GradientMode.CENTER);
			g.setPaintMode();
			break;
		}
		case GhostSquare:
		{
			GraphicsUtils.fillGradientRect(g, bgColor, new Color(0,0,0,0), locX, locY, squareWidth, squareHeight, GradientMode.DOWNLEFT);			
			break;
		}
		case Rotatuille:
		{
			
			/*int delta = rainbowFast.getIncreasingValue();
			
			int[] x ={locX+squareWidth-delta, locX+squareWidth, locX+delta, locX };
			int[] y ={locY, locY-delta, locY+squareHeight, locY+delta };
			g.fillPolygon(x, y, 4);*/ //<<<--- TODO: Fireworks!?
			g.setColor(bgColor);
			int delta = (rainbowFast.getIncreasingValue()*squareWidth)/(255-rainbowFast.getSpeed());			
			int[] x ={locX+squareWidth-delta, locX+squareWidth, locX+delta, locX };
			int[] y ={locY, locY+squareHeight-delta, locY+squareHeight, locY+delta };
			g.fillPolygon(x, y, 4);
			break;
			
		}
		case Smiley:
		{
			g.setColor(bgColor);
			g.fillOval(locX, locY, squareWidth, squareHeight);
			g.setColor(Color.black);
			g.fillOval(locX+squareWidth/4, locY+squareHeight/4,squareWidth/8,squareHeight/8); //left eye
			g.fillOval(locX+squareWidth*5/8, locY+squareHeight/4,squareWidth/8,squareHeight/8); //right eye
			g.drawArc(locX+squareWidth/4, locY+squareHeight/4, squareWidth/2, squareHeight/2, 225, 90); //mouth
			break;
		
		}
		default:
		{
			g.setColor(bgColor);
			g.fillRect(locX,locY,squareWidth,squareHeight);
			break;
		}
		
			
		}
			
	}

	public Color getBorderColor() {
		return bordColor;
	}

	public void setBorderColor(Color bordcolor) {
		this.bordColor = bordcolor;
	}
	
	private Color brighter(Color c,int s)
	{
		int red = Math.min(c.getRed() + s, 255);
		int green = Math.min(c.getGreen() + s, 255);
		int blue = Math.min(c.getBlue() + s, 255);
		
		return new Color(red, green, blue, c.getAlpha());
	}
	
	private Color darker(Color c, int s)
	{
		int red = Math.max(c.getRed() - s, 0);
		int green = Math.max(c.getGreen() - s, 0);
		int blue = Math.max(c.getBlue() - s, 0);
		
		return new Color(red, green, blue, c.getAlpha());
	}
	
	
}
