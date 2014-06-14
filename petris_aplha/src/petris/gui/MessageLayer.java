package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import parsleyj.utils.GraphicsUtils;

public class MessageLayer extends Layer {

	private int parentWidth;
	private int parentHeight;
	private int boxHeight;
	private Color bgColor = new Color(50,50,50);
	private Color foreColor;
	private int renderDelay = 20;
	private int showTime;
	private int fadeInTicks;
	private int fadeOutTicks;
	private String message;
	private Font textFont;
	private boolean isStarted = false;
	private int currentAlpha;
	private int alphaMax;
	private int alphaStep;
	private int offset;
	//private PriorityQueue<Msg> messages;
	private boolean permanent;
	private int borderSize = 3;

	public MessageLayer(int pw, int ph, int bh, Font font, int maxalpha) {
		parentWidth = pw;
		parentHeight = ph;
		bgColor = new Color(50,50,50,0);
		textFont = font;
		showTime = 0;
		fadeInTicks = 0;
		fadeOutTicks = 0;
		message = "";
		currentAlpha = 0;
		alphaStep = 0;
		alphaMax = maxalpha;
		boxHeight = bh;
		offset = 0;
		//messages = new PriorityQueue<Msg>();
	}
	
	public MessageLayer(int pw, int ph, int bh, int centerOffset, Font font, int maxalpha) {
		parentWidth = pw;
		parentHeight = ph;
		bgColor = new Color(50,50,50,0);
		textFont = font;
		showTime = 0;
		fadeInTicks = 0;
		fadeOutTicks = 0;
		message = "";
		currentAlpha = 0;
		alphaStep = 0;
		alphaMax = maxalpha;
		boxHeight = bh;
		offset = centerOffset;
		//messages = new PriorityQueue<Msg>();
	}
	
	public void fadeInStep()
	{
		currentAlpha += alphaStep;
		if(!(currentAlpha > alphaMax));
		{
			bgColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), currentAlpha);
			foreColor = new Color(foreColor.getRed(), foreColor.getGreen(), foreColor.getBlue(), currentAlpha);
		}
		
		framePaint();
		--fadeInTicks;
	}
	
	public void framePaint()
	{
		graphics.setColor(bgColor);
		GraphicsUtils.fillBlurredBorderRect(graphics, bgColor, 0, parentHeight/2 - (boxHeight-borderSize*2)/2 + offset,
				parentWidth, boxHeight-borderSize*2, 0, borderSize);
		//graphics.fillRect(0, parentHeight/2 - boxHeight/2 + offset, parentWidth, boxHeight);
		graphics.setColor(foreColor);
		graphics.setFont(textFont);
		int strLength = (int) graphics.getFontMetrics().getStringBounds(message, graphics).getWidth();
		int strHeight = (int) graphics.getFontMetrics().getStringBounds(message, graphics).getHeight();
		int startX = parentWidth/2 - strLength/2;
		int startY = parentHeight/2 + strHeight/2;
		graphics.drawString(message, startX, startY + offset -2);
		
	}
	
	public void fadeOutStep()
	{
		currentAlpha -= alphaStep;
		if(!(currentAlpha < 0));
		{
			bgColor = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), Math.max(currentAlpha, 0));
			foreColor = new Color(foreColor.getRed(), foreColor.getGreen(), foreColor.getBlue(), Math.max(currentAlpha, 0));
		}
		framePaint();
		--fadeOutTicks;
	}
	
	public void paint()
	{
		if(!isStarted) return;
		if(fadeOutTicks <= 0)
		{
			isStarted = false;
			currentAlpha = fadeOutTicks = fadeInTicks = showTime = 0;
			return;
		}
		else
		{
			if(showTime <= 0 && !permanent)
			{
				fadeOutStep();
			}
			else
			{
				if(fadeInTicks <= 0)
				{
					framePaint();
					--showTime;
				}
				else
				{
					fadeInStep();
				}
			}
		}
	}
	
	public void reset()
	{
		showTime = 0;
		fadeInTicks = 0;
		fadeOutTicks = 0;
		message = "";
		currentAlpha = 0;
		alphaStep = 0;
	}
	
	public void show(String text, Color color, int messageTime, int fTime)
	{
		//messages.add(new Msg(text,color,messageTime,fTime));
		
		reset();
		message = text;
		foreColor = color;
		showTime = messageTime / renderDelay;
		permanent = !(messageTime >= 0); //if messageTime is negative, the message will be visible until fadeOut() is invoked.
		fadeOutTicks = fadeInTicks = fTime / renderDelay; 
		alphaStep = alphaMax / (fTime / renderDelay);
		isStarted = true;
		
	}
	
	public void fadeOut()
	{
		permanent = false;
		showTime = 0;
		fadeInTicks = 0;
		
	}
	
	public void setParentSize(Dimension ssize) {
		parentWidth = ssize.width;
		parentHeight = ssize.height;
		
	}

}
