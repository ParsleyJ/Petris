package petris.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PetrisFakeTimer implements GlobalTimerListener {
	
	public static GlobalTimer globalTimer = new GlobalTimer(20);
	
	private int curDelay = 20;
	private int curOffset = 0;
	private boolean started;
	private ActionListener curListener = null;
	
	public PetrisFakeTimer(int delay, ActionListener listener)
	{
		curDelay = delay;
		curListener = listener;
		globalTimer.addListener(this);
		if (!globalTimer.isRunning()) globalTimer.start();
	}
	
	public PetrisFakeTimer(int delay, int offset,  ActionListener listener)
	{
		curDelay = delay;
		curOffset = offset;
		curListener = listener;
		globalTimer.addListener(this);
		if (!globalTimer.isRunning()) globalTimer.start();
	}

	@Override
	public int getDelay() {
		return curDelay;
	}

	@Override
	public int getOffset() {
		return curOffset;
	}

	@Override
	public void tick(ActionEvent e) {
		
		curListener.actionPerformed(e);

	}
	
	public void start()
	{
		started = true;
	}
	
	public void stop()
	{
		started = false;
	}
	
	public boolean isRunning()
	{
		return started;
	}
	
	public void setDelay(int d)
	{
		curDelay = d;
	}

}
