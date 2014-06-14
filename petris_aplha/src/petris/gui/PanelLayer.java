package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import petris.gui.LabelLayer.DockSide;

public class PanelLayer extends Layer {

	private Dimension layerSize;
	private int coordX =0;
	private int coordY =0;
	private FadingColor layerColor = new FadingColor(new Color(50,50,50,0), 100);
	private int angleSquare = 10;
	
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
	
	
	public PanelLayer(Dimension l) 
	{
		super();
		layerSize = l;
	}
	public PanelLayer(Dimension l, int x, int y) 
	{
		super();
		layerSize = l;
		coordX = x;
		coordY = y;
	}
	public PanelLayer(Dimension l, int x, int y, Color c, int alpha) 
	{
		super();
		layerSize = l;
		coordX = x;
		coordY = y;
		layerColor = new FadingColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),0), alpha);
	}
	
	public void  paint()
	{
		graphics.setColor(layerColor.getStaticColor());
		graphics.fillRoundRect(coordX, coordY, (int)layerSize.getWidth(), (int)layerSize.getHeight(), angleSquare, angleSquare);
		
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
