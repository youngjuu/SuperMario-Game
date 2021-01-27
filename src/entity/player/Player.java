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
	private int immortalTime = 0; 			// 방어 모드 지속 시간
	private int firemodeTime = 0;			// 공격 모드 지속 시간
	private int yoshiTime = 0;
	private int deadTime = 0;				// 죽은 후 지속 시간

	private boolean immortal = false; 		// 방어 상태
	private boolean yoshi = false;
	
	public static PlayerMode mode = PlayerMode.normal;

	public Player(int x, int y, int width, int height, Id id, Controller controller) {
		super(x, y, width, height, id, controller);
		random = new Random();
	}

	// 플레이어 애니메이션
	public void render(Graphics g) {
		
		// 죽었을 때
		if(dead) {
			g.drawImage(Main.deadPlayer.getBufferedImage(), x, y, width, height, null);
		}
		
		// 파워 스타 먹었을 때
		else if(immortal) {
			if(side == 0) {         // 왼쪽 이미지 가져오기
				g.drawImage(Main.shinningPlayer[frame].getBufferedImage(), x, y, width, height, null);
			}
			else if(side == 1) {   // 오른쪽 이미지 가져오기
				g.drawImage(Main.shinningPlayer[frame+4].getBufferedImage(), x, y, width, height, null);
			}
		}
		
		// 파이어 플라워 먹었을 때
		else if(mode == PlayerMode.firemode) {
			  if(side == 0) {         // 왼쪽 이미지 가져오기
				  g.drawImage(Main.fireplayer[frame].getBufferedImage(), x, y, width, height, null);
			  }
			  else if(side == 1) {   // 오른쪽 이미지 가져오기
				  g.drawImage(Main.fireplayer[frame+4].getBufferedImage(), x, y, width, height, null);
			  }
		}
		
		// 요시 알 먹었을 때
		else if (mode == PlayerMode.ridingyoshi) {
			  if(side == 0) {
				  g.drawImage(Main.ridingyoshi[frame].getBufferedImage(), x, y, width, height, null);
			  }
			  else if(side == 1 ) {
				  g.drawImage(Main.ridingyoshi[frame+4].getBufferedImage(), x, y, width, height, null);
			  }
		}

		else {
			if(side == 0) {			// 왼쪽 이미지 가져오기
				g.drawImage(Main.player[frame+4].getBufferedImage(), x, y, width, height, null);
			} 
			else if(side == 1) {	// 오른쪽 이미지 가져오기
				g.drawImage(Main.player[frame].getBufferedImage(), x, y, width, height, null);
			}
		}
	}

	// 플레이어 이벤트
	public void update() {
		
		x += velX;
		y += velY;
		
		if(dead) {
			deadTime++;
			if(deadTime >= 180) {
				controller.killEntity(this); //죽으면 개체 삭제
				Main.liveNum--;
				Main.showLiveScreen = true;
				Main.dieSound.play();
				if(Main.liveNum == 0) {
					Main.gameOver = true;  //목숨 0개 되면 게임 종료화면
					Main.dieSound.close();
					Main.gameOverSound.play();
				}
			}
		}
		
		// 공중에서 바닥으로 떨어진다.
		if(!falling && !jumping) {
			falling = true;
			gravity = 0.8;
		}
		
		// 보너스 타임에는 키보드 조작 없이 자동으로 전진한다.
		if(Main.inBonusTime)
			setVelX(7);
	
		// 방어 지속 시간
		if(immortal) {
			immortalTime++;
			
			// 10초 동안 방어 모드 지속
			if(immortalTime >= 600) {
				immortal = false;
				immortalTime = 0;
			}
		}
		
		// 공격 지속 시간
		if(mode == PlayerMode.firemode) {
			firemodeTime++;
			
			// 10초 동안 공격 모드 지속
			if(firemodeTime >= 600) {
				mode = PlayerMode.normal;
				firemodeTime = 0;
			}
		}
		
		// 요시 지속 시간
		if(yoshi) {
			yoshiTime++;
			mode = PlayerMode.ridingyoshi;
			if(side==1) {		//오른쪽을 보는 경우 
				setVelX(10);
			}
			if(side==0) {		//왼쪽을 보는 경우 
				setVelX(-10);
			}
			if(yoshiTime >= 600) {
				mode = PlayerMode.normal;
				yoshi = false;
				yoshiTime = 0;
			}
		}
	    
		// 벽돌과 충돌
		for(int i=0; i<controller.brick.size(); i++) {

			Brick br = controller.brick.get(i);
			if(br.isBlock() && !ridingPipe) {
				
				if(collision().intersects(br.collision())) {
					if(br.getId() == Id.goal)
						Main.nextStage();
				}
				
				// 벽돌과 머리가 닿으면 더이상 위로 이동이 불가능하다.
				if(collisionTop().intersects(br.collision())) {
					setVelY(0);
					
					// 점프 상태였다면 다시 추락.
					if (jumping) {
						jumping = false;
						gravity = 0.8;
						falling = true;
					}
					
					// 랜덤 박스와 충돌
					if(br.getId() == Id.randombox) {
						if(collisionTop().intersects(br.collision())) {
							br.activate=true;
						}
					}
					// 사과 박스와 충돌
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
				
				// 벽돌과 발이 닿으면 더이상 밑으로 이동이 불가능하다.
				if(collisionBottom().intersects(br.collision())) {
					setVelY(0);

					// 이중 점프 구현 장치
					Keyboard.doubleJump = false;
					Keyboard.keyCount = 0;
				
					// 추락 상태였다면 추락 멈춤
					if(falling)
						falling = false;					
				}
				
				// 벽돌과 좌측이 닿으면 우측으로 튕겨 나간다.
				else if(collisionLeft().intersects(br.collision())) {
					setVelX(0);
					x = br.getX() + br.width;
				}
				
				// 벽돌과 우측이 닿으면 좌측으로 튕겨 나간다.
				else if(collisionRight().intersects(br.collision())) {
					setVelX(0);
					x = br.getX()- br.width;
				}
			}
		}
		
		// 개체와 충돌
		for(int i=0; i<controller.entity.size(); i++) {
			Entity en = controller.entity.get(i);
			
			// 굼바와 충돌
			if(en.getId() == Id.goomba) {
				
				// 파워 스타 먹은 상태
				if(immortal) {		
					if(collision().intersects(en.collision())) {
					}
				}
				
				else {
					// 굼바 머리와 발이 닿으면 굼바가 죽는다.
					if(collisionBottom().intersects(en.collision())) {
						Main.stompSound.play();
						en.die();
					}
				
					// 플레이어 상태에 따라 크기가 줄어들거나 죽는다.
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
			
			// 피라냐와 충돌
			else if(en.getId() == Id.piranha) {
				
				// 파워 스타 먹은 상태
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
			
			// 쿠파와 충돌
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
			
			
			
			// 새똥과 충돌
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
	         
			 // 움직이는 타일과 충돌
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
			
			//보스와 충돌 
			else if(en.getId()==Id.boss) {  
				
				if(immortal) {   //마리오가 파워스타 먹은 상태이면 보스와의 충돌 감지 못함
	               if(collision().intersects(en.collision())) {
	               }
	            }//if

	            else {  //파워스타 먹지 않았을 때 
	               if(collisionBottom().intersects(en.collisionTop())) { //마리오가 보스 위에서 접촉할때
	            	   if(en.attackable){ //마리오가 보스를 공격하면 체력1감소 및 노멀모드로 변함
	            		   System.out.println("콜리젼");
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
	               else {  //보스랑 충돌하면
	            	   if(en.bossMode != BossMode.weaken) {
	 	                  if(collision().intersects(en.collision())) {
	 	                     System.out.println("안돼");
	 	                     if(mode == PlayerMode.supermode) {  //캐릭터가 컸다면 크기 감소
	 	                        mode = PlayerMode.normal; 
	 	                        width /= 2;    
	 	                        height /= 2;
	 	                     }
	 	                     else if (mode == PlayerMode.normal)
	 	                    	 die(); //캐릭터가 작았다면 죽음
	 	                  }
	            	   }
	               }

	            }//else

			}
			
			// 하트와 충돌
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
			
			// 버섯 획득 
			else if(en.getId()==Id.mushroom) {
				// 버섯을 먹으면 크기가 커진다.
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
			
			// 코인 획득
			else if(en.getId() == id.coin) {
				if(collision().intersects(en.collision())) {
					Main.coinNum++;
					Main.coinSound.play();
					en.die();
				}
			}
			
			// 파워 스타 획득
			else if(en.getId() == id.star) {
				if(collision().intersects(en.collision())) {
					immortal = true;
					en.die(); 
				}
			}
	        
	        // 파이어 플라워 획득 
			else if (en.getId() == Id.fireflower) {
				if(collision().intersects(en.collision())) {
					mode = PlayerMode.firemode;
					en.die();
				}
			}
			
			// 목숨 사과 획득
			else if(en.getId() == id.liveapple) {
				if(collision().intersects(en.collision())) {
					Main.liveNum++;
					en.die(); 
				}
			}
			
			// 독사과 획득
			else if(en.getId() == id.poisionapple) {
				if(collision().intersects(en.collision())) {
					die();
					en.die();
				}
			}
	        
	        // 요시 알 획득
			else if(en.getId() == id.yoshiegg) {
				
				 if(collision().intersects(en.collision())) {
					 yoshi = true;
	         		 en.die();
	         	 } 	 
			}
		}
		
		// 점프
		if(jumping) {
			gravity -= 0.3;
			setVelY((int)-gravity);
			if(gravity <= 0.5) {
				jumping = false;
				falling = true;
			}
		}
		
		// 추락
		if(falling && !ridingPipe) {
			if(!Main.inBonusTime) 
				gravity += 0.3;
			else {
				gravity += 0.5;
				
				// 보너스 타임에서 너무 많이 떨어지면 죽는다.
				if(y >= 5000)
					die();
			}
			setVelY((int)gravity);
		}
		
		// 플레이어가 움직이고 있으면
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
					// 다음 애니메이션 이미지가 왼쪽으로 향하는 이미지면 다시 오른쪽으로 향하는 이미지로 돌아간다.
					if(frame>=3) {
						frame = 0;
					}
					frameDelay = 0;
				}
			}
		}
		
		// 파이프를 타면
		if(ridingPipe) {
			for(int i=0; i<Main.controller.brick.size(); i++) {
				Brick br = Main.controller.brick.get(i);
				
				if(br.getId() == id.pipe) {
					
					// 파이프와 다리가 닿으면 파이프를 타고 내려간다.
					if(collision().intersects(br.collision())) {
						setVelY(5);		// 파이프를 타고 계속 아래로 내려간다.
						setVelX(0);		// 파이프를 타는 동안은 좌우로 움직이지 않는다.
						pipeCount += velY;
					}
					// 파이프에서 다 내려오면 더이상 내려가지 않는다.
					if(pipeCount >= br.height+50) {
						ridingPipe = false;
						pipeCount= 0;
					}
				}
			}
		}
	}
}
