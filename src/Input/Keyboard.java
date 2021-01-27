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
	
	public static int keyCount;						// w 누른 횟수
	public static boolean doubleJump = false;		// 이중 점프한 횟수
   
   /*사용자가 키를 눌렀을 때*/
   public void keyPressed(KeyEvent e) {
      int code = e.getKeyCode(); 	//사용자가 누른 키보드의 문자를 가져옴
      
      //entity class에 있는 것들에 대하여
      for(int i=0; i< Main.controller.entity.size(); i++ ) {
         Entity en = Main.controller.entity.get(i);
         
         //entity가 player이면
         if(en.getId() == Id.player) {        	 
        	 // 파이프를 타고있을 때는 키보드 조작을 막는다.
        	 if(en.ridingPipe || en.dead)
        		 return;

        	 switch(code) {
        	 //키보드 W를 눌렀을 때 점프
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
           		 
            		 // 이중 점프
            		 if(doubleJump) {
            			 en.jumping = false;
            			 keyCount = 0;
            			 doubleJump = false;
            		 }
        		 }
        		        		 
        		 // 보너스 타임 시 다중 점프로 조작
        		 else {
            		 if(en.falling) {
            			 en.jumping = true;
            			 en.gravity = -15.0;
            		 }
        		 }
        		 break;

        	 //키보드 S를 눌렀을 때   
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

        	 //키보드 A를 눌렀을 때 왼쪽으로 움직임
        	 case KeyEvent.VK_A:
        		 en.setVelX(-6); 
        		 en.side = 0; //왼쪽 얼굴 
        		 break;

        	 //키보드 D를 눌렀을 때 오른쪽으로 움직임
        	 case KeyEvent.VK_D:
        		 en.setVelX(6);  
        		 en.side = 1; //오른쪽 얼굴
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
   
   /*사용자가 키를 뗄 때*/
   public void keyReleased(KeyEvent e) {
      int code = e.getKeyCode();
      
      for(int i=0; i<Main.controller.entity.size(); i++ ) {
         Entity en = Main.controller.entity.get(i);
         
         if(en.getId() == Id.player) {
            
            //w, a, d 키 떼면 캐릭터 속도=0 (멈춤)
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

   /*사용 안함*/
   public void keyTyped(KeyEvent e) {
      
   }
}