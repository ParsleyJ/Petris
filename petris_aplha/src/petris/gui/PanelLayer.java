package petris.gui;

import java.awt.Color;
import java.awt.Dimension;

public class PanelLayer extends Layer {

	private Dimension layerSize;
	private int coordX =0;
	private int coordY =0;
	private FadingColor layerColor = new FadingColor(new Color(50,50,50,0), 100);
	private int angleSquare = 10;
	
	
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
