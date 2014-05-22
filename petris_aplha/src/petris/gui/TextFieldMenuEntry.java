package petris.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;

public class TextFieldMenuEntry extends PetrisMenuEntry {

	private String prompText = "";
	private String fieldText = "";
	//private int cursorPosition = 0;
	private int borderSpace = 30;
	
	public TextFieldMenuEntry(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		
	}
	
	public TextFieldMenuEntry(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor, String prompt) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		setPrompText(prompt);
	}

	public String getPrompText() {
		return prompText;
	}

	public void setPrompText(String prompText) {
		this.prompText = prompText;
	}

	public String getFieldText() {
		return fieldText;
	}

	public void setFieldText(String fieldText) {
		this.fieldText = fieldText;
	}

	public int getBorderSpace() {
		return borderSpace;
	}

	public void setBorderSpace(int borderSpace) {
		this.borderSpace = borderSpace;
	}

	public void input(String s)
	{
		fieldText+=s;
	}
	
	public void input(char c)
	{
		fieldText+=c;
	}
	
	public boolean backspace()
	{
		if (fieldText.isEmpty()) return false;
		fieldText = fieldText.substring(0, fieldText.length()-1);
		return true;
	}
	public boolean canc()
	{
		if (fieldText.isEmpty()) return false;
		fieldText = fieldText.substring(1, fieldText.length());
		return true;
	}
	/*
	@Override
	public void performLeft()
	{
		if (fieldText.isEmpty()) 
		{
			cursorPosition = 0;
			return;
		}
		if (cursorPosition == 0) return;
		--cursorPosition;
	}
	
	@Override
	public void performRight()
	{
		if (fieldText.isEmpty()) 
		{
			cursorPosition = 0;
			return;
		}
		if (cursorPosition == fieldText.length()-1) return;
		++cursorPosition;
	}
	*/
	@Override
	public void paint(Graphics graphics, int y)
	{
		if (!isVisible && textColor.getAlpha()==0 && bgColor.getAlpha()==0 && focusColor.getAlpha()==0) return;
		graphics.setColor(bgColor.getStaticColor());		
		if(style == "Blurred")GraphicsUtils.fillBlurredBorderRect(graphics, bgColor.getStaticColor(), 0, y+borderSize, width, height-borderSize*2, borderSize, borderSize);
		else graphics.fillRect(0,y,width ,height);
		graphics.setColor(focusColor.getStaticColor());
		if(style == "Blurred")GraphicsUtils.fillBlurredBorderRect(graphics, focusColor.getStaticColor(), 0, y+borderSize, width, height-borderSize*2, borderSize, borderSize);
		else graphics.fillRect(0,y,width ,height);
		if (isEnabled()) graphics.setColor(textColor.getStaticColor());
		else graphics.setColor(disabledColor.getStaticColor());		
		graphics.setFont(textFont);
		int h = (int) graphics.getFontMetrics().getStringBounds(fieldText,graphics).getHeight();
		
		
		
		Point titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(width-borderSpace*2,h));
		graphics.drawString(text, titleCoords.x, titleCoords.y);
	}
	
}
