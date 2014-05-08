package petris.gui;

import java.awt.Graphics;

public abstract class Layer {

	protected Graphics graphics;
	
	
	
	public abstract void paint();

	public Graphics getGraphics() {
		return graphics;
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}
	
	
	
	
}
