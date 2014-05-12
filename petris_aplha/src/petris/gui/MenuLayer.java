package petris.gui;

public class MenuLayer extends Layer {

	private MenuInterface currentMenu;
	
	
	
	@Override
	public void paint() {
		// TODO Auto-generated method stub
		currentMenu.paint(graphics);
	}

	public MenuInterface getCurrentMenu() {
		return currentMenu;
	}

	public void setCurrentMenu(MenuInterface currentMenu) {
		this.currentMenu = currentMenu;
	}

}
