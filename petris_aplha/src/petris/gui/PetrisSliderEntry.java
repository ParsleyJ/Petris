package petris.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;
import parsleyj.utils.GraphicsUtils.GradientMode;

public class PetrisSliderEntry extends PetrisOptionMenuEntry {

	private int maxValue = 255;
	private int minValue = 0;
	private int value = 0;
	private int step = 16;
	
	private int barHeight = 10;
	private int horizDistance = 50; //horizontal distance from parent container borders (best if more than 35)
	
	public PetrisSliderEntry(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		super.setOptionText(""+value);
	}
	
	public PetrisSliderEntry(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor, int min, int max, int step) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		minValue = min;
		maxValue = max;
		this.step = step;
		super.setOptionText(""+value);
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		super.setOptionText(""+value);
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
	
	public void increase()
	{
		value = Math.min(maxValue, value + step);
		super.setOptionText(""+value);
	}
	
	public void decrease()
	{
		value = Math.max(minValue, value - step);
		super.setOptionText(""+value);
	}
	
	public void performRight()
	{
		increase();
		runOnRight();
	}
	
	public void performLeft()
	{
		decrease();
		runOnLeft();
	}
	
	public void performOk()
	{
		runOnOk();
	}
	
	public void paint(Graphics graphics, int y)
	{
		width = root.parentWidth;
		graphics.setColor(bgColor.getStaticColor());
		if(style == "Blurred")GraphicsUtils.fillBlurredBorderRect(graphics, bgColor.getStaticColor(), 0, y+borderSize, width, height-borderSize*2, borderSize, borderSize);
		else graphics.fillRect(0,y,width ,height);
		graphics.setColor(focusColor.getStaticColor());
		if(style == "Blurred")GraphicsUtils.fillBlurredBorderRect(graphics, focusColor.getStaticColor(), 0, y+borderSize, width, height-borderSize*2, borderSize, borderSize);
		else graphics.fillRect(0,y,width ,height);
		if (isEnabled()) graphics.setColor(textColor.getStaticColor());
		else graphics.setColor(disabledColor.getStaticColor());		
		graphics.setFont(textFont);
		int w = (int) graphics.getFontMetrics().getStringBounds(text,graphics).getWidth();
		int h = (int) graphics.getFontMetrics().getStringBounds(text,graphics).getHeight();
		graphics.setFont(textFont.deriveFont(textFont.getSize() - 4F));
		int w2 = (int) graphics.getFontMetrics().getStringBounds(selectedText,graphics).getWidth();
		int h2 = (int) graphics.getFontMetrics().getStringBounds(selectedText,graphics).getHeight();
		
		if (text == "") h = 0;
		
		Point titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w, h -5 -h2 -5 -barHeight));
		graphics.setFont(textFont);
		graphics.drawString(text, titleCoords.x, titleCoords.y);
		
		Point barCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(width-horizDistance*2,barHeight));
		if (isEnabled()) graphics.setColor(textColor.getStaticColor());
		else graphics.setColor(disabledColor.getStaticColor());		
		graphics.drawRect(barCoords.x,titleCoords.y + 5,width-horizDistance*2,barHeight);
		if(value!=0)
			//graphics.fillRect(barCoords.x,titleCoords.y + 5, (value*(width-horizDistance*2))/(maxValue-minValue),barHeight);
			GraphicsUtils.fillGradientRect(graphics, GraphicsUtils.setColorAlpha(graphics.getColor(), 00), graphics.getColor(), 
					barCoords.x,titleCoords.y + 5, (value*(width-horizDistance*2))/(maxValue-minValue),barHeight, GradientMode.HORIZONTAL);
			
		Point optionCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w2, h2));
		graphics.setFont(textFont.deriveFont(textFont.getSize() - 6F));
		graphics.drawString(selectedText, optionCoords.x, titleCoords.y + 5 + barHeight + 5 +h2);
		
		if (drawTriangles)
		{
			if (isEnabled()) graphics.setColor(textColor.getStaticColor());
			else graphics.setColor(disabledColor.getStaticColor());		
			//draw left triangle
			int lsX = 0 + triangleBorderDistance;
			int lsY = y + (height/2 - triangleHeight/2);
			int[] lX = { lsX, lsX + triangleWidth, lsX + triangleWidth };
			int[] lY = { lsY + triangleHeight/2, lsY, lsY + triangleHeight};
			graphics.fillPolygon(lX, lY, 3);
			
			//draw right triangle
			int rsX = width - (triangleBorderDistance + triangleWidth);
			int rsY = lsY;
			int[] rX = { rsX, rsX + triangleWidth, rsX};
			int[] rY = { rsY, rsY + triangleHeight/2, rsY + triangleHeight};
			graphics.fillPolygon(rX, rY, 3);
		}
	}

}
