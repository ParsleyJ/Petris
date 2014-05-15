package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import petris.Action;

public class PetrisDialogMenu extends PetrisChildMenu {

	private PetrisMenuEntry confirmEntry;
	public DialogConfirmAction confirmAction;
	
	public PetrisDialogMenu(Dimension parentSize, String title, Font font,
			FadingColor backgroundColor, FadingColor foreColor, DialogConfirmAction action) { //TODO: rewrite ctor declaration
		//super(parentSize, title, font, backgroundColor, foreColor);
		super(title, font, parentSize.width, 40, backgroundColor, foreColor);
		confirmEntry = new PetrisMenuEntry("Ok", font, parentSize.width, 40, new FadingColor(new Color(50,50,50,230), 230), foreColor, true);
		super.addEntry(confirmEntry);
		confirmAction = action;
		confirmEntry.setAction(new Action()
		{
			@Override
			public void run() {
				PetrisDialogMenu.this.confirmAction.run(entries);				
			}
		});
	}
	
	
	public void addEntry(PetrisMenuEntry e)
	{
		super.addEntry(e,entries.size()-1); //confirmEntry will be always the last entry
	}
	
	
	/*public void performOk()
	{
		super.performOk();
		System.out.println(root.entries.get(root.focusedEntry).getText());
		
		if(confirmEntry.isFocused())
		{
			
			confirmAction.run(entries);
			this.close();
		}
		
	}*/
	

}
