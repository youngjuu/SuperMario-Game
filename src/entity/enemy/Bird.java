package entity.enemy;

import java.awt.Graphics;

import brick.Brick;
import entity.Entity;
import entity.item.Poop;
import entity.item.PowerStar;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;
import mode.BossMode;

public class Bird extends Entity{

   private boolean poppedDown = false;
   
   private int poppedTime = 0; 			// 새똥 배출 시간  
   private int spriteY = getY();
   
   public Bird(int x, int y, int width, int height, Id id, Controller controller) {
      super(x, y, width, height, id, controller);
   }

   public void render(Graphics g) {
      if(side == 0)            // 왼쪽 면
         g.drawImage(Main.bird[frame].getBufferedImage(), x, y, width, height, null);
      else if (side == 1)         // 오른쪽 면
         g.drawImage(Main.bird[frame+4].getBufferedImage(), x, y, width, height, null);
   }

   public void update() {
      x += velX;
      y += velY;
      
      //3초마다 똥 배출     
      poppedTime++;    
      if (poppedTime >= 180)
    	  poppedDown = true;
      
      if(poppedDown) {
         spriteY++;
         controller.addEntity(new Poop(x, spriteY, width, height, Id.poop, controller));
         poppedDown = false;
         poppedTime = 0;
      }
      
      // 벽돌과 충돌
      for(int i=0; i<controller.brick.size(); i++) {
         Brick br = controller.brick.get(i);
         if(br.isBlock()) { 
            // 벽돌과 좌측이 닿으면 우측으로 움직인다.
            if(collisionLeft().intersects(br.collision())) {
               setVelX(2);
               side = 1;
               x = br.getX() + br.width;
            }

            // 벽돌과 우측이 닿으면 좌측으로 움직인다.
            if(collisionRight().intersects(br.collision())) {
               setVelX(-2);
               side = 0;
               x = br.getX() - br.width;
            }
         }
      }

      // 마리오의 움직임 감지
      for(int i=0; i<controller.entity.size(); i++) {
         Entity en = controller.entity.get(i);
         
         if(en.getId() == Id.player) {
            if(en.getX() < getX()) {
               setVelX(-2); 
               side = 0;
            }
            else if(en.getX() > getX()) {
               setVelX(2); //go right
               side = 1;
            }
         }
      }
      
      if(velX != 0) {
         frameDelay++;
         if(frameDelay>=10) {
            frame++;
            if(frame >= 3) {
               frame = 0;
            }
            frameDelay = 0;
         }
      }
   }

}