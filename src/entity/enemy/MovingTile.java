package entity.enemy;

import java.awt.Graphics;
import java.util.Random;
import brick.Brick;
import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class MovingTile extends Entity{
	
	public MovingTile(int x, int y, int width, int height, Id id, Controller controller) {
		super(x, y, width, height, id, controller);
		setVelX(-10);	
	}

	public void render(Graphics g) {
		g.drawImage(Main.movingtile.getBufferedImage(), x, y, width, height, null);	
	}

	public void update() {
		  x += velX;
	      y += velY;
	      
	      // 벽돌과 충돌
	      for(int i=0; i<controller.brick.size(); i++) {
	         Brick br = controller.brick.get(i);         
	         if(br.isBlock()) {
	            
	            // 벽돌과 발이 닿으면 더이상 밑으로 이동이 불가능하다.
	            if(collisionBottom().intersects(br.collision())) {
	               setVelY(0);
	               // 추락 상태였다면 추락 멈춤
	            }	            
	            // 벽돌과 좌측이 닿으면 우측으로 움직인다.
	            if(collisionLeft().intersects(br.collision())) {
	               setVelX(10);
	               side = 1;
	            }	            
	            // 벽돌과 우측이 닿으면 좌측으로 움직인다.
	            if(collisionRight().intersects(br.collision())) {
	               setVelX(-10);
	               side = 0;
	            }
	         }
	      }	
	}
}