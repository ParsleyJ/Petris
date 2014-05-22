package petris.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;

public class ProgressIndicatorMenuEntry extends PetrisMenuEntry {

	public ProgressIndicatorMenuEntry(String entryText, Font font,
			int parentWidth, int entryHeight, FadingColor background,
			FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		// TODO Auto-generated constructor stub
	}
	
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
		int w = (int) graphics.getFontMetrics().getStringBounds(text,graphics).getWidth();
		int h = (int) graphics.getFontMetrics().getStringBounds(text,graphics).getHeight();
		int indicatorSize = h+4;
		
		Point titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w+indicatorSize,h));
		graphics.drawString(text, titleCoords.x, titleCoords.y);
		
		int frame1 = (int)((((double)root.getCurrentFrame()%125.0)/125.0)*360.0);
		int frame2 = (int)((((double)root.getCurrentFrame()%50.0)/50.0)*360.0);
		graphics.fillArc(titleCoords.x+w+5, titleCoords.y-h, indicatorSize, indicatorSize, frame1, frame2);
		
	}

}
