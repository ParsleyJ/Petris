package petris.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;
import petris.Game.Action;

public class PetrisOptionMenuEntry extends PetrisMenuEntry {

	private boolean okForNext = true;
	private boolean cycle =  true;
	protected boolean drawTriangles = true;
	private ArrayList<String> options = new ArrayList<String>();
	private int selectedOption = 0;
	protected String selectedText = "";
	protected int triangleHeight = 16;
	protected int triangleWidth = 20;
	protected int triangleBorderDistance = 15;
	private Action onLeft;
	private Action onRight;
	private Action onOk;
	
	public PetrisOptionMenuEntry(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
	}
	
	public void setOptions(ArrayList<String> array)
	{
		options = array;
	}
	
	public void addOption(String s)
	{
		options.add(s);
		if (options.size() == 1) 
		{
			selectedOption = 0;
			selectedText = getSelected();
		}
	}
	
	public void addOption(String s, int index)
	{
		options.add(index, s);
	}
	
	public void removeOption(int index)
	{
		options.remove(index);
	}
	
	public void selectNext()
	{
		if (options.isEmpty()) return;
		if (selectedOption + 1 > options.size() -1) 
		{
			if (cycle) 
			{
				selectedOption = 0;
				selectedText = getSelected();
			}
			else return;
		}
		else
		{
			++selectedOption;
			selectedText = getSelected();
		}
		
	}
	
	public void selectPrevious()
	{
		if (options.isEmpty()) return;
		if (selectedOption - 1 < 0) 
		{
			if (cycle)
			{
				selectedOption = options.size() - 1;
				selectedText = getSelected();
			}
			else return;
		}
		else
		{
			--selectedOption;
			selectedText = getSelected();
		}
		
	}
	
	public String getSelected()
	{
		if (options.isEmpty()) return "";
		return options.get(selectedOption);
	}
	
	public void setOptionText(String text)
	{
		selectedText = text;
	}
	
	public void performOk()
	{
		super.performOk();
		runOnOk();
		if (okForNext)
		{
			selectNext();
		}
	}
	
	private void runOnOk() {
		if (onOk != null)onOk.run();		
	}

	public void performRight()
	{
		super.performRight();
		runOnRight();
		selectNext();
	}
	
	private void runOnRight() {
		if (onRight != null)onRight.run();		
	}

	public void performLeft()
	{
		super.performLeft();
		runOnLeft();
		selectPrevious();
	}
	
	private void runOnLeft() {
		if (onLeft != null)onLeft.run();				
	}

	public void paint(Graphics graphics, int y)
	{
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
			
			Point titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w, h -5 -h2));
			graphics.setFont(textFont);
			graphics.drawString(text, titleCoords.x, titleCoords.y);
			
			Point optionCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w2, h2));
			graphics.setFont(textFont.deriveFont(textFont.getSize() - 6F));
			graphics.drawString(selectedText, optionCoords.x, titleCoords.y + h + 5);
			
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

	public boolean isDrawTrianglesEnabled() {
		return drawTriangles;
	}

	public void setDrawTriangles(boolean drawTriangles) {
		this.drawTriangles = drawTriangles;
	}

	public int getTriangleHeight() {
		return triangleHeight;
	}

	public void setTriangleHeight(int triangleHeight) {
		this.triangleHeight = triangleHeight;
	}

	public int getTriangleWidth() {
		return triangleWidth;
	}

	public void setTriangleWidth(int triangleWidth) {
		this.triangleWidth = triangleWidth;
	}

	public int getTriangleBorderDistance() {
		return triangleBorderDistance;
	}

	public void setTriangleBorderDistance(int triangleBorderDistance) {
		this.triangleBorderDistance = triangleBorderDistance;
	}

	public Action getOnLeft() {
		return onLeft;
	}

	public void setOnLeft(Action onLeft) {
		this.onLeft = onLeft;
	}

	public Action getOnRight() {
		return onRight;
	}

	public void setOnRight(Action onRight) {
		this.onRight = onRight;
	}

	public Action getOnOk() {
		return onOk;
	}

	public void setOnOk(Action onOk) {
		this.onOk = onOk;
	}
	
	
	
	
	

}
