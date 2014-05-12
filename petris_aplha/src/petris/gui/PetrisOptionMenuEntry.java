package petris.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import parsleyj.utils.GuiUtils;

public class PetrisOptionMenuEntry extends PetrisMenuEntry {

	private boolean okForNext = true;
	private boolean cycle =  true;
	private ArrayList<String> options = new ArrayList<String>();
	private int selectedOption = 0;
	private String selectedText = "";
	
	public PetrisOptionMenuEntry(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		// TODO Auto-generated constructor stub
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
	
	public void performOk()
	{
		super.performOk();
		if (okForNext)
		{
			selectNext();
		}
	}
	
	public void performRight()
	{
		super.performRight();
		selectNext();
	}
	
	public void performLeft()
	{
		super.performLeft();
		selectPrevious();
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
			graphics.setFont(textFont.deriveFont(textFont.getSize() - 4F));
			int w2 = (int) graphics.getFontMetrics().getStringBounds(selectedText,graphics).getWidth();
			int h2 = (int) graphics.getFontMetrics().getStringBounds(selectedText,graphics).getHeight();
			
			Point titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w, h - 5 -h2));
			graphics.setFont(textFont);
			graphics.drawString(text, titleCoords.x, titleCoords.y);
			
			Point optionCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w2, h2));
			graphics.setFont(textFont.deriveFont(textFont.getSize() - 6F));
			graphics.drawString(selectedText, optionCoords.x, titleCoords.y + h + 5);
			
			
	}
	

}
