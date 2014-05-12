package petris.gui;

import java.awt.Graphics;

public interface MenuInterface {
	
	public void show();
	
	public void close();
	
	
	public void addEntry(PetrisMenuEntry entry);
	public void addEntry(PetrisMenuEntry entry, int index);
	
	public void removeEntry(int index);
	
	public void paint(Graphics graphics);
	
	//main navigation commands:
	public void performGoUp();
	public void performGoDown();
	public void performGoToEntry(int index);
	public void performGoLeft();
	public void performGoRight();
	public void performOk();
	public void performBack();
	
}
