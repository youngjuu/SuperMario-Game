package etc;

import entity.Entity;
import gameloop.Main;

public class Cam {
   public int x, y;
   
   /*ī�޶��� ��ġ ������Ʈ*/
   //ȭ�� ��ü ������� �÷��̾��� ��ǥ���� ���� ȭ���� �÷��̾��� ������ ���� �̵��ϰ� ��  
   public void update(Entity player) {
      setX(Main.WIDTH - player.getX());   
      setY(Main.HEIGHT - player.getY());
   }

   public int getX() {
      return x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(int y) {
      this.y = y;
   }
}