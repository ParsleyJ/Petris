package petris.gui;

import java.awt.Color;
import java.awt.Dimension;

public class GuiLayer extends Layer {

	private Dimension layerSize;
	private int coordX =0;
	private int coordY =0;
	private Color layerColor = new Color(50,50,50,100);
	private int angleSquare = 10;
	
	
	public GuiLayer(Dimension l) 
	{
		super();
		layerSize = l;
	}
	public GuiLayer(Dimension l, int x, int y) 
	{
		super();
		layerSize = l;
		coordX = x;
		coordY = y;
	}
	public GuiLayer(Dimension l, int x, int y, Color c, int alpha) 
	{
		super();
		layerSize = l;
		coordX = x;
		coordY = y;
		layerColor = new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
	}
	
	public void  paint()
	{
		graphics.setColor(layerColor);
		graphics.fillRoundRect(coordX, coordY, (int)layerSize.getWidth(), (int)layerSize.getHeight(), angleSquare, angleSquare);
		
	}

}
