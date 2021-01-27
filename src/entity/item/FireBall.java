package entity.item;

import java.awt.Graphics;

import brick.Brick;
import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class FireBall extends Entity{

	public FireBall(int x, int y, int width, int height, Id id, Controller controller, int side) {
		super(x, y, width, height, id, controller);
	
		this.side = side;
		switch(side) {
		case 0:
			setVelX(-8);
			break;
		case 1:
			setVelX(8);
			break;	
		}
	}

	public void render(Graphics g) {
		g.drawImage(Main.fireball.getBufferedImage(),getX(),getY(),getWidth(),getHeight(),null);
		
	}
	
	public void update() {
		x += velX;
		y += velY;
				
		for(int i =0; i<controller.brick.size();i++) {
			Brick br = controller.brick.get(i);
			if(br.isBlock()) {
				
				// 타일과 충돌하면 사라진다. 
				if(collisionLeft().intersects(br.collision()) || collisionRight().intersects(br.collision()))
					die();
				
				// 바닥과 충돌하면 튕겨 오른다.
				if(collisionBottom().intersects(br.collision())){
					jumping = true;
					falling = false;
					gravity = 4.0;
				}
				
				else if (!falling && !jumping) {
					falling =true;
					gravity=1.0;
				}
			}			
		}
		
		for(int i =0; i<controller.entity.size();i++) {
			Entity en = controller.entity.get(i);
			
			// 개체를 죽인다.
			if(en.getId()==Id.goomba || en.getId()==Id.koopa || en.getId()==Id.boss || en.getId()==Id.peach) {
				if(collision().intersects(en.collision())) {
					die();
					en.die();
				}
			}
		}
		
		if(jumping) {
			gravity-=0.3;
			setVelY((int)-gravity);
			if(gravity<=0.5) {
				jumping = false;
				falling = true;
			}
		}
		
		if(falling) {
			gravity+=0.3;
			setVelY((int)gravity);
		}		
	}
}