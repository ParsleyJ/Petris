package petris.gui;

import java.awt.event.ActionEvent;

public interface GlobalTimerListener {
	
	public int getDelay();
	public int getOffset();
	public void tick(ActionEvent e);
	public boolean isRunning();

}
