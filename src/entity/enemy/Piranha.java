package entity.enemy;

import java.awt.Graphics;

import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Piranha extends Entity {
	
	private int lastTime;		// ������ ���̳� �ٱ����� �����ϴ� �ð�
	private int upDownCount;		// ������ ���̳� �ٱ����� ������ ũ��
	
	private boolean outsideOfPipe = false;
	private boolean idle = true;

	public Piranha(int x, int y, int width, int height, Id id, Controller controller) {
		super(x, y, width, height, id, controller);

	}

	public void render(Graphics g) {
		g.drawImage(Main.piranha.getBufferedImage(), x, y, width, height, null);
	}
	
	

	public void update() {
		y += velY;
		
		if(idle)
			lastTime++;
		
		// 5�ʸ��� ������ �ۿ� �ִ��� �ȿ� �ִ��� üũ
		if(lastTime >= 300) {
			if(outsideOfPipe)
				outsideOfPipe = false;
			else
				outsideOfPipe = true;
			
			idle = false;
			lastTime = 0;
		}
		
		// �ȿ� ������ ������ ������, �ۿ� ������ ������ ����.
		if(!idle) {
			if(outsideOfPipe)
				setVelY(5);
			else
				setVelY(-5);
			
			upDownCount += velY;
			
			// �Ƕ�� ���̺��� ���� Ƣ����ų� ���� ���� �ʰ� ����
			if(upDownCount >= getHeight() || upDownCount <= -getHeight()) {
				upDownCount = 0;
				idle = true;
				setVelY(0);
			}
		}
	}
}
