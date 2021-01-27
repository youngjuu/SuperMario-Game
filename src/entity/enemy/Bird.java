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
   
   private int poppedTime = 0; 			// ���� ���� �ð�  
   private int spriteY = getY();
   
   public Bird(int x, int y, int width, int height, Id id, Controller controller) {
      super(x, y, width, height, id, controller);
   }

   public void render(Graphics g) {
      if(side == 0)            // ���� ��
         g.drawImage(Main.bird[frame].getBufferedImage(), x, y, width, height, null);
      else if (side == 1)         // ������ ��
         g.drawImage(Main.bird[frame+4].getBufferedImage(), x, y, width, height, null);
   }

   public void update() {
      x += velX;
      y += velY;
      
      //3�ʸ��� �� ����     
      poppedTime++;    
      if (poppedTime >= 180)
    	  poppedDown = true;
      
      if(poppedDown) {
         spriteY++;
         controller.addEntity(new Poop(x, spriteY, width, height, Id.poop, controller));
         poppedDown = false;
         poppedTime = 0;
      }
      
      // ������ �浹
      for(int i=0; i<controller.brick.size(); i++) {
         Brick br = controller.brick.get(i);
         if(br.isBlock()) { 
            // ������ ������ ������ �������� �����δ�.
            if(collisionLeft().intersects(br.collision())) {
               setVelX(2);
               side = 1;
               x = br.getX() + br.width;
            }

            // ������ ������ ������ �������� �����δ�.
            if(collisionRight().intersects(br.collision())) {
               setVelX(-2);
               side = 0;
               x = br.getX() - br.width;
            }
         }
      }

      // �������� ������ ����
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