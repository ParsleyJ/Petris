package petris.gui;

import java.awt.Color;
import java.awt.Dimension;

import petris.Action;
import petris.PaintAction;



public class BackgroundLayer extends Layer {
	
	PetrisColor color;
	Dimension size;
	
	PaintAction customPaint;
	
	


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
			customPaint.runPaint(graphics);
		}
		else
		{
			graphics.setColor(color.getColor());
			graphics.fillRect(0,0,(int)size.getWidth(),(int)size.getHeight());
		}
	}

	public PaintAction getCustomPaint() {
		return customPaint;
	}

	public void setCustomPaint(PaintAction customPaint) {
		this.customPaint = customPaint;
	}
	
	

	
	
	
}
