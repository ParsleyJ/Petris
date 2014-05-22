package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import parsleyj.utils.GraphicsUtils;
import parsleyj.utils.GuiUtils;

public class PetrisMenu implements MenuInterface {
	
	protected ArrayList<PetrisMenuEntry> entries = new ArrayList<PetrisMenuEntry>();
	private boolean isListCentered = true;
	
	private Stack<ArrayList<PetrisMenuEntry>> traceback = new Stack<ArrayList<PetrisMenuEntry>>();
	private Stack<String> titlesTraceback = new Stack<String>();
	private Stack<FadingColor> foreColorTraceback = new Stack<FadingColor>();
	private Stack<Integer> focusedTraceback = new Stack<Integer>();
	private Stack<PetrisChildMenu> childTraceback = new Stack<PetrisChildMenu>();
	
	private boolean isVisible = false;
	private boolean isEmpty = true;
	private FadingColor bgColor = new FadingColor(new Color(50,50,50,230), 230);
	private FadingColor titleColor = new FadingColor(new Color(50,200,50,230), 230);
	private Font titleFont;
	protected int parentWidth;
	private int parentHeight;
	private String title;
	
	private int titleHeight = 100;
	private int statusBarHeight = 30;
	
	private int startingY = titleHeight;
	
	protected int focusedEntry = 0;
	private int spaceHeight = 1;
	private boolean needToReset = false;
	
	private final int fadeTime = 300;
	protected String style = "Blurred";
	protected int titleBorderSize = 10;
	
	private boolean scrollActive = false;
	
	public final PetrisChildMenu nullChild = new PetrisChildMenu("###root###",null,0,0,new FadingColor(Color.black),new FadingColor(Color.black));
	
	private PetrisChildMenu curChild = nullChild;
	
	
	public PetrisMenu(Dimension parentSize, String title, Font font)
	{
		this.setParentWidth((int)parentSize.getWidth());
		this.setParentHeight((int)parentSize.getHeight());
		this.setTitle(title);
		titleFont = font;
	}
	
	
	public PetrisMenu(Dimension parentSize, String title, Font font, FadingColor backgroundColor, FadingColor foreColor)
	{
		this.setParentWidth((int)parentSize.getWidth());
		this.setParentHeight((int)parentSize.getHeight());
		this.setTitle(title);
		titleFont = font;
		bgColor = backgroundColor;
		titleColor = foreColor;
	}
	
	
	@Override
	public void show() {
		isVisible = true;
		if (needToReset) 
		{
			resetRoot();
			needToReset = false;
		}
		bgColor.fadeIn(-1, fadeTime);
		titleColor.fadeIn(-1, fadeTime);
		for (PetrisMenuEntry e : entries)
		{
			e.show();
		}

	}

	@Override
	public void close() {
		isVisible = false;
		bgColor.fadeOut();
		titleColor.fadeOut();
		for (PetrisMenuEntry e : entries)
		{
			e.close();
		}
	}


	@Override
	public void addEntry(PetrisMenuEntry entry) {
		entries.add(entry);
		entry.setRootMenu(this);
		startingY = getStartingListY();
		if (isEmpty)
		{
			isEmpty = false;
			entries.get(0).setFocused(true);
			focusedEntry = 0;
		}
	}
	
	@Override
	public void addEntry(PetrisMenuEntry entry, int index) {
		entries.add(index, entry);
		entry.setRootMenu(this);
		startingY = getStartingListY();
		if (isEmpty)
		{
			isEmpty = false;
			entries.get(0).setFocused(true);
			focusedEntry = 0;
		}
	}

	@Override
	public void removeEntry(int index) {
		entries.remove(index);
		startingY = getStartingListY();
		isEmpty = entries.isEmpty();
		focusedEntry = 0;
	}

	@Override
	public void paint(Graphics graphics) {	
		
		//draw entries
		int currY;
		if (scrollActive){
			currY = titleHeight + spaceHeight + getScrollOffset();
		}
		else currY = startingY;
		for (int i = 0; i < entries.size(); ++i)
		{
			PetrisMenuEntry e = entries.get(i);
			e.paint(graphics, currY);
			currY += e.getHeight() + spaceHeight;
		}
		
		if (!isVisible && titleColor.getAlpha()==0 && bgColor.getAlpha()==0) ; //return;
		else {
			//draw title
			graphics.setColor(bgColor.getStaticColor());
			if(style == "Blurred")GraphicsUtils.fillBlurredBorderRect(graphics, bgColor.getStaticColor(), 0, 0, parentWidth, titleHeight-titleBorderSize*2, titleBorderSize, titleBorderSize);
			else graphics.fillRect(0, 0, parentWidth, titleHeight);
			graphics.setColor(titleColor.getStaticColor());
			graphics.setFont(titleFont);
			int w = (int) graphics.getFontMetrics().getStringBounds(title,graphics).getWidth();
			int h = (int) graphics.getFontMetrics().getStringBounds(title,graphics).getHeight();
			Point titleCoords;
			if (style == "Blurred") titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,0), new Dimension(parentWidth, titleHeight-titleBorderSize*2), new Dimension(w,h));
			else titleCoords = GuiUtils.getCenteredChildRectCoords(new Point(0,0), new Dimension(parentWidth, titleHeight), new Dimension(w,h));
			graphics.drawString(title, titleCoords.x, titleCoords.y);
		}
		
		//draw status bar
		
		
	}

	private int getScrollOffset() {
		int offs = 0;
		for (int i  = 0; i < focusedEntry; ++i)
		{
			offs += entries.get(i).getHeight() + spaceHeight;
		}
		return -Math.min(offs, getTotalEntriesHeight()- (parentHeight - spaceHeight*2 - titleHeight));
	}

	private int getTotalEntriesHeight()
	{
		int totalListHeight = 0;
		for (PetrisMenuEntry e : entries)
		{
			totalListHeight += e.getHeight() + spaceHeight;
		}
		return totalListHeight - spaceHeight;
	}
	
	private int getStartingListY() {
		int totalListHeight = 0;
		if (isListCentered)
		{
			for (PetrisMenuEntry e : entries)
			{
				totalListHeight += e.getHeight() + spaceHeight;
			}
			
			if (totalListHeight > parentHeight - spaceHeight*2 - titleHeight) scrollActive = true;
			else scrollActive = false;
			
			totalListHeight -= spaceHeight;
			return parentHeight/2 - totalListHeight/2;
		}
		else return titleHeight + spaceHeight;
	}

	@Override
	public void performGoUp() 
	{
		if (focusedEntry > 0)
		{
			entries.get(focusedEntry).setFocused(false);
			while(true)
			{
				if(entries.get(focusedEntry-1).isEnabled() || focusedEntry == 0) break;//TODO: what if first entry is disabled?
				else --focusedEntry;
			}
			if (focusedEntry != 0)--focusedEntry;
			entries.get(focusedEntry).setFocused(true);			
		}			
	}

	@Override
	public void performGoDown() {
		if (focusedEntry < entries.size() - 1)
		{
			entries.get(focusedEntry).setFocused(false);
			while (true)
			{
				if(entries.get(focusedEntry+1).isEnabled() || focusedEntry == entries.size()-1 )break; 
				else ++focusedEntry;
			}
			if (focusedEntry != entries.size() - 1)++focusedEntry;
			entries.get(focusedEntry).setFocused(true);
		}
	}

	@Override
	public void performGoToEntry(int index) {
		try
		{
			entries.get(index).setFocused(true);
			entries.get(focusedEntry).setFocused(false);
			focusedEntry = index;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void performGoLeft() {
		entries.get(focusedEntry).performLeft();

	}

	@Override
	public void performGoRight() {
		entries.get(focusedEntry).performRight();

	}

	@Override
	public void performOk() {
		entries.get(focusedEntry).performOk();

	}

	@Override
	public void performBack() {
		
		if(titlesTraceback.isEmpty()) return;
		
		curChild.performExiting();
		curChild= childTraceback.pop();
		title = titlesTraceback.pop();
		titleColor = foreColorTraceback.pop();
		entries = traceback.pop();
		startingY = getStartingListY();
		isEmpty = entries.isEmpty();
		focusedEntry = focusedTraceback.pop();
		if (!isEmpty)  
		{
			for (int i = 0; i < entries.size(); ++ i)
			{
				if (i==focusedEntry) entries.get(focusedEntry).setFocused(true);
				else entries.get(i).setFocused(false);
			}			
		}

	}
	
	private void resetRoot()
	{
		if(titlesTraceback.isEmpty()) return;
		title = titlesTraceback.get(0);
		titleColor = foreColorTraceback.get(0);
		entries = traceback.get(0);
		for (PetrisMenuEntry e: entries)
		{
			e.bgColor.setAlpha(bgColor.getAlpha());
			e.textColor.setAlpha(bgColor.getAlpha());
		}
		startingY = getStartingListY();
		isEmpty = entries.isEmpty();
		focusedEntry = focusedTraceback.get(0);
		if (!isEmpty) 
		{
			for (int i = 0; i < entries.size(); ++ i)
			{
				if (i==focusedEntry) entries.get(focusedEntry).setFocused(true);
				else entries.get(i).setFocused(false);
			}			
		}
		curChild.performExiting();
		curChild = nullChild;
	}
	
	public void resetRootEntries() { //it will be resetted before the next show()
		needToReset  = true;
	}

	public FadingColor getBgColor() {
		return bgColor;
	}

	public void setBgColor(FadingColor bgColor) {
		this.bgColor = bgColor;
	}

	public int getParentWidth() {
		return parentWidth;
	}

	public void setParentWidth(int parentWidth) {
		this.parentWidth = parentWidth;
	}

	public int getParentHeight() {
		return parentHeight;
	}

	public void setParentHeight(int parentHeight) {
		this.parentHeight = parentHeight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTitleHeight() {
		return titleHeight;
	}

	public void setTitleHeight(int titleHeight) {
		this.titleHeight = titleHeight;
	}

	public int getStatusBarHeight() {
		return statusBarHeight;
	}

	public void setStatusBarHeight(int stastusBarHeight) {
		this.statusBarHeight = stastusBarHeight;
	}

	public int getSpaceHeight() {
		return spaceHeight;
	}


	public void setSpaceHeight(int spaceHeight) {
		this.spaceHeight = spaceHeight;
	}


	public boolean isEmpty() {
		return isEmpty;
	}

	public void enterChildMenu(PetrisChildMenu petrisChildMenu) {
		traceback.push(new ArrayList<PetrisMenuEntry>(entries));
		titlesTraceback.push("" + title);
		foreColorTraceback.push(titleColor);
		focusedTraceback.push(focusedEntry);
		childTraceback.push(curChild);
		curChild = petrisChildMenu;
		entries = petrisChildMenu.entries;
		title = petrisChildMenu.getText();
		titleColor = petrisChildMenu.getTextColor();
		
		startingY = getStartingListY();
		isEmpty = entries.isEmpty();
		focusedEntry = 0;
		if (!isEmpty)  
		{
			for (int i = 0; i < entries.size(); ++ i)
			{
				if (i==focusedEntry) entries.get(focusedEntry).setFocused(true);
				else entries.get(i).setFocused(false);
				entries.get(i).show();
			}			
		}
		curChild.performEntered();
		
	}


	public void replace() {
		startingY = getStartingListY();
		
	}

	
	
	

}
