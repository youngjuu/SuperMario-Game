package brick;

import java.awt.Graphics;

import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Wall extends Brick{
	
	public Wall(int x, int y, int width, int height, boolean block, Id id, Controller controller) {
		super(x, y, width, height, block, id, controller);
	}
	
	public void render(Graphics g) {
		g.drawImage(Main.wall.getBufferedImage(), x, y, width, height, null);
	}
	
	public void update() {
		
	}
}