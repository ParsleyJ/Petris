package petris.gui;

import java.awt.Graphics;

public abstract class Layer {

	protected Graphics graphics;
	protected boolean enabled = true;
	public abstract void paint();

	public Graphics getGraphics() {
		return graphics;
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
