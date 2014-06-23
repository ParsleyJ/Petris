package petris.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;
import petris.Action;

public class TextFieldMenuEntry extends PetrisMenuEntry {

	private String prompText = "";
	private String fieldText = "";
	private int cursorPosition = 0;
	private int borderSpace = 30;
	private boolean inputMode = false;
	private Action onEnteringEdit;
	private Action onExitingEdit;
	
	public TextFieldMenuEntry(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor );
	
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
		this.cursorPosition = fieldText.length();
	}

	public int getBorderSpace() {
		return borderSpace;
	}

	public void setBorderSpace(int borderSpace) {
		this.borderSpace = borderSpace;
	}

	public void input(String s)
	{
		String left = fieldText.substring(0, cursorPosition);
		String right = fieldText.substring(Math.min(fieldText.length(), cursorPosition));
		left+=s;
		fieldText = left + right;
		cursorPosition = Math.min(fieldText.length(), cursorPosition+1);
	}
	
	public boolean backspace()
	{
		if (fieldText.isEmpty() || cursorPosition == 0) return false;
		String left = fieldText.substring(0, cursorPosition-1);
		String right = fieldText.substring(Math.min(fieldText.length(), cursorPosition));
		fieldText = left + right;
		cursorPosition = Math.max(0, cursorPosition-1);
		//fieldText = fieldText.substring(0, fieldText.length()-1);
		return true;
	}
	public boolean delete()
	{
		if (fieldText.isEmpty()) return false;
		String left = fieldText.substring(0, cursorPosition);
		String right = fieldText.substring(Math.min(fieldText.length(), cursorPosition+1));
		fieldText = left + right;
		//cursorPosition = Math.min(fieldText.length(), cursorPosition+1);
		//fieldText = fieldText.substring(0, fieldText.length()-1);
		return true;
	}
	
	@Override
	public void performOk()
	{
		if (onEnteringEdit!=null)onEnteringEdit.run();
		inputMode = true;
		root.setTextInputMode(inputMode);
		
	}
	
	public boolean isInputMode() {
		return inputMode;
	}

	public void setInputMode(boolean inputMode) {
		this.inputMode = inputMode;
	}
	
	@Override
	public void performTextInput(KeyEvent keyEvent)
	{
		int keyCode = keyEvent.getKeyCode();
		if (inputMode){
			if (keyCode == KeyEvent.VK_BACK_SPACE)
			{
				this.backspace();
			}
			else if (keyCode == KeyEvent.VK_DELETE)
			{
				this.delete();
			}
			else if (keyCode == KeyEvent.VK_ENTER)
			{
				if (onExitingEdit!=null)onExitingEdit.run();
				inputMode = false;
				root.setTextInputMode(inputMode);
				//System.out.println("Inserted text: <start>" + fieldText+ "<end> length: " + fieldText.length());//DEBUG
				//System.out.println("Prompt text:   <start>" + prompText+ "<end> length: " + prompText.length());//DEBUG
				root.ignoreNextEnter();
				
				
			}
			else if (keyCode == KeyEvent.VK_RIGHT)
			{
				performRight();
			}
			else if (keyCode == KeyEvent.VK_LEFT)
			{
				performLeft();
			}
			else if (keyCode == KeyEvent.VK_UP)
			{
				
			}
			else if (keyCode == KeyEvent.VK_DOWN)
			{
				
			}
			else if (keyCode == KeyEvent.VK_SHIFT)
			{
				
			}
			else if (keyCode == KeyEvent.VK_ALT)
			{
				
			}
			else if (keyCode == KeyEvent.VK_ESCAPE)
			{
				
			}
			else if (keyCode == 0 || keyCode == 17 || keyCode == 157 || (keyCode >= 112 && keyCode <= 123))
			{
				
			}
			else 
			{
				this.input("" + keyEvent.getKeyChar());
				//System.out.println(keyCode); //DEBUG
			}
		}
		return;
	}

	
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
		if (cursorPosition == fieldText.length()) return;
		++cursorPosition;
	}
	
	@Override
	public void paint(Graphics graphics, int y)
	{
		width = root.parentWidth;
		if (!isVisible && textColor.getAlpha()==0 && bgColor.getAlpha()==0 && focusColor.getAlpha()==0) return;
		graphics.setColor(bgColor.getStaticColor());		
		if(style == "Blurred")GraphicsUtils.fillBlurredBorderRect(graphics, bgColor.getStaticColor(), 0, y+borderSize, width, height-borderSize*2, borderSize, borderSize);
		else graphics.fillRect(0,y,width ,height);
		graphics.setColor(focusColor.getStaticColor());
		if(style == "Blurred")GraphicsUtils.fillBlurredBorderRect(graphics, focusColor.getStaticColor(), 0, y+borderSize, width, height-borderSize*2, borderSize, borderSize);
		else graphics.fillRect(0,y,width ,height);
		String printText = "";
		if (prompText.length() != 0)
		{
			if (fieldText.length() != 0)printText = fieldText;
			else printText = prompText;
		}
		else printText = fieldText;
		int h2 = (int) graphics.getFontMetrics().getStringBounds(printText,graphics).getHeight();
		int h = (int) graphics.getFontMetrics().getStringBounds(text,graphics).getHeight();
		int w = (int) graphics.getFontMetrics().getStringBounds(text,graphics).getWidth();
		Point titleCoords =  GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(w,h-5-h2-h/2));
		Point textCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(width-20-borderSpace*2,h2));
		Point boxCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,y), new Dimension(width,height), new Dimension(width-borderSpace*2,h2 + 5));
		
		if (isEnabled()) graphics.setColor(textColor.getStaticColor());
		else graphics.setColor(disabledColor.getStaticColor());		
		graphics.drawString(text, titleCoords.x, titleCoords.y);
		
		if (isEnabled()) {
			if (prompText.length() != 0)
			{
				if (fieldText.length() != 0) graphics.setColor(textColor.getStaticColor());
				else graphics.setColor(disabledColor.getStaticColor());
			}
			else graphics.setColor(textColor.getStaticColor());
		}
		else graphics.setColor(disabledColor.getStaticColor());		
		graphics.setFont(textFont);
		
		
	
		graphics.drawString(printText, textCoords.x, textCoords.y +h2);
		
		if (isEnabled()) {
			if(inputMode) graphics.setColor(textColor.getStaticColor());
			else graphics.setColor(disabledColor.getStaticColor());
		}
		else graphics.setColor(disabledColor.getStaticColor());		
		int boxHeight = h2+6;
		graphics.drawRect(boxCoords.x, boxCoords.y-(boxHeight) +h2, width-borderSpace*2, boxHeight);
		
		if(inputMode && (root.getCurrentFrame()%50)<25)
		{
			String tmp = printText.substring(0,cursorPosition);
			int cursorX = (int) graphics.getFontMetrics().getStringBounds(tmp,graphics).getWidth()+textCoords.x;
			int cursorH = (int) graphics.getFontMetrics().getStringBounds(tmp,graphics).getHeight();
			graphics.drawLine(cursorX, boxCoords.y-(cursorH)+h2, cursorX, boxCoords.y+h2);
		}
	}

	public Action getOnEnteringEdit() {
		return onEnteringEdit;
	}

	public void setOnEnteringEdit(Action onEnteringEdit) {
		this.onEnteringEdit = onEnteringEdit;
	}

	public Action getOnExitingEdit() {
		return onExitingEdit;
	}

	public void setOnExitingEdit(Action onExitingEdit) {
		this.onExitingEdit = onExitingEdit;
	}
	
}
