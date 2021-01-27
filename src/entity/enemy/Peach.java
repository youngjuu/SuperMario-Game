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
   private int shootedTime = 0; //��Ʈ ��� �ð�

   public Peach(int x, int y, int width, int height, Id id, Controller controller) {
      super(x, y, width, height, id, controller);

      // ���� ���� �� ��,�� �� ������ �������� �����δ�.
      int dir = random.nextInt(2);

      if(dir == 0) {
         setVelX(-2);
         side = 0;
      }
      else {
         setVelX(2);
         side = 1;
      }

      //���� ���� �� ��ġ �ٴ����� ������
      falling = true;
   }

   public void render(Graphics g) {
      if(side == 0)            // ���� ��
         g.drawImage(Main.peach[frame].getBufferedImage(), x, y, width, height, null);
      else if (side == 1)         // ������ ��
         g.drawImage(Main.peach[frame+4].getBufferedImage(), x, y, width, height, null);
   }

   public void update() {
      x += velX;
      y += velY;

      //5�ʸ��� ��Ʈ ���
      shootedTime++;      
      if(shootedTime >= 300)
         shooted = true;

      if(shooted) {      
         controller.addEntity(new Heart(x, y, width, height, Id.heart, controller, side));
         shooted = false;
         shootedTime = 0;
      }

      // ������ �浹
      for(int i=0; i<controller.brick.size(); i++) {
         Brick br = controller.brick.get(i);         
         if(br.isBlock()) {
            if(br.getId() == Id.brick) {

               // ������ ���� ������ ���̻� ������ �̵��� �Ұ����ϴ�.
               if(collisionBottom().intersects(br.collision())) {
                  setVelY(0);
                  // �߶� ���¿��ٸ� �߶� ����
                  if(falling) 
                     falling = false;
               }
               // ���� ���� �� ���ٰ� ���߿��� ��Ÿ�� �ٴ����� ��������.
               else if(!falling) {
                  falling = true;
                  gravity = 0.8;
               }

               // ������ ������ ������ �������� �����δ�.
               if(collisionLeft().intersects(br.collision())) {
                  setVelX(2);
                  side = 1;
                  x = br.getX() + br.width;  //��ġ ��ġ ����
               }

               // ������ ������ ������ �������� �����δ�.
               if(collisionRight().intersects(br.collision())) {
                  setVelX(-2);
                  side = 0;
                  x = br.getX() - br.width;   //��ġ ��ġ ����
               }
            }
         }
      }

      //��ġ�� �������� ��������  �����ϰ� �����δ�
      for(int i=0; i<controller.entity.size(); i++) {
         Entity en = controller.entity.get(i);
         if(en.getId() == Id.player) {

            //�������� ��ġ���� ���ʿ� ������ ��ġ �������� �̵� -> �ӷ��� �����̾�� �Ѵ� 
            if(en.getX() < getX() && getVelX()>0) {
               setVelX(-getVelX()); 
               side = 0;
            }
            //�������� ��ġ���� �����ʿ� ������ ��ġ ���������� �̵�  -> �ӷ��� ����̾�� �Ѵ�
            else if(en.getX() > getX() && getVelX()<0) {
               setVelX(-getVelX()); 
               side = 1;
            }
         }
      }

      // ��ġ�� �������� velY�� gravity ���� ��� ����.
      if(falling) {
         gravity += 0.1;
         setVelY((int)gravity);
      }

      // �¿�� �̵� �� ����, ������ ���̵� �̹����� �ٲ�� �Ѵ�.
      if(velX != 0) {
         frameDelay++;
         if(frameDelay>=10) {
            frame++;
            // ���� �ִϸ��̼� �̹����� �ݴ������� ���ϴ� �̹����� �ٽ� ���� ������ �̹����� ���ư���.
            if(frame >= 3) {
               frame = 0;
            }
            frameDelay = 0;
         }
      }
   }
}