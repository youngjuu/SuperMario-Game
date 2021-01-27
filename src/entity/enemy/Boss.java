package entity.enemy;

import java.awt.Graphics;
import java.util.Random;

import brick.Brick;
import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;
import mode.BossMode;

public class Boss extends Entity {
	
	private Random random;
	private int wait;
	
	public Boss(int x, int y, int width, int height, Id id, Controller controller, int hp) {
      super(x, y, width, height, id, controller);
      this.hp = hp;

      random = new Random();

      bossMode = BossMode.normal; 
	}

    public void render(Graphics g) {

      if(bossMode == BossMode.normal || bossMode == BossMode.dash || bossMode == BossMode.jump) {

         if (side == 0) //왼쪽 면 
            g.drawImage(Main.boss[frame].getBufferedImage(), x, y, width, height, null );
         else if (side == 1) //오른쪽 면 
            g.drawImage(Main.boss[frame+4].getBufferedImage(), x, y, width, height, null );   
      }

      else if (bossMode == BossMode.spin) {
         g.drawImage(Main.bossSpin[frame].getBufferedImage(), x, y, width, height, null );
      }

      else if (bossMode == BossMode.weaken) {
         g.drawImage(Main.bossWeaken.getBufferedImage(), x, y, width, height, null );
      }
    }


    public void update() {
      x += velX;
      y += velY;

      if(hp <= 0) 
    	  die(); //체력 0 되면 보스 죽음

      stateTime++;

      //보스 모드 3초마다 모드 변환
      if(stateTime >= 180) 
    	  chooseMode();

      //보스모드가 spin이거나 weak일 때 마리오는 공격 못함
      if(bossMode==BossMode.spin || bossMode==BossMode.weaken)
    	  attackable = false;
      else 
    	  attackable = true;

      //보스모드가 weaken 모드가 되면 체력 1감소
      if (bossMode == BossMode.weaken) {
    	  wait++;
    	  
    	  setVelX(0);
    	  setVelY(0);
    	  
    	  System.out.println(hp);
    	  
    	  // 3초 후에 모드 변화
    	  if(stateTime >= 180) {
    		  chooseMode();
    		  stateTime = 0;
    	  }
    	  
    	  if(wait >= 180) {
    		  hp -= 1;
    		  wait = 0;
    	  }
      }

      // 벽돌과 충돌
      for(int i=0; i<controller.brick.size(); i++) {
         Brick br = controller.brick.get(i);
         if(br.isBlock()) {
            if(br.getId() == Id.brick) {

               //보스가 점프를 해서 벽 윗부분에 닿으면 바닥으로 떨어진다 
               if(collisionTop().intersects(br.collision())) {
                  setVelY(0);
                  if (jumping) {
                     jumping = false;
                     gravity = 0.8;
                     falling = true;
                  }
               }

               // 벽돌과 발이 닿으면 더이상 밑으로 이동이 불가능하다.
               if(collisionBottom().intersects(br.collision())) {
                  setVelY(0);
                  // 추락 상태였다면 추락 멈춤
                  if(falling) falling = false;
               } 

               // 벽돌과 좌측이 닿으면 우측으로 움직인다.
               if(collisionLeft().intersects(br.collision())) {
                  setVelX(2);
                  side = 1;
                  x = br.getX() + br.width;  //보스 위치 고정
               }

               // 벽돌과 우측이 닿으면 좌측으로 움직인다.
               if(collisionRight().intersects(br.collision())) {
                  setVelX(-2);
                  side = 0;
                  x = br.getX() - br.width;   //보스 위치 고정
               }
            }
         }
      }


      //보스는 마리오의 움직임을  감지하고 움직인다
      for(int i=0; i<controller.entity.size(); i++) {
         Entity en = controller.entity.get(i);
         if(en.getId() == Id.player) {

            //마리오가 보스보다 왼쪽에 있으면 보스 왼쪽으로 이동 -> 속력이 음수이어야 한다 
            if(en.getX() < getX() && getVelX()>0) {
               setVelX(-getVelX()); 
               side = 0;
            }
            //마리오가 보스보다 오른쪽에 있으면 보스 오른쪽으로 이동  -> 속력이 양수이어야 한다
            else if(en.getX() > getX() && getVelX()<0) {
               setVelX(-getVelX()); 
               side = 1;
            }
         }
      }

      //보스가 점프할 때
      if (jumping) {
         gravity -= 0.17;
         setVelY((int)-gravity);

         if(gravity <= 0.5) {
            jumping = false;
            falling = true;
         }
      }

      // 보스가 떨어지면 velY에 gravity 값을 계속 더함.
      if(falling) {
         gravity += 0.17;
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

   //보스 모드 랜덤 변화: normal -> dash/jump/spin
   public void chooseMode() {
      int nextState = random.nextInt(3);
      //보스가 dash상태일때 달리기 속력  5
      if(nextState == 0) {
         bossMode = BossMode.dash;

         int dir = random.nextInt(2);
         if(dir == 0) setVelX(-5);
         else setVelX(5);
      }

      //보스가 jump상태일 때 점프
      else if(nextState == 1) {
         bossMode = BossMode.jump;

         jumping = true;
         gravity = 8.0;
      }

      //보스가 spin 상태일 때  스핀 속력 3
      else if(nextState == 2) {
         bossMode = BossMode.spin;

         int dir = random.nextInt(2);
         if(dir == 0) setVelX(-3);
         else setVelX(3);

      }

      stateTime = 0;
   }
}