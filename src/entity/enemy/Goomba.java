package entity.enemy;

import java.awt.Graphics;
import java.util.Random;

import brick.Brick;
import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Goomba extends Entity {
	
	private Random random = new Random();

	public Goomba(int x, int y, int width, int height, Id id, Controller controller) {
		super(x, y, width, height, id, controller);
		
		// ���� ���� �� ��,�� �� ������ �������� �����δ�.
		int dir = random.nextInt(2);		
		if(dir == 0) {
			setVelX(-2);
			side = 0;
		}
		else {
			setVelX(2);
			side = 1;
		}
	}

	// ���� �ִϸ��̼�
	public void render(Graphics g) {
		if(side == 0)				// ���� ��
			g.drawImage(Main.goomba[frame+4].getBufferedImage(), x, y, width, height, null);
		else if (side == 1)			// ������ ��
			g.drawImage(Main.goomba[frame].getBufferedImage(), x, y, width, height, null);
		
	}

	// ���� �̺�Ʈ
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
					if(falling) falling = false;
				} 
				// ���� ���� �� ���ٰ� ���߿��� ��Ÿ�� �ٴ����� ��������.
				else if(!falling) {
					falling = true;
					gravity = 0.8;
				}
				
				// ������ ������ ������ �������� �����δ�.
				if(collisionLeft().intersects(br.collision())) {
					setVelX(2);
					side = 1;
				}
				
				// ������ ������ ������ �������� �����δ�.
				if(collisionRight().intersects(br.collision())) {
					setVelX(-2);
					side = 0;
				}
			}
		}
		
		// ���ٰ� �������� velY�� gravity ���� ��� ����.
		if(falling) {
			gravity += 0.1;
			setVelY((int)gravity);
		}
		
		// �¿�� �̵� �� ����, ������ ���̵� �̹����� �ٲ�� �Ѵ�.
		if(velX != 0) {
			frameDelay++;
			if(frameDelay>=10) {
				frame++;
				// ���� �ִϸ��̼� �̹����� �ݴ������� ���ϴ� �̹����� �ٽ� ���� ������ �̹����� ���ư���.
				if(frame >= 3) {
					frame = 0;
				}
				frameDelay = 0;
			}
		}
		
	}

}
