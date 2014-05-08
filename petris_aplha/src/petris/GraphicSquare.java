package petris;

import java.awt.Color;
import java.awt.Graphics;



public class GraphicSquare extends Item {
	
	private Color bgcolor;
	private Color bordcolor;
	public int deepness;
	public int squareHeight = 30;
	public int squareWidth = 30;
	private SquareStyle style;
	
	
	public enum SquareStyle {
		noBorder, SimpleBorder, Border3d, RoundSquare, RoundSquareBorder, Circle, CircleBorder, Octagon, Medieval,  
	}
	
	public GraphicSquare()//Default ctor
	{
		
		bgcolor = new Color(0,0,0);
		deepness = 0;
		style = SquareStyle.noBorder;
	}
	
	public GraphicSquare(int h, int w, Color c, int d)
	{
		bgcolor = c;
		squareHeight = h;
		squareWidth = w;
		deepness = d;
		style = SquareStyle.noBorder;
	}
	
	public GraphicSquare(int x, int y, int h, int w, Color c, Color b, int d)
	{
		locX = x;
		locY = y;
		bgcolor = c;
		squareHeight = h;
		squareWidth = w;
		deepness = d;
		bordcolor = b;
		style = SquareStyle.noBorder;
	}
	
	public GraphicSquare(int x, int y, int h, int w, Color c, Color b, int d, SquareStyle sqs)
	{
		locX = x;
		locY = y;
		bgcolor = c;
		squareHeight = h;
		squareWidth = w;
		deepness = d;
		bordcolor = b;
		style = sqs;
	}
	
	public GraphicSquare(GraphicSquare s)//Copy ctor
	{
		this.bgcolor = s.bgcolor;//it references the same color (for now)
		this.bordcolor = s.bordcolor;
		this.deepness = s.deepness;
		this.squareHeight = s.squareHeight;
		this.squareWidth = s.squareWidth;
		this.style = s.style;
	}
	
	public void setBGColor(Color col)
	{
		bgcolor = col;
	}
	public Color getBGColor()
	{
		return bgcolor;
	}
	
	public int getDeepness()
	{
		return deepness;
	}
	public void setDeepness(int d)
	{
		deepness = d;
	}
	public void paint(Graphics g)
	{
		switch(style)
		{
		case noBorder:
		{
			g.setColor(bgcolor);
			g.fillRect(locX,locY,squareWidth,squareHeight);
			break;
		}
		case SimpleBorder:
		{
			g.setColor(bgcolor);
			g.fillRect(locX,locY,squareWidth,squareHeight);
			g.setColor(bordcolor.darker().darker());
			g.drawRect(locX, locY, squareWidth, squareHeight);
			break;
		}
		case Border3d:
		{
			g.setColor (bgcolor);
	        g.fillRect(locX + deepness - 1, locY + deepness - 1, 
	        		squareWidth - deepness, squareHeight - deepness);
	        Color bord = new Color(bordcolor.getRGB());
	        Color bord2 = new Color(bordcolor.getRGB());
	        for (int i = 1; i <= deepness; ++i)
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
		case RoundSquare:
		{
			g.setColor(bgcolor);
			g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			break;
		}
		case RoundSquareBorder:
		{
			g.setColor(bgcolor);
			g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			g.setColor(bordcolor.darker().darker());
			g.drawRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			break;
		}
		case Circle:
		{
			g.setColor(bgcolor);
			//g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			g.fillOval(locX, locY, squareWidth, squareHeight);
			break;
		}
		case CircleBorder:
		{
			g.setColor(bgcolor);
			//g.fillRoundRect(locX,locY,squareWidth-1,squareHeight-1,squareWidth/3,squareHeight/3);
			g.fillOval(locX, locY, squareWidth, squareHeight);
			g.setColor(bordcolor.darker().darker());
			g.drawOval(locX, locY, squareWidth, squareHeight);
			break;
		}
		case Octagon:
		{
			g.setColor(bgcolor);
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
			g.setColor(bgcolor);
			int margin = (int)((4 + Math.sqrt(16+8*squareHeight*squareHeight))/4);
			int xp[] = { locX + margin, locX + squareHeight -margin, locX + squareHeight, locX+squareHeight,
						locX + squareHeight -margin, locX +margin, locX, locX};
			int yp[] = { locY, locY, locY +  margin, locY + squareHeight -margin,
						locY + squareHeight, locY + squareHeight, locY + squareHeight - margin, locY +margin}; 
			g.fillPolygon(xp, yp, 8);
			break;
		}
		default:
		{
			g.setColor(bgcolor);
			g.fillRect(locX,locY,squareWidth,squareHeight);
			break;
		}
		
			
		}
			
	}

	public Color getBorderColor() {
		return bordcolor;
	}

	public void setBorderColor(Color bordcolor) {
		this.bordcolor = bordcolor;
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
