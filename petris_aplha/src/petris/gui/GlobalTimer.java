package petris.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class GlobalTimer extends Timer implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int curDelay = 20;
	ArrayList<GlobalTimerListener> listeners = new ArrayList<GlobalTimerListener>();
	ArrayList<Integer> curSteps = new ArrayList<Integer>();
	
	public GlobalTimer(int delay) {
		super(delay, null);
		super.addActionListener(this);
		curDelay = delay;
	}
	
	public void addListener(GlobalTimerListener e)
	{
		listeners.add(e);
		curSteps.add(e.getOffset());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < listeners.size(); ++i)
		{
			if (listeners.get(i).isRunning())
			{
				curSteps.set(i,curSteps.get(i) + listeners.get(i).getDelay());
				if (curSteps.get(i) >= curDelay)
				{
					listeners.get(i).tick(e);
					curSteps.set(i,Math.abs(curSteps.get(i)-curDelay));
				}
			}
			
		}

	}

}
