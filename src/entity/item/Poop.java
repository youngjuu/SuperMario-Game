package entity.item;

import java.awt.Graphics;

import brick.Brick;
import entity.Entity;
import entity.enemy.Bird;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Poop extends Entity {
   
   private int poopTime = 0; //���� �ٴڿ� �ӹ��� �ð�
   
   public Poop(int x, int y, int width, int height, Id id, Controller controller) {
      super(x, y, width, height, id, controller);

      setVelY(3); //�� ������
   }

   public void render(Graphics g) {
      g.drawImage(Main.poop.getBufferedImage(), x, y, width, height, null);
   }

   public void update() {
      y += velY;

      //���̶� �ٴ� �ݸ��� 
      for(Brick br:controller.brick) {
         if (br.isBlock()) {

            if(collisionBottom().intersects(br.collision())) {
               if(br.getId() == Id.brick) {
                  setVelY(0);
                  if(falling) falling = false;
                  
                  //���� �ٴڿ� ��� 5�� �� �����
                  poopTime++;
                  if(poopTime >= 300) {
                     die();
                  }
               }
            } 

         }
      }//for
      
   }

}