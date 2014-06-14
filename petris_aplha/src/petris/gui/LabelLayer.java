package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

public class LabelLayer extends Layer {

	
	private int coordX =0;
	private int coordY =0;
	private FadingColor layerColor = new FadingColor(new Color(50,50,50,0),230);
	private Font font;
	public String value;
	
	public enum DockSide{Left, Right}	
	private Point originalLocation;
	private Dimension originalParentSize;
	private DockSide side;
	
	public void setOriginalPosition(Point loc, Dimension siz, DockSide side)
	{
		originalLocation = loc;
		originalParentSize = siz;
		this.side = side;
	}
	
	public void updatePosition(Dimension newParentSize)
	{
		if (originalLocation == null || originalParentSize == null) return;
		switch(side)
		{
		case Left:
		{
			
		}
		case Right:
		{
			int dist=originalParentSize.width-originalLocation.x;
			coordX = newParentSize.width - dist;
		}
		}
	}
	
	
	
	
	public LabelLayer(String s, Font f) 
	{
		super();
		font = f;
		value = s;
	}
	public LabelLayer(String s, Font f, int x, int y) 
	{
		super();
		font = f;
		coordX = x;
		coordY = y;
		value = s;
	}
	public LabelLayer(String s, Font f, int x, int y, Color c, int alpha) 
	{
		super();
		font = f;
		coordX = x;
		coordY = y;
		value = s;
		layerColor = new FadingColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),0), alpha);
	}
	
	public void  paint()
	{
		graphics.setColor(layerColor.getStaticColor());
		graphics.setFont(font);
		graphics.drawString(value, coordX, coordY);
		
	}
	
	
	
	public void fadeIn(int maxAlphaTime, int fadeTime)
	{
		layerColor.fadeIn(maxAlphaTime, fadeTime);
	}

	public void fadeOut()
	{
		layerColor.fadeOut();
	}
	
	public void setOpacity(int alpha)
	{
		layerColor.setAlpha(alpha);
	}

}
