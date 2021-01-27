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
		
		// 게임 실행 시 좌,우 중 랜덤한 방향으로 움직인다.
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

	// 굼바 애니메이션
	public void render(Graphics g) {
		if(side == 0)				// 왼쪽 면
			g.drawImage(Main.goomba[frame+4].getBufferedImage(), x, y, width, height, null);
		else if (side == 1)			// 오른쪽 면
			g.drawImage(Main.goomba[frame].getBufferedImage(), x, y, width, height, null);
		
	}

	// 굼바 이벤트
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
					if(falling) falling = false;
				} 
				// 게임 실행 시 굼바가 공중에서 나타나 바닥으로 떨어진다.
				else if(!falling) {
					falling = true;
					gravity = 0.8;
				}
				
				// 벽돌과 좌측이 닿으면 우측으로 움직인다.
				if(collisionLeft().intersects(br.collision())) {
					setVelX(2);
					side = 1;
				}
				
				// 벽돌과 우측이 닿으면 좌측으로 움직인다.
				if(collisionRight().intersects(br.collision())) {
					setVelX(-2);
					side = 0;
				}
			}
		}
		
		// 굼바가 떨어지면 velY에 gravity 값을 계속 더함.
		if(falling) {
			gravity += 0.1;
			setVelY((int)gravity);
		}
		
		// 좌우로 이동 시 왼쪽, 오른쪽 사이드 이미지도 바꿔야 한다.
		if(velX != 0) {
			frameDelay++;
			if(frameDelay>=10) {
				frame++;
				// 다음 애니메이션 이미지가 반대쪽으로 향하는 이미지면 다시 원래 방향의 이미지로 돌아간다.
				if(frame >= 3) {
					frame = 0;
				}
				frameDelay = 0;
			}
		}
		
	}

}
