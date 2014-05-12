package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import parsleyj.utils.GuiUtils;
import petris.Game.Action;

public class PetrisMenuEntry {
	private String text;
	private int height = 40;
	private boolean isFocused = false;
	private FadingColor bgColor = new FadingColor(new Color(50,50,50,230), 230);
	private FadingColor textColor = new FadingColor(new Color(50,200,50,230), 230);
	private FadingColor focusColor = new FadingColor(new Color(50,50,50,0).brighter().brighter());
	private int width;
	private Font textFont;
	private boolean isVisible;
	private boolean hasAction = false;
	private Action action;
	protected PetrisMenu root;
	
	public PetrisMenuEntry(String entryText, Font font, int parentWidth)
	{
		textFont = font;
		text = entryText;
		width = parentWidth;
	}
	
	public PetrisMenuEntry(String entryText, Font font, int parentWidth, int entryHeight)
	{
		textFont = font;
		text = entryText;
		height = entryHeight;
		width = parentWidth;
	}
	
	public PetrisMenuEntry(String entryText, Font font, int parentWidth, int entryHeight, FadingColor background, FadingColor foreColor)
	{
		text = entryText;
		textFont = font;
		height = entryHeight;
		bgColor = background;
		textColor = foreColor;
		//focusColor = new FadingColor(background.getStaticColor().brighter().brighter());
		focusColor = new FadingColor(foreColor.getStaticColor().darker().darker().darker());
		focusColor.setAlpha(0);
		width = parentWidth;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public Font getTextFont() {
		return textFont;
	}

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void setFocused(boolean isFocused) {
		this.isFocused = isFocused;
		if (isFocused)
		{
			focusColor.fadeIn(-1, 500);
			bgColor.fadeOut();
		}
		else
		{
			focusColor.fadeOut();
			bgColor.fadeIn(-1, 500);
		}
	}
	
	public void paint(Graphics graphics, int y)
	{
			graphics.setColor(bgColor.getStaticColor());
			graphics.fillRect(0,y,width ,height);
			graphics.setColor(focusColor.getStaticColor());
			graphics.fillRect(0,y,width ,height);
			graphics.setColor(textColor.getStaticColor());
			graphics.setFont(textFont);
			int w = (int) graphics.getFontMetrics().getStringBounds(text,graphics).getWidth();
			int h = (int) graphics.getFontMetrics().getStringBounds(text,graphics).getHeight();
			
			Point titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w,h));
			graphics.drawString(text, titleCoords.x, titleCoords.y);
	}
	
	public void performLeft() {
		
	}
	
	public void performRight() {
		
	}
	
	public void performOk() {
		if(!hasAction) return;
		action.run();
	}
	
	public void performBack() {
		
	}

	public FadingColor getFocusColor() {
		return focusColor;
	}

	public void setFocusColor(FadingColor focusColor) {
		this.focusColor = focusColor;
	}

	public FadingColor getBackgroundColor() {
		return bgColor;
	}

	public void setBackgroundColor(FadingColor bgColor) {
		this.bgColor = bgColor;
	}
	
	
	public FadingColor getTextColor() {
		return textColor;
	}

	public void setTextColor(FadingColor textColor) {
		this.textColor = textColor;
	}

	public void show() {
		isVisible = true;
		bgColor.fadeIn(-1, 500);
		textColor.fadeIn(-1, 500);

	}

	public void close() {
		isVisible = false;
		bgColor.fadeOut();
		textColor.fadeOut();
		focusColor.fadeOut();
	}
	
	public boolean isVisible()
	{
		return isVisible;
	}
	
	public void setAction(Action a)
	{
		hasAction  = true;
		action = a;
	}

	public PetrisMenu getRootMenu() {
		return root;
	}

	public void setRootMenu(PetrisMenu root) {
		this.root = root;
	}
	
	
}
