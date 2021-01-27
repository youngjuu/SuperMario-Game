package entity.item;

import java.awt.Graphics;

import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;

public class PowerStar extends Entity{

   public PowerStar(int x, int y, int width, int height, Id id, Controller controller) {
      super(x, y, width, height, id, controller);
      setVelX(0);
   }

   public void render(Graphics g) {
      g.drawImage(Main.star.getBufferedImage(), getX(), getY(), getWidth(), getHeight(), null);
   }

   public void update() {

   }

}