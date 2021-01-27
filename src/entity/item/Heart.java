package entity.item;

import java.awt.Graphics;

import brick.Brick;
import entity.Entity;
import entity.enemy.Peach;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class Heart extends Entity {

   public Heart(int x, int y, int width, int height, Id id, Controller controller, int side){
      super(x, y, width, height, id, controller);

      this.side = side;
      //��Ʈ �ӵ� 5
      switch (side) {
      case 0 : 
         setVelX(-5);
         break;
      case 1: 
         setVelX(5);
      }      
   }

public void render(Graphics g) {
      g.drawImage(Main.heart.getBufferedImage(), x, y, width, height, null);
   }

   public void update() {
      x += velX;

      //��Ʈ�� ������ ���鿡 ������ �����
      for(Brick br:controller.brick) {
         if (br.isBlock() && br.getId() == Id.brick) {
               if(collisionLeft().intersects(br.collision()) || collisionRight().intersects(br.collision())) die(); 
            }   
      }
   }
}