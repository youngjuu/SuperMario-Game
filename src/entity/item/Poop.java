package entity.item;

import java.awt.Graphics;

import brick.Brick;
import entity.Entity;
import entity.enemy.Bird;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Poop extends Entity {
   
   private int poopTime = 0; //¶ËÀÌ ¹Ù´Ú¿¡ ¸Ó¹«´Â ½Ã°£
   
   public Poop(int x, int y, int width, int height, Id id, Controller controller) {
      super(x, y, width, height, id, controller);

      setVelY(3); //¶Ë ¶³¾îÁü
   }

   public void render(Graphics g) {
      g.drawImage(Main.poop.getBufferedImage(), x, y, width, height, null);
   }

   public void update() {
      y += velY;

      //¶ËÀÌ¶û ¹Ù´Ú ÄÝ¸®Á¯ 
      for(Brick br:controller.brick) {
         if (br.isBlock()) {

            if(collisionBottom().intersects(br.collision())) {
               if(br.getId() == Id.brick) {
                  setVelY(0);
                  if(falling) falling = false;
                  
                  //¶ËÀÌ ¹Ù´Ú¿¡ ´ê°í 5ÃÊ ÈÄ »ç¶óÁü
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