package brick;

import java.awt.Graphics;
import java.awt.Rectangle;

import etc.Id;
import gameloop.Controller;

public abstract class Brick {
	public int x, y, width, height;
	public int velX, vleY;
	public int part;		// 1-파이프 머리, 2-파이프 중간, 3-파이프 밑단
	
	public boolean block;
	public boolean activate = false;
	
	public Id id;
	public Controller controller;
	
	public Brick(int x, int y, int width, int height, boolean block, Id id, Controller controller) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.block=block;
		this.id=id;
		this.controller=controller;
	}
	
	public abstract void render(Graphics g);
	public abstract void update();
	
	public void kill() {
		controller.removeBrick(this);
	}
	
	public Rectangle collision() {
		return new Rectangle(getX(),getY(),width,height);
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}	
	public Id getId() {
		return id;
	}
	public boolean isBlock() {
		return block;
	}
	public void setVelX(int velX) {
		this.velX = velX;
	}
	public void setVelY(int vleY) {
		this.vleY = vleY;
	}
}
