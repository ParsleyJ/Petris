package petris.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;

public class PetrisGridOptionMenuEntry extends PetrisOptionMenuEntry {

	private PieceLayer curLayer;
	private int layerHeight;
	private int layerWidth;
	
	public PetrisGridOptionMenuEntry(String entryText, Font font,
			int parentWidth, int entryHeight, FadingColor background,
			FadingColor foreColor, PieceLayer layer, int layerWidth, int layerHeight) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		this.curLayer = layer;
		this.layerHeight = layerHeight;
		this.layerWidth = layerWidth;
	}

	public PieceLayer getLayer() {
		return curLayer;
	}

	public void setLayer(PieceLayer layer) {
		this.curLayer = layer;
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
		
		Point titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w, h -5 -h2 -5 -layerHeight));
		titleCoords.setLocation(titleCoords.x, titleCoords.y - h);
		graphics.setFont(textFont);
		graphics.drawString(text, titleCoords.x, titleCoords.y);
		//graphics.drawRect(titleCoords.x, titleCoords.y-h, w, h);
		
		Point layerCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(layerWidth, layerHeight));
		curLayer.setOffsets(layerCoords.x, titleCoords.y + h + 5);
		curLayer.setGraphics(graphics);
		curLayer.paint();
		//graphics.drawRect(layerCoords.x, titleCoords.y + h + 5, layerWidth, layerHeight);
		
		if (isEnabled()) graphics.setColor(textColor.getStaticColor());
		else graphics.setColor(disabledColor.getStaticColor());	
		
		Point optionCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w2, h2));
		graphics.setFont(textFont.deriveFont(textFont.getSize() - 6F));
		graphics.drawString(selectedText, optionCoords.x, titleCoords.y + h + 5 + layerHeight + 5 + h2);
		//graphics.drawRect(optionCoords.x,titleCoords.y + h + 5 + layerHeight + 5, w2, h2);
	
		
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
