package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;
import petris.Action;

public class PetrisMenuEntry {
	protected String text;
	protected int height = 40;
	private boolean isFocused = false;
	protected FadingColor bgColor = new FadingColor(new Color(50,50,50,230), 230);
	protected FadingColor textColor = new FadingColor(new Color(50,200,50,230), 230);
	protected FadingColor focusColor = new FadingColor(textColor.getStaticColor().darker().darker(), 230);
	protected FadingColor disabledColor = new FadingColor(new Color(100,100,100,230), 230);
	protected int width;
	protected Font textFont;
	protected boolean isVisible;
	protected boolean hasAction = false;
	protected Action action;
	protected PetrisMenu root;
	private boolean enabled = true;
	private final int fadeTime = 300;
	protected String style = "Blurred";
	protected int borderSize = 5;
	
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
	
	public PetrisMenuEntry(String entryText, Font font, int parentWidth, int entryHeight, FadingColor background, FadingColor foreColor, boolean isEnabled)
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
		setEnabled(isEnabled);
	}
	
	public PetrisMenuEntry(PetrisMenuEntry x)
	{
		text = x.getText();
		textFont = x.getTextFont();
		height = x.height;
		width = x.width;
		bgColor = new FadingColor(x.bgColor.getStaticColor());
		textColor = new FadingColor(x.textColor.getStaticColor());
		focusColor = new FadingColor(textColor.getStaticColor().darker().darker().darker());
		action = x.action;
		hasAction = x.hasAction;
		setFocused(false);
		focusColor.setAlpha(0);
		bgColor.setAlpha(230);
		setEnabled(x.enabled);
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
			focusColor.fadeIn(-1, 200);
			bgColor.fadeOut();
		}
		else
		{
			focusColor.fadeOut();
			bgColor.fadeIn(-1, 200);
		}
	}
	
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
	
	public void performTextInput(KeyEvent keyCode)
	{
		
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
		bgColor.fadeIn(-1, fadeTime);
		textColor.fadeIn(-1, fadeTime);
		disabledColor.fadeIn(-1, fadeTime);

	}

	public void close() {
		isVisible = false;
		bgColor.fadeOut();
		textColor.fadeOut();
		focusColor.fadeOut();
		disabledColor.fadeOut();
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
