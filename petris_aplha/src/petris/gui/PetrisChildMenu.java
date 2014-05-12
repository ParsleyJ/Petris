package petris.gui;

import java.awt.Font;
import java.util.ArrayList;

public class PetrisChildMenu extends PetrisMenuEntry {
	
	public ArrayList<PetrisMenuEntry> entries = new ArrayList<PetrisMenuEntry>();
	private boolean isEmpty = true;
	
	public PetrisChildMenu(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		
	}
	
	
	public void addEntry(PetrisMenuEntry entry) {
		entries.add(entry);
		entry.setRootMenu(this.getRootMenu());
		if (isEmpty)
		{
			isEmpty = false;
			entries.get(0).setFocused(true);
			
		}
	}
	
	
	public void addEntry(PetrisMenuEntry entry, int index) {
		entries.add(index, entry);
		if (isEmpty)
		{
			isEmpty = false;
			entries.get(0).setFocused(true);
		}
	}
	
	public void removeEntry(int index) {
		entries.remove(index);
		isEmpty = entries.isEmpty();
	}
	
	public void performOk()
	{
		super.performOk();
		root.enterChildMenu(this);
	}
}
