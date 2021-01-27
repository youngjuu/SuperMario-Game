package entity.enemy;

import java.awt.Graphics;
import java.util.Random;

import brick.Brick;
import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;
import mode.BossMode;

public class Boss extends Entity {
	
	private Random random;
	private int wait;
	
	public Boss(int x, int y, int width, int height, Id id, Controller controller, int hp) {
      super(x, y, width, height, id, controller);
      this.hp = hp;

      random = new Random();

      bossMode = BossMode.normal; 
	}

    public void render(Graphics g) {

      if(bossMode == BossMode.normal || bossMode == BossMode.dash || bossMode == BossMode.jump) {

         if (side == 0) //���� �� 
            g.drawImage(Main.boss[frame].getBufferedImage(), x, y, width, height, null );
         else if (side == 1) //������ �� 
            g.drawImage(Main.boss[frame+4].getBufferedImage(), x, y, width, height, null );   
      }

      else if (bossMode == BossMode.spin) {
         g.drawImage(Main.bossSpin[frame].getBufferedImage(), x, y, width, height, null );
      }

      else if (bossMode == BossMode.weaken) {
         g.drawImage(Main.bossWeaken.getBufferedImage(), x, y, width, height, null );
      }
    }


    public void update() {
      x += velX;
      y += velY;

      if(hp <= 0) 
    	  die(); //ü�� 0 �Ǹ� ���� ����

      stateTime++;

      //���� ��� 3�ʸ��� ��� ��ȯ
      if(stateTime >= 180) 
    	  chooseMode();

      //������尡 spin�̰ų� weak�� �� �������� ���� ����
      if(bossMode==BossMode.spin || bossMode==BossMode.weaken)
    	  attackable = false;
      else 
    	  attackable = true;

      //������尡 weaken ��尡 �Ǹ� ü�� 1����
      if (bossMode == BossMode.weaken) {
    	  wait++;
    	  
    	  setVelX(0);
    	  setVelY(0);
    	  
    	  System.out.println(hp);
    	  
    	  // 3�� �Ŀ� ��� ��ȭ
    	  if(stateTime >= 180) {
    		  chooseMode();
    		  stateTime = 0;
    	  }
    	  
    	  if(wait >= 180) {
    		  hp -= 1;
    		  wait = 0;
    	  }
      }

      // ������ �浹
      for(int i=0; i<controller.brick.size(); i++) {
         Brick br = controller.brick.get(i);
         if(br.isBlock()) {
            if(br.getId() == Id.brick) {

               //������ ������ �ؼ� �� ���κп� ������ �ٴ����� �������� 
               if(collisionTop().intersects(br.collision())) {
                  setVelY(0);
                  if (jumping) {
                     jumping = false;
                     gravity = 0.8;
                     falling = true;
                  }
               }

               // ������ ���� ������ ���̻� ������ �̵��� �Ұ����ϴ�.
               if(collisionBottom().intersects(br.collision())) {
                  setVelY(0);
                  // �߶� ���¿��ٸ� �߶� ����
                  if(falling) falling = false;
               } 

               // ������ ������ ������ �������� �����δ�.
               if(collisionLeft().intersects(br.collision())) {
                  setVelX(2);
                  side = 1;
                  x = br.getX() + br.width;  //���� ��ġ ����
               }

               // ������ ������ ������ �������� �����δ�.
               if(collisionRight().intersects(br.collision())) {
                  setVelX(-2);
                  side = 0;
                  x = br.getX() - br.width;   //���� ��ġ ����
               }
            }
         }
      }


      //������ �������� ��������  �����ϰ� �����δ�
      for(int i=0; i<controller.entity.size(); i++) {
         Entity en = controller.entity.get(i);
         if(en.getId() == Id.player) {

            //�������� �������� ���ʿ� ������ ���� �������� �̵� -> �ӷ��� �����̾�� �Ѵ� 
            if(en.getX() < getX() && getVelX()>0) {
               setVelX(-getVelX()); 
               side = 0;
            }
            //�������� �������� �����ʿ� ������ ���� ���������� �̵�  -> �ӷ��� ����̾�� �Ѵ�
            else if(en.getX() > getX() && getVelX()<0) {
               setVelX(-getVelX()); 
               side = 1;
            }
         }
      }

      //������ ������ ��
      if (jumping) {
         gravity -= 0.17;
         setVelY((int)-gravity);

         if(gravity <= 0.5) {
            jumping = false;
            falling = true;
         }
      }

      // ������ �������� velY�� gravity ���� ��� ����.
      if(falling) {
         gravity += 0.17;
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

   //���� ��� ���� ��ȭ: normal -> dash/jump/spin
   public void chooseMode() {
      int nextState = random.nextInt(3);
      //������ dash�����϶� �޸��� �ӷ�  5
      if(nextState == 0) {
         bossMode = BossMode.dash;

         int dir = random.nextInt(2);
         if(dir == 0) setVelX(-5);
         else setVelX(5);
      }

      //������ jump������ �� ����
      else if(nextState == 1) {
         bossMode = BossMode.jump;

         jumping = true;
         gravity = 8.0;
      }

      //������ spin ������ ��  ���� �ӷ� 3
      else if(nextState == 2) {
         bossMode = BossMode.spin;

         int dir = random.nextInt(2);
         if(dir == 0) setVelX(-3);
         else setVelX(3);

      }

      stateTime = 0;
   }
}