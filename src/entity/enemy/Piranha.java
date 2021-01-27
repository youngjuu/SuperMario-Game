package entity.enemy;

import java.awt.Graphics;

import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Piranha extends Entity {
	
	private int lastTime;		// 파이프 안이나 바깥에서 존재하는 시간
	private int upDownCount;		// 파이프 안이나 바깥으로 나오는 크기
	
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
		
		// 5초마다 파이프 밖에 있는지 안에 있는지 체크
		if(lastTime >= 300) {
			if(outsideOfPipe)
				outsideOfPipe = false;
			else
				outsideOfPipe = true;
			
			idle = false;
			lastTime = 0;
		}
		
		// 안에 있으면 밖으로 나오고, 밖에 있으면 안으로 들어간다.
		if(!idle) {
			if(outsideOfPipe)
				setVelY(5);
			else
				setVelY(-5);
			
			upDownCount += velY;
			
			// 피라냐 높이보다 높게 튀어나오거나 깊이 들어가지 않게 조절
			if(upDownCount >= getHeight() || upDownCount <= -getHeight()) {
				upDownCount = 0;
				idle = true;
				setVelY(0);
			}
		}
	}
}
