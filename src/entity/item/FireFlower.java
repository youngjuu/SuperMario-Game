package entity.item;

import java.awt.Graphics;

import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class FireFlower extends Entity{

	public FireFlower(int x, int y, int width, int height, Id id, Controller controller) {
		super(x, y, width, height, id, controller);		
	}
	
	public void render(Graphics g) {
		g.drawImage(Main.fireflower.getBufferedImage(), getX(), getY(),getWidth(),getHeight(),null);
	}
	
	public void update() {
	}

}