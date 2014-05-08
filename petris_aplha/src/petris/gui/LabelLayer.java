package petris.gui;

import java.awt.Color;

import java.awt.Font;

public class LabelLayer extends Layer {

	
	private int coordX =0;
	private int coordY =0;
	private Color layerColor = new Color(50,50,50,100);
	private Font font;
	public String value;
	
	
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
		layerColor = new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
	}
	
	public void  paint()
	{
		graphics.setColor(layerColor);
		graphics.setFont(font);
		graphics.drawString(value, coordX, coordY);
		
	}


}
