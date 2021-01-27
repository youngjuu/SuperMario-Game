package brick;

import java.awt.Graphics;

import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class DieBrick extends Brick{
	
	private int hittingtime=0;
	private boolean hitting = false;
	
	public DieBrick(int x, int y, int width, int height, boolean block, Id id, Controller controller) {
		super(x, y, width, height, block, id, controller);	
	}

	public void render(Graphics g) {
		if (!activate) 
			g.drawImage(Main.diebox.getBufferedImage(), x, y ,width, height, null);
		else
			g.drawImage(Main.diedbox.getBufferedImage(), x, y, width, height, null );	
	}
	
	public void update() {		
	}
}