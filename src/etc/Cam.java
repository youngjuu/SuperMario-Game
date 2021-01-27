package etc;

import entity.Entity;
import gameloop.Main;

public class Cam {
   public int x, y;
   
   /*카메라의 위치 업데이트*/
   //화면 전체 사이즈에서 플레이어의 좌표값을 빼면 화면이 플레이어의 움직에 따라 이동하게 됨  
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