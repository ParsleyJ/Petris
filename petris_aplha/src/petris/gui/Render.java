package petris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import petris.gui.effects.*;


public class Render extends JPanel implements ActionListener, RenderInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Layer> layers;
	PetrisFakeTimer timer;
	Dimension dimension;
	boolean isActivated = false;
	private int frameDelay = 20;
	private int frameCounter = 0;
	
	private Filter filter = null;
	
	
	private boolean freeze = false;
	
	private BufferedImage bufferedImage;
	private Filters curFilter = Filters.noFilter;
	private boolean layerBasedFiltersDisabled = false;
	
	public enum Filters {Blur1, Blur2, Invert, Sharp, Posterize, Tilt, BrightUp, BrightDown, 
						JustEdges, GrayScale, Antialiasing, noFilter, };
	
	public Render(Color bgCol, Dimension size)
	{
		this.setBackground(bgCol);
		layers = new ArrayList<Layer>();
		frameDelay = 20;
		timer = new PetrisFakeTimer(frameDelay, this);
		setPreferredSize(size);
		dimension = size;
		bufferedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		setFilter(curFilter);
		
	}
	
	public Dimension getSize()
	{
		return dimension;
	}
	
	public void setSize(Dimension s)
	{
		super.setSize(s);
		super.setPreferredSize(s);
		bufferedImage = new BufferedImage(s.width, s.height, BufferedImage.TYPE_INT_RGB);
		
		dimension = s;
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
		
		if (layerBasedFiltersDisabled )
		{
			if(filter==null)
			{
				
				g.clearRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
				g.setColor(Color.black);
				g.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
				for(Layer l : layers)
				{
					if (l.isEnabled())
					{
						l.setGraphics(g);
						l.paint();
					}
				}
			
			
			}
			
			else
			{
				
				Graphics2D g2D  = bufferedImage.createGraphics();
				g2D.clearRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
				g2D.setColor(Color.black);
				g2D.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
				for(Layer l : layers)
				{
					if (l.isEnabled())
					{
						l.setGraphics(g2D);
						l.paint();
					}
				}
				
				bufferedImage = filter.processImage(bufferedImage);
					
				g.drawImage(bufferedImage, 0, 0, null);
				
				
				//freeze=true;
			    
			    //((Graphics2D)getGraphics()).drawImage(bufferedImage, blurFilter, this.getLocation().x, this.getLocation().x);
			}
		}
		else
		{
			Graphics2D g2D  = bufferedImage.createGraphics();
			g2D.clearRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
			g2D.setColor(Color.black);
			g2D.fillRect(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
			for(Layer l : layers)
			{
				if (l.isEnabled())
				{
					if (l.getBackFilter() !=null && l.isBackFilterEnabled())
					{
						bufferedImage = l.getBackFilter().processImage(bufferedImage);
						g2D = bufferedImage.createGraphics();
					}
					
					l.setGraphics(g2D);
					l.paint();
					if (l.getFrontFilter() !=null && l.isFrontFilterEnabled()) 
					{
						bufferedImage = l.getFrontFilter().processImage(bufferedImage);
						g2D = bufferedImage.createGraphics();
					}
					
				}
			}
			
			if(filter!=null)bufferedImage = filter.processImage(bufferedImage);
				
			g.drawImage(bufferedImage, 0, 0, null);
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

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		bufferedImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
		this.filter = null;
		this.filter = filter;
		//setFreeze(true); //TODO: this is for debug, remove
	}
	
	public void resetFilter()
	{
		this.filter = null;
	}
	
	/*public void applyFilter(MyFilter filter) {
		  	//Graphics2D g = (Graphics2D) getGraphics();
		  	
		    this.getGraphics().drawImage(filter.processImage(createImage()), this.getLocation().x, this.getLocation().x,null);
		    //repaint();
	}
	 
	public void applyBlurFilter()
	{
		float[] blurMatrix = { 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
		        1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f };
		BufferedImageOp blurFilter = new ConvolveOp(new Kernel(3, 3, blurMatrix),
		        ConvolveOp.EDGE_NO_OP, null);
		((Graphics2D)getGraphics()).drawImage(createImage(), blurFilter, this.getLocation().x, this.getLocation().x);
	}
	
	
	private BufferedImage createImage() {

		    int w = this.getWidth();
		    int h = this.getHeight();
		    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		    Graphics2D g = bi.createGraphics();
		    printFilter =false;
		    this.paint(g);
		    printFilter =true;
		    return bi;
	}
	*/
	

	public boolean isFreeze() {
		return freeze;
	}

	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}
	

	public void nextFilter() 
	{		
		Filters[] allv = Filters.values();
		if (curFilter.ordinal() + 1 >= allv.length) curFilter = allv[0];
		else curFilter = allv[curFilter.ordinal() + 1];
		setFilter(curFilter );
	}
	
	public void previousFilter() {
		Filters[] allv = Filters.values();
		if (curFilter.ordinal() - 1 < 0) curFilter = allv[allv.length-1];
		else curFilter = allv[curFilter.ordinal() - 1];
		setFilter(curFilter);
		
	}
	
	public void setFilter(Filters filter) {
		switch(filter)
		{
		case Blur1:
			setFilter(new BlurFilter());
			break;
		case Blur2:
			setFilter(new BlurFilter2());
			break;
		case Invert:
			setFilter(new InvertFilter());
			break;
		case Sharp:
			setFilter(new SharpenFilter());
			break;
		case Posterize:
			setFilter(new PosterizeFilter());
			break;
		case Tilt:
			setFilter(new TiltFilter());
			break;
		case BrightUp:
			setFilter(new BrightUpFilter());
			break;
		case BrightDown:
			setFilter(new BrightDownFilter());
			break;
		case JustEdges:
			setFilter(new EdgeFilter());
			break;
		case GrayScale:
			setFilter(new GrayScaleFilter());
			break;
		case Antialiasing:
			setFilter(new AntialiasingFilter());
			break;
			
		case noFilter:
			resetFilter();
			break;
		
		default:
			resetFilter();
			break;
		}
		
	}
	
	public Filters getCurrentFilter()
	{
		return curFilter;
	}

	public boolean isLayerBasedFiltersDisabled() {
		return layerBasedFiltersDisabled;
	}

	public void setLayerBasedFiltersDisabled(boolean layerBasedFiltersDisabled) {
		this.layerBasedFiltersDisabled = layerBasedFiltersDisabled;
	}
	
}
