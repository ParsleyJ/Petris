package petris.gui;

import java.awt.Dimension;

public class BackgroundLayer extends Layer {
	
	PetrisColor color;
	Dimension size;
	
	

	public BackgroundLayer(PetrisColor col, Dimension s)
	{
		color = col;
		size = s;
	}
	
	public void setColor(PetrisColor col)
	{
		color = col;
	}
	
	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public void paint()
	{
		graphics.setColor(color.getColor());
		graphics.fillRect(0,0,(int)size.getWidth(),(int)size.getHeight());
	}
	
	
	
}
