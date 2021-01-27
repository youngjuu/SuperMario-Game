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
	      
	      // ������ �浹
	      for(int i=0; i<controller.brick.size(); i++) {
	         Brick br = controller.brick.get(i);         
	         if(br.isBlock()) {
	            
	            // ������ ���� ������ ���̻� ������ �̵��� �Ұ����ϴ�.
	            if(collisionBottom().intersects(br.collision())) {
	               setVelY(0);
	               // �߶� ���¿��ٸ� �߶� ����
	            }	            
	            // ������ ������ ������ �������� �����δ�.
	            if(collisionLeft().intersects(br.collision())) {
	               setVelX(10);
	               side = 1;
	            }	            
	            // ������ ������ ������ �������� �����δ�.
	            if(collisionRight().intersects(br.collision())) {
	               setVelX(-10);
	               side = 0;
	            }
	         }
	      }	
	}
}