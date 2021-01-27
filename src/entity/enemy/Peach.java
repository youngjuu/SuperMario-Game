package entity.enemy;

import java.awt.Graphics;
import java.util.Random;

import brick.Brick;
import entity.Entity;
import entity.item.Heart;
import entity.item.Poop;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Peach extends Entity{

   private Random random = new Random();
   private boolean shooted = false;
   private int shootedTime = 0; //하트 쏘는 시간

   public Peach(int x, int y, int width, int height, Id id, Controller controller) {
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

      //게임 실행 시 피치 바닥으로 떨어짐
      falling = true;
   }

   public void render(Graphics g) {
      if(side == 0)            // 왼쪽 면
         g.drawImage(Main.peach[frame].getBufferedImage(), x, y, width, height, null);
      else if (side == 1)         // 오른쪽 면
         g.drawImage(Main.peach[frame+4].getBufferedImage(), x, y, width, height, null);
   }

   public void update() {
      x += velX;
      y += velY;

      //5초마다 하트 쏘기
      shootedTime++;      
      if(shootedTime >= 300)
         shooted = true;

      if(shooted) {      
         controller.addEntity(new Heart(x, y, width, height, Id.heart, controller, side));
         shooted = false;
         shootedTime = 0;
      }

      // 벽돌과 충돌
      for(int i=0; i<controller.brick.size(); i++) {
         Brick br = controller.brick.get(i);         
         if(br.isBlock()) {
            if(br.getId() == Id.brick) {

               // 벽돌과 발이 닿으면 더이상 밑으로 이동이 불가능하다.
               if(collisionBottom().intersects(br.collision())) {
                  setVelY(0);
                  // 추락 상태였다면 추락 멈춤
                  if(falling) 
                     falling = false;
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
                  x = br.getX() + br.width;  //피치 위치 고정
               }

               // 벽돌과 우측이 닿으면 좌측으로 움직인다.
               if(collisionRight().intersects(br.collision())) {
                  setVelX(-2);
                  side = 0;
                  x = br.getX() - br.width;   //피치 위치 고정
               }
            }
         }
      }

      //피치는 마리오의 움직임을  감지하고 움직인다
      for(int i=0; i<controller.entity.size(); i++) {
         Entity en = controller.entity.get(i);
         if(en.getId() == Id.player) {

            //마리오가 피치보다 왼쪽에 있으면 피치 왼쪽으로 이동 -> 속력이 음수이어야 한다 
            if(en.getX() < getX() && getVelX()>0) {
               setVelX(-getVelX()); 
               side = 0;
            }
            //마리오가 피치보다 오른쪽에 있으면 피치 오른쪽으로 이동  -> 속력이 양수이어야 한다
            else if(en.getX() > getX() && getVelX()<0) {
               setVelX(-getVelX()); 
               side = 1;
            }
         }
      }

      // 피치가 떨어지면 velY에 gravity 값을 계속 더함.
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