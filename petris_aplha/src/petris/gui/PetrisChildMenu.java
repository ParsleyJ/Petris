package petris.gui;

import java.awt.Font;
import java.util.ArrayList;

import petris.Action;

public class PetrisChildMenu extends PetrisMenuEntry {
	
	public ArrayList<PetrisMenuEntry> entries = new ArrayList<PetrisMenuEntry>();
	private boolean isEmpty = true;
	
	private Action onEntered;
	private Action onExiting;
	private Action onEnteringChild;
	
	public PetrisChildMenu(String entryText, Font font, int parentWidth,
			int entryHeight, FadingColor background, FadingColor foreColor) {
		super(entryText, font, parentWidth, entryHeight, background, foreColor);
		
	}
	
	
	public void setRootMenu(PetrisMenu root)
	{
		super.setRootMenu(root);
		if (isEmpty) return;
		for (PetrisMenuEntry e : entries)
		{
			e.setRootMenu(root);
		}
	}
	
	public void addEntry(PetrisMenuEntry entry) 
	{
		entries.add(entry);
		entry.setRootMenu(this.getRootMenu());
		if (root!= null)root.replace();
		if (isEmpty)
		{
			isEmpty = false;
			entries.get(0).setFocused(true);
			
		}
	}	
	
	public void addEntry(PetrisMenuEntry entry, int index) {
		entries.add(index, entry);
		entry.setRootMenu(this.getRootMenu());
		if (root!= null)root.replace();
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


	public Action getOnEntered() {
		return onEntered;
	}


	public void setOnEntered(Action onEntered) {
		this.onEntered = onEntered;
	}


	public Action getOnExiting() {
		return onExiting;
	}


	public void setOnExiting(Action onExiting) {
		this.onExiting = onExiting;
	}
	
	public void performOnEntered()
	{
		if (onEntered!=null)onEntered.run();
	}
	
	public void performOnExiting()
	{
		if(onExiting!=null)onExiting.run();
	}
	
	public void clearEntries()
	{
		entries.clear();
	}


	public Action getOnEnteringChild() {
		return onEnteringChild;
	}


	public void setOnEnteringChild(Action onEnteringChild) {
		this.onEnteringChild = onEnteringChild;
	}


	public void performOnEnteringChild() {
		if (onEnteringChild!=null)onEnteringChild.run();
		
	}
}
