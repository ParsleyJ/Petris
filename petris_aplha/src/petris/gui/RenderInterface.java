package petris.gui;

public interface RenderInterface {
	public void repaint();
	public int addLayer(Layer l);
	public int getFrameDelay();
	public void setFrameDelay(int frameDelay);
	public int getIdealFps();
	public int getSampledRealFps();
	public int getRealFps(int samplingDelay);
}
