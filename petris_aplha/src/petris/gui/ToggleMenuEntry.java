package petris.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;

public class ToggleMenuEntry extends PetrisMenuEntry {
	
	private boolean checked = false;

	public ToggleMenuEntry(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		// TODO Auto-generated constructor stub
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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
		int indicatorSize = h;
		
		Point titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w+indicatorSize,h));
		graphics.drawString(text, titleCoords.x, titleCoords.y);
		
		if (checked)graphics.fillRect(titleCoords.x+w+5, titleCoords.y-h, indicatorSize, indicatorSize);
		else graphics.drawRect(titleCoords.x+w+5, titleCoords.y-h, indicatorSize, indicatorSize);
	}
	
	@Override
	public void performOk()
	{
		super.performOk();
		this.checked = !this.checked;
	}

}
