package brick;

import java.awt.Graphics;

import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Goal extends Brick {

	public Goal(int x, int y, int width, int height, boolean block, Id id, Controller controller) {
		super(x, y, width, height, block, id, controller);
	}

	public void render(Graphics g) {
		g.drawImage(Main.goal[0].getBufferedImage(), getX(), getY()+256, width, 64, null);
		g.drawImage(Main.goal[1].getBufferedImage(), getX(), getY()+64, width, 64*3, null);	
		g.drawImage(Main.goal[2].getBufferedImage(), getX()+64, getY(), width, 64, null);		
		
	}

	public void update() {
		
	}

}
