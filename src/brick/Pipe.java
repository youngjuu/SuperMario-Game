package brick;

import java.awt.Graphics;

import entity.enemy.Piranha;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Pipe extends Brick {
	
	public Pipe(int x, int y, int width, int height, boolean block, Id id, Controller controller) {
		super(x, y, width, height, block, id, controller);
	}

	public void render(Graphics g) {
		g.drawImage(Main.pipe.getBufferedImage(), x, y, width, height, null);
	}

	public void update() {
		
	}

}
