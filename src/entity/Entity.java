package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import etc.Id;
import gameloop.Controller;
import gameloop.Main;
import mode.BossMode;
import mode.KoopaMode;
import mode.PlayerMode;

// 추상 클래스
public abstract class Entity {
	public int x, y;
	public int width, height;
	public int velX, velY;	
	public int side = 0;		// 0-왼쪽 면 , 1-오른쪽 면
	public int frame = 0, frameDelay = 0;
	public int hp;
	public int stateTime;
	
	public double gravity = 0.0;
	
	public boolean jumping = false;
	public boolean falling = false;
	public boolean ridingPipe = false;
	public boolean attackable = true;
	public boolean dead = false;
	
	public Controller controller;
	public Id id;
	
	public KoopaMode koopamode;
	public BossMode bossMode;
	
	public Entity(int x, int y, int width, int height, Id id, Controller controller) {	
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		this.controller = controller;
	}
	
	// 추상 메소드
	public abstract void render(Graphics g);
	public abstract void update();
	
	public void die() {
		if(getId() == Id.player)
			if(Main.inBonusTime)
				Main.inBonusTime = false;
			dead = true;
		
		if(getId() != Id.player)
			controller.killEntity(this); //죽으면 개체 삭제
	}
	
	// 컬리젼 디텍션을 위한 함수
	public Rectangle collision() {
		return new Rectangle(getX(), getY(), width, height);
	}
	public Rectangle collisionTop() {
		return new Rectangle(getX(), getY(), width-20,5);
	}
	public Rectangle collisionBottom() {
		return new Rectangle(getX()+10, getY()+height-5, width-20, 5);
	}
	public Rectangle collisionLeft() {
		return new Rectangle(getX(), getY()+10, 5, height-20);
	}
	public Rectangle collisionRight() {
		return new Rectangle(getX()+width-5, getY()+10, 5, height-20);
	}

	// getters and setters
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
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getVelX() {
		return velX;
	}
	public void setVelX(int velX) {
		this.velX = velX;
	}
	public int getVelY() {
		return velY;
	}
	public void setVelY(int velY) {
		this.velY = velY;
	}
	public Id getId() {
		return id;
	}
}
