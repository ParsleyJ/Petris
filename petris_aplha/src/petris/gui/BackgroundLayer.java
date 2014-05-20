package petris.gui;

import java.awt.Color;
import java.awt.Dimension;

import petris.Action;
import petris.Painter;



public class BackgroundLayer extends Layer {
	
	PetrisColor color;
	Dimension size;
	
	Painter customPaint;
	
	


	public BackgroundLayer(PetrisColor col, Dimension s)
	{
		color = col;
		size = s;
	}
	
	public void setColor(PetrisColor col)
	{
		color = col;
	}
	
	public PetrisColor getPetrisColor() {
		return color;
	}
	
	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public void paint()
	{
		if (customPaint != null)
		{
			customPaint.paint(graphics);
		}
		else
		{
			graphics.setColor(color.getColor());
			graphics.fillRect(0,0,(int)size.getWidth(),(int)size.getHeight());
		}
	}

	public Painter getCustomPaint() {
		return customPaint;
	}

	public void setCustomPaint(Painter customPaint) {
		this.customPaint = customPaint;
	}
	
	

	
	
	
}
