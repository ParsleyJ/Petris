package petris.gui;

public class FlashingGravityColor extends DynamicColor {

	private int gravity;
	private int maxAlpha = 30;
	private int minAlpha = 0;
	//private int alphaStep = 1;
	//private boolean loop;
	//private boolean ascending;
	private double cosAngle = 0;
	private double detail = 30.0;
	private double exponent = 20;
	private double speed = 0.5;
	
	public FlashingGravityColor() {
		super();
		
	}
	
	public FlashingGravityColor(int gr, int d)
	{
		super(0,255,0,0,d);
		gravity =gr;
		//loop = true;
		//ascending = true;
		updateRedGreen();
		start();
	}
	
	protected void checkValuesAndThrow()
	{
		super.checkValuesAndThrow();
		if (gravity < 0 || gravity >510) throw new RuntimeException("Gravity out of range");
	}
	
	private void updateRedGreen()
	{
		setGreen(Math.min(510-gravity, 255));
		setRed(Math.min(gravity, 255));
	}

	@Override
	void tick() {
		/*
		if (ascending)
		{
			if (getAlpha() + alphaStep <= maxAlpha) setAlpha(getAlpha() + alphaStep);
			else ascending = false;
		}
		else
		{
			if (getAlpha() - alphaStep >= minAlpha) setAlpha(getAlpha() - alphaStep);
			else ascending = true;
		}
		*/
		
		setAlpha((int)(Math.pow(Math.sin((cosAngle/detail)*Math.PI),exponent )*(maxAlpha - minAlpha) + minAlpha));
		cosAngle = (cosAngle + speed) % detail ;
	}

	public int getGravity() {
		return gravity;
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
		checkValuesAndThrow();
		updateRedGreen();
	}
	
	/*
	public void setInLoop(boolean b)
	{
		loop = b;
	}
	*/
	

}
