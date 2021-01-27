package entity.player;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import Input.Keyboard;
import brick.Brick;
import entity.Entity;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;
import mode.BossMode;
import mode.KoopaMode;
import mode.PlayerMode;

public class Player extends Entity {
	
	private Random random;
	
	private int pipeCount = 0;
	private int immortalTime = 0; 			// ��� ��� ���� �ð�
	private int firemodeTime = 0;			// ���� ��� ���� �ð�
	private int yoshiTime = 0;
	private int deadTime = 0;				// ���� �� ���� �ð�

	private boolean immortal = false; 		// ��� ����
	private boolean yoshi = false;
	
	public static PlayerMode mode = PlayerMode.normal;

	public Player(int x, int y, int width, int height, Id id, Controller controller) {
		super(x, y, width, height, id, controller);
		random = new Random();
	}

	// �÷��̾� �ִϸ��̼�
	public void render(Graphics g) {
		
		// �׾��� ��
		if(dead) {
			g.drawImage(Main.deadPlayer.getBufferedImage(), x, y, width, height, null);
		}
		
		// �Ŀ� ��Ÿ �Ծ��� ��
		else if(immortal) {
			if(side == 0) {         // ���� �̹��� ��������
				g.drawImage(Main.shinningPlayer[frame].getBufferedImage(), x, y, width, height, null);
			}
			else if(side == 1) {   // ������ �̹��� ��������
				g.drawImage(Main.shinningPlayer[frame+4].getBufferedImage(), x, y, width, height, null);
			}
		}
		
		// ���̾� �ö�� �Ծ��� ��
		else if(mode == PlayerMode.firemode) {
			  if(side == 0) {         // ���� �̹��� ��������
				  g.drawImage(Main.fireplayer[frame].getBufferedImage(), x, y, width, height, null);
			  }
			  else if(side == 1) {   // ������ �̹��� ��������
				  g.drawImage(Main.fireplayer[frame+4].getBufferedImage(), x, y, width, height, null);
			  }
		}
		
		// ��� �� �Ծ��� ��
		else if (mode == PlayerMode.ridingyoshi) {
			  if(side == 0) {
				  g.drawImage(Main.ridingyoshi[frame].getBufferedImage(), x, y, width, height, null);
			  }
			  else if(side == 1 ) {
				  g.drawImage(Main.ridingyoshi[frame+4].getBufferedImage(), x, y, width, height, null);
			  }
		}

		else {
			if(side == 0) {			// ���� �̹��� ��������
				g.drawImage(Main.player[frame+4].getBufferedImage(), x, y, width, height, null);
			} 
			else if(side == 1) {	// ������ �̹��� ��������
				g.drawImage(Main.player[frame].getBufferedImage(), x, y, width, height, null);
			}
		}
	}

	// �÷��̾� �̺�Ʈ
	public void update() {
		
		x += velX;
		y += velY;
		
		if(dead) {
			deadTime++;
			if(deadTime >= 180) {
				controller.killEntity(this); //������ ��ü ����
				Main.liveNum--;
				Main.showLiveScreen = true;
				Main.dieSound.play();
				if(Main.liveNum == 0) {
					Main.gameOver = true;  //��� 0�� �Ǹ� ���� ����ȭ��
					Main.dieSound.close();
					Main.gameOverSound.play();
				}
			}
		}
		
		// ���߿��� �ٴ����� ��������.
		if(!falling && !jumping) {
			falling = true;
			gravity = 0.8;
		}
		
		// ���ʽ� Ÿ�ӿ��� Ű���� ���� ���� �ڵ����� �����Ѵ�.
		if(Main.inBonusTime)
			setVelX(7);
	
		// ��� ���� �ð�
		if(immortal) {
			immortalTime++;
			
			// 10�� ���� ��� ��� ����
			if(immortalTime >= 600) {
				immortal = false;
				immortalTime = 0;
			}
		}
		
		// ���� ���� �ð�
		if(mode == PlayerMode.firemode) {
			firemodeTime++;
			
			// 10�� ���� ���� ��� ����
			if(firemodeTime >= 600) {
				mode = PlayerMode.normal;
				firemodeTime = 0;
			}
		}
		
		// ��� ���� �ð�
		if(yoshi) {
			yoshiTime++;
			mode = PlayerMode.ridingyoshi;
			if(side==1) {		//�������� ���� ��� 
				setVelX(10);
			}
			if(side==0) {		//������ ���� ��� 
				setVelX(-10);
			}
			if(yoshiTime >= 600) {
				mode = PlayerMode.normal;
				yoshi = false;
				yoshiTime = 0;
			}
		}
	    
		// ������ �浹
		for(int i=0; i<controller.brick.size(); i++) {

			Brick br = controller.brick.get(i);
			if(br.isBlock() && !ridingPipe) {
				
				if(collision().intersects(br.collision())) {
					if(br.getId() == Id.goal)
						Main.nextStage();
				}
				
				// ������ �Ӹ��� ������ ���̻� ���� �̵��� �Ұ����ϴ�.
				if(collisionTop().intersects(br.collision())) {
					setVelY(0);
					
					// ���� ���¿��ٸ� �ٽ� �߶�.
					if (jumping) {
						jumping = false;
						gravity = 0.8;
						falling = true;
					}
					
					// ���� �ڽ��� �浹
					if(br.getId() == Id.randombox) {
						if(collisionTop().intersects(br.collision())) {
							br.activate=true;
						}
					}
					// ��� �ڽ��� �浹
					else if(br.getId() == Id.applebox) {
						if(collisionTop().intersects(br.collision())) {
							br.activate=true;
						}
					}
					else if(br.getId() == Id.diebox) {
						if(immortal) {
							if(collision().intersects(br.collision())) {								
							}
						}
						else {
							if(collisionTop().intersects(br.collision())) {
								br.activate=true;
								die();
							}
						}
					}
				}
				
				// ������ ���� ������ ���̻� ������ �̵��� �Ұ����ϴ�.
				if(collisionBottom().intersects(br.collision())) {
					setVelY(0);

					// ���� ���� ���� ��ġ
					Keyboard.doubleJump = false;
					Keyboard.keyCount = 0;
				
					// �߶� ���¿��ٸ� �߶� ����
					if(falling)
						falling = false;					
				}
				
				// ������ ������ ������ �������� ƨ�� ������.
				else if(collisionLeft().intersects(br.collision())) {
					setVelX(0);
					x = br.getX() + br.width;
				}
				
				// ������ ������ ������ �������� ƨ�� ������.
				else if(collisionRight().intersects(br.collision())) {
					setVelX(0);
					x = br.getX()- br.width;
				}
			}
		}
		
		// ��ü�� �浹
		for(int i=0; i<controller.entity.size(); i++) {
			Entity en = controller.entity.get(i);
			
			// ���ٿ� �浹
			if(en.getId() == Id.goomba) {
				
				// �Ŀ� ��Ÿ ���� ����
				if(immortal) {		
					if(collision().intersects(en.collision())) {
					}
				}
				
				else {
					// ���� �Ӹ��� ���� ������ ���ٰ� �״´�.
					if(collisionBottom().intersects(en.collision())) {
						Main.stompSound.play();
						en.die();
					}
				
					// �÷��̾� ���¿� ���� ũ�Ⱑ �پ��ų� �״´�.
					else if(collision().intersects(en.collision())) {
	            		Main.bumpSound.play();
						
						if(mode == PlayerMode.supermode) {
							mode = PlayerMode.normal;
							width /= 2;
							height /= 2;
							x -= width;
						 	y -= height;
						}
		            	else if(mode == PlayerMode.normal)
		            		die();
		            	else if(mode == PlayerMode.firemode)
							mode = PlayerMode.normal;
		            	else if(mode == PlayerMode.ridingyoshi)
		            		en.die();
					}
				}
			}
			
			// �Ƕ�Ŀ� �浹
			else if(en.getId() == Id.piranha) {
				
				// �Ŀ� ��Ÿ ���� ����
				if(immortal) {		
					if(collision().intersects(en.collision())) {
					}
				}			
				else {
					if(collision().intersects(en.collision())) {
	            		Main.bumpSound.play();
						if(mode == PlayerMode.supermode) {
							mode = PlayerMode.normal;
							width /= 2;
							height /= 2;
							x -= width;
						 	y -= height;
						}
						else if(mode == PlayerMode.normal)
		            		die();
						else if(mode == PlayerMode.firemode)
							mode = PlayerMode.normal;
		            	else if(mode == PlayerMode.ridingyoshi) 
		            		en.die();		            	
					}
				}
			}
			
			// ���Ŀ� �浹
			else if(en.getId() == Id.koopa) {
				if(immortal) {
					if(collision().intersects(en.collision())) {
					}
				}
				else {					
					if(en.koopamode == KoopaMode.normal) {
						if(collisionBottom().intersects(en.collisionTop())) {
							en.koopamode = KoopaMode.shell;
							
							jumping = true;
							falling = false;
							gravity = 5;
						}
						else if(collision().intersects(en.collision())) {							
							die();
						}
					}
					else if(en.koopamode == KoopaMode.shell) {
						if(collisionBottom().intersects(en.collisionTop())) {
							en.koopamode = KoopaMode.spinning;
							
							int dir = random.nextInt(2);
							if(dir == 0) 
								en.setVelX(-10);
							else
								en.setVelX(10);

							
							jumping = true;
							falling = false;
							gravity = 3.5;
						}
						
						if(collisionLeft().intersects(en.collisionRight())) {
							en.setVelX(-10);
							en.koopamode = KoopaMode.spinning;
						}
						if(collisionRight().intersects(en.collisionLeft())) {
							en.setVelX(10);
							en.koopamode = KoopaMode.spinning;
						}
					}
					else if(en.koopamode == KoopaMode.spinning) {
						if(collisionBottom().intersects(en.collisionTop())) {
							en.koopamode = KoopaMode.shell;
							
							jumping = true;
							falling = false;
							gravity = 3.5;
						}
						else if(collision().intersects(en.collision()))
							die();
					}
				}			
			}
			
			
			
			// ���˰� �浹
			else if(en.getId() == id.poop) {
	            if(immortal) {
	               if(collisionTop().intersects(en.collision())) {

	               }
	            }
	            else {
	               if(collision().intersects(en.collision())) {
	                  en.die();
	                  if(mode==PlayerMode.ridingyoshi || mode==PlayerMode.supermode || mode==PlayerMode.firemode) {
	                	  mode = PlayerMode.normal;
	                  }
	                  else
	                	  die();
	               }
	            }
	         }
	         
			 // �����̴� Ÿ�ϰ� �浹
	         else if (en.getId()==Id.movingtile) {
	        	 if(immortal) {
	        		 if(collision().intersects(en.collision())){
	        		 }
	        	 }
	        	 else {
	        		 if(collision().intersects(en.collision())) {
	        			 die();
	        		 } 
	        	 }
	         }
			
			//������ �浹 
			else if(en.getId()==Id.boss) {  
				
				if(immortal) {   //�������� �Ŀ���Ÿ ���� �����̸� �������� �浹 ���� ����
	               if(collision().intersects(en.collision())) {
	               }
	            }//if

	            else {  //�Ŀ���Ÿ ���� �ʾ��� �� 
	               if(collisionBottom().intersects(en.collisionTop())) { //�������� ���� ������ �����Ҷ�
	            	   if(en.attackable){ //�������� ������ �����ϸ� ü��1���� �� ��ָ��� ����
	            		   System.out.println("�ݸ���");
	            		   en.bossMode = BossMode.weaken;
	            		   jumping = true;
	            		   falling = false;
	            		   gravity = 3;
	            		   en.attackable = false;
	            		   en.falling = true; 
	            		   en.gravity = 3.0;
	            		   en.stateTime = 0;
	            	   }
	               }
	               else {  //������ �浹�ϸ�
	            	   if(en.bossMode != BossMode.weaken) {
	 	                  if(collision().intersects(en.collision())) {
	 	                     System.out.println("�ȵ�");
	 	                     if(mode == PlayerMode.supermode) {  //ĳ���Ͱ� �Ǵٸ� ũ�� ����
	 	                        mode = PlayerMode.normal; 
	 	                        width /= 2;    
	 	                        height /= 2;
	 	                     }
	 	                     else if (mode == PlayerMode.normal)
	 	                    	 die(); //ĳ���Ͱ� �۾Ҵٸ� ����
	 	                  }
	            	   }
	               }

	            }//else

			}
			
			// ��Ʈ�� �浹
			if(en.getId() == id.heart) {
				if(immortal) {
					if(collision().intersects(en.collision())) {						
					}
				}
	            else {
	            	if(collision().intersects(en.collision())) {
	            		en.die();
	            		die();
	            	}
	            }
			}
			
			// ���� ȹ�� 
			else if(en.getId()==Id.mushroom) {
				// ������ ������ ũ�Ⱑ Ŀ����.
				if(collision().intersects(en.collision())) {
					int tpX = getX();
					int tpY = getY();
					width *= 2;
					height *= 2;
					setX(tpX - width);
					setY(tpY - height);
					
					if(mode == PlayerMode.normal) 
						mode =PlayerMode.supermode;
					
					en.die();
				}
			}
			
			// ���� ȹ��
			else if(en.getId() == id.coin) {
				if(collision().intersects(en.collision())) {
					Main.coinNum++;
					Main.coinSound.play();
					en.die();
				}
			}
			
			// �Ŀ� ��Ÿ ȹ��
			else if(en.getId() == id.star) {
				if(collision().intersects(en.collision())) {
					immortal = true;
					en.die(); 
				}
			}
	        
	        // ���̾� �ö�� ȹ�� 
			else if (en.getId() == Id.fireflower) {
				if(collision().intersects(en.collision())) {
					mode = PlayerMode.firemode;
					en.die();
				}
			}
			
			// ��� ��� ȹ��
			else if(en.getId() == id.liveapple) {
				if(collision().intersects(en.collision())) {
					Main.liveNum++;
					en.die(); 
				}
			}
			
			// ����� ȹ��
			else if(en.getId() == id.poisionapple) {
				if(collision().intersects(en.collision())) {
					die();
					en.die();
				}
			}
	        
	        // ��� �� ȹ��
			else if(en.getId() == id.yoshiegg) {
				
				 if(collision().intersects(en.collision())) {
					 yoshi = true;
	         		 en.die();
	         	 } 	 
			}
		}
		
		// ����
		if(jumping) {
			gravity -= 0.3;
			setVelY((int)-gravity);
			if(gravity <= 0.5) {
				jumping = false;
				falling = true;
			}
		}
		
		// �߶�
		if(falling && !ridingPipe) {
			if(!Main.inBonusTime) 
				gravity += 0.3;
			else {
				gravity += 0.5;
				
				// ���ʽ� Ÿ�ӿ��� �ʹ� ���� �������� �״´�.
				if(y >= 5000)
					die();
			}
			setVelY((int)gravity);
		}
		
		// �÷��̾ �����̰� ������
		if(velX != 0) {
			frameDelay++;
			if(dead) {
				if(frameDelay >= 100) {
					frame++;
				}
				if(frame>=4) {
					frame = 0;
				}
				frameDelay = 0;
			}
			else {
				if(frameDelay>=10) {
					frame++;
					// ���� �ִϸ��̼� �̹����� �������� ���ϴ� �̹����� �ٽ� ���������� ���ϴ� �̹����� ���ư���.
					if(frame>=3) {
						frame = 0;
					}
					frameDelay = 0;
				}
			}
		}
		
		// �������� Ÿ��
		if(ridingPipe) {
			for(int i=0; i<Main.controller.brick.size(); i++) {
				Brick br = Main.controller.brick.get(i);
				
				if(br.getId() == id.pipe) {
					
					// �������� �ٸ��� ������ �������� Ÿ�� ��������.
					if(collision().intersects(br.collision())) {
						setVelY(5);		// �������� Ÿ�� ��� �Ʒ��� ��������.
						setVelX(0);		// �������� Ÿ�� ������ �¿�� �������� �ʴ´�.
						pipeCount += velY;
					}
					// ���������� �� �������� ���̻� �������� �ʴ´�.
					if(pipeCount >= br.height+50) {
						ridingPipe = false;
						pipeCount= 0;
					}
				}
			}
		}
	}
}
