package petris.gui;

import java.awt.Graphics;

import petris.gui.effects.Filter;

public abstract class Layer {

	protected Graphics graphics;
	protected boolean enabled = true;
	protected Filter backFilter = null;
	protected boolean backFilterEnabled = false;
	protected Filter frontFilter = null;
	protected boolean frontFilterEnabled = false;
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

	public Filter getBackFilter() {
		return backFilter;
	}

	public void setBackFilter(Filter filter) {
		this.backFilter = filter;
	}


	public Filter getFrontFilter() {
		return frontFilter;
	}


	public void setFrontFilter(Filter frontFilter) {
		this.frontFilter = frontFilter;
	}


	public boolean isBackFilterEnabled() {
		return backFilterEnabled;
	}


	public void setBackFilterEnabled(boolean backFilterEnabled) {
		this.backFilterEnabled = backFilterEnabled;
	}


	public boolean isFrontFilterEnabled() {
		return frontFilterEnabled;
	}


	public void setFrontFilterEnabled(boolean frontFilterEnabled) {
		this.frontFilterEnabled = frontFilterEnabled;
	}
	
}
