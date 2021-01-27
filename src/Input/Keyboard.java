package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import brick.Brick;
import gameloop.Main;
import mode.PlayerMode;
import entity.Entity;
import entity.item.FireBall;
import entity.player.Player;
import etc.Id;

public class Keyboard implements KeyListener{
	
	public static int keyCount;						// w ���� Ƚ��
	public static boolean doubleJump = false;		// ���� ������ Ƚ��
   
   /*����ڰ� Ű�� ������ ��*/
   public void keyPressed(KeyEvent e) {
      int code = e.getKeyCode(); 	//����ڰ� ���� Ű������ ���ڸ� ������
      
      //entity class�� �ִ� �͵鿡 ���Ͽ�
      for(int i=0; i< Main.controller.entity.size(); i++ ) {
         Entity en = Main.controller.entity.get(i);
         
         //entity�� player�̸�
         if(en.getId() == Id.player) {        	 
        	 // �������� Ÿ������ ���� Ű���� ������ ���´�.
        	 if(en.ridingPipe || en.dead)
        		 return;

        	 switch(code) {
        	 //Ű���� W�� ������ �� ����
        	 case KeyEvent.VK_W: 
        		 if(Player.mode == PlayerMode.normal)
        			 Main.jumpSound_small.play();
        		 else if(Player.mode == PlayerMode.supermode)
        			 Main.jumpSound_big.play();
        		 
        		 if(!Main.inBonusTime) {
        			 en.jumping = true;
         			 en.gravity = 12.0; 
        			 keyCount++;
        			 
            		 if(keyCount>=2) {
            			 doubleJump = true;
            		 }
           		 
            		 // ���� ����
            		 if(doubleJump) {
            			 en.jumping = false;
            			 keyCount = 0;
            			 doubleJump = false;
            		 }
        		 }
        		        		 
        		 // ���ʽ� Ÿ�� �� ���� ������ ����
        		 else {
            		 if(en.falling) {
            			 en.jumping = true;
            			 en.gravity = -15.0;
            		 }
        		 }
        		 break;

        	 //Ű���� S�� ������ ��   
        	 case KeyEvent.VK_S:
        		 for(int j=0; j<Main.controller.brick.size();j++) {
        			 Brick br = Main.controller.brick.get(j);

        			 if(br.getId() == Id.pipe) {
        				 if(en.collisionBottom().intersects(br.collision())) {
        					 if(!en.ridingPipe) {
        						 en.ridingPipe = true;
        						 Main.pipeSound.play();
        					 }
        				 }
        			 }
        		 }
        		 break;

        	 //Ű���� A�� ������ �� �������� ������
        	 case KeyEvent.VK_A:
        		 en.setVelX(-6); 
        		 en.side = 0; //���� �� 
        		 break;

        	 //Ű���� D�� ������ �� ���������� ������
        	 case KeyEvent.VK_D:
        		 en.setVelX(6);  
        		 en.side = 1; //������ ��
        		 break;
        		 
        	 case KeyEvent.VK_SPACE:
        		 if(Player.mode != PlayerMode.firemode)
        			 continue;
        		 switch(en.side) {
        		 case 0:
        			 Main.controller.addEntity(new FireBall(en.getX()-24, en.getY()+12, 24, 24, Id.fireball, Main.controller, en.side));
        			 break;
        		 case 1:
        			 Main.controller.addEntity(new FireBall(en.getX()+en.getWidth(), en.getY()+12, 24, 24, Id.fireball, Main.controller, en.side));
        			 break;
        		 }     		 
        	 }      
         }
      }
   }
   
   /*����ڰ� Ű�� �� ��*/
   public void keyReleased(KeyEvent e) {
      int code = e.getKeyCode();
      
      for(int i=0; i<Main.controller.entity.size(); i++ ) {
         Entity en = Main.controller.entity.get(i);
         
         if(en.getId() == Id.player) {
            
            //w, a, d Ű ���� ĳ���� �ӵ�=0 (����)
            switch(code) {      
            case KeyEvent.VK_W:
               en.setVelY(0);
               break;
               
            case KeyEvent.VK_A:   
               en.setVelX(0);
               break;
               
            case KeyEvent.VK_D:
               en.setVelX(0);
               break;
            }
         }
      }
   }

   /*��� ����*/
   public void keyTyped(KeyEvent e) {
      
   }
}