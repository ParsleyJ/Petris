package petris;

public class Item {
	protected int locX;
	protected int locY;
	
	public Item()
	{
		locX = 0;
		locY = 0;
	}
	
	public Item(Item i)
	{
		locX = i.locX;
		locY = i.locY;
	}
	
	public int x() {return locX;} //position on the grid
	public int y() {return locY;}
	
	public void setX(int arg) {locX = arg;}
	public void setY(int arg) {locY = arg;}
}
