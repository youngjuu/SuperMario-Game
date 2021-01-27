package entity.enemy;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import brick.Brick;
import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;
import mode.KoopaMode;

public class Koopa extends Entity {	
	
	private Random random = new Random();
	private int shellCount;

	public Koopa(int x, int y, int width, int height, Id id, Controller controller) {
		super(x, y, width, height, id, controller);
		
		int dir = random.nextInt(2);
		if(dir == 0) {
			setVelX(-2);
			side = 0;		
		}
		else {
			setVelX(2);
			side = 1;
		}
		koopamode = KoopaMode.normal;
	}

	public void render(Graphics g) {
		if(koopamode == KoopaMode.normal) {
			if(side == 0)
				g.drawImage(Main.koopa[frame+4].getBufferedImage(), x, y, width, height, null);
			else if(side == 1)
				g.drawImage(Main.koopa[frame].getBufferedImage(), x, y, width, height, null);
		}
		else
			g.drawImage(Main.koopashell.getBufferedImage(), x, y, width, height, null);
	}

	public void update() {		
		x += velX;
		y += velY;
		
		if(koopamode == KoopaMode.shell) {
			setVelX(0);
			
			shellCount++;
			if(shellCount >= 300) {
				shellCount = 0;
				koopamode = KoopaMode.normal;
			}			
		}
		
		if(koopamode == KoopaMode.normal || koopamode == KoopaMode.spinning) {
			shellCount = 0;
			
			if(velX == 0) {
				
				int dir = random.nextInt(2);				
				if(dir == 0) 
					setVelX(-2);		
				else 
					setVelX(2);				
			}
		}
		
		// ������ �浹
		for(int i=0; i<controller.brick.size(); i++) {
			Brick br = controller.brick.get(i);			
			if(br.isBlock()) {
				
				// ������ ���� ������ ���̻� ������ �̵��� �Ұ����ϴ�.
				if(collisionBottom().intersects(br.collision())) {
					setVelY(0);
					// �߶� ���¿��ٸ� �߶� ����
					if(falling) 
						falling = false;
				} 
				// ���� ���� �� ���ٰ� ���߿��� ��Ÿ�� �ٴ����� ��������.
				else if(!falling) {
					falling = true;
					gravity = 0.8;
				}
				
				// ������ ������ ������ �������� �����δ�.
				if(collisionLeft().intersects(br.collision())) {
					if(koopamode == KoopaMode.spinning)
						setVelX(10);
					else
						setVelX(2);
				}
				
				// ������ ������ ������ �������� �����δ�.
				if(collisionRight().intersects(br.collision())) {
					if(koopamode == KoopaMode.spinning)
						setVelX(-10);
					else
						setVelX(-2);
				}
			}
		}
		
		// ��ü�� �浹
		for(int i=0; i<controller.entity.size(); i++) {
			Entity en = controller.entity.get(i);			

			if(koopamode == KoopaMode.spinning) {
				if(en.getId()==Id.goomba) {
					if(collision().intersects(en.collision())) {
						en.die();
					}
				}
			}
		}
		
		// ���İ� �������� velY�� gravity ���� ��� ����.
		if(falling) {
			gravity += 0.1;
			setVelY((int)gravity);
		}	
	}
}
