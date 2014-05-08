package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;


public class Render extends JPanel implements ActionListener, RenderInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Layer> layers;
	Timer timer;
	Dimension dimension;
	boolean isActivated = false;
	private int frameDelay = 20;
	private int frameCounter = 0;
	
	
	

	public Render(Color bgCol, Dimension size)
	{
		this.setBackground(bgCol);
		layers = new ArrayList<Layer>();
		frameDelay = 20;
		timer = new Timer(frameDelay, this);
		setPreferredSize(size);
		dimension = size;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		repaint();
	}
	
	public int addLayer(Layer l)
	{
		layers.add(l);
		if (!isActivated)
		{
			isActivated = true;
			timer.start();
		}
		return layers.size();
	}
	
	
	
	public void paintComponent(Graphics g)
	{
		
		g.clearRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
		g.setColor(Color.black);
		g.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
		for(Layer l : layers)
		{
			l.setGraphics(g);
			l.paint();
		}
		++frameCounter;
	}
	
	public int getFrameDelay() {
		return frameDelay;
	}

	public void setFrameDelay(int frameDelay) {
		this.frameDelay = frameDelay;
		timer.setDelay(frameDelay);
	}
	
	public int getIdealFps()
	{
		return 1000 / this.frameDelay;
	}
	
	public int getSampledRealFps()
	{
		int result = frameCounter;
		this.frameCounter = 0;
		return result;
	}
	
	public int getRealFps(int samplingDelay)
	{
		int result = samplingDelay / this.frameCounter;
		this.frameCounter = 0;
		return result;
		
	}
	
}
