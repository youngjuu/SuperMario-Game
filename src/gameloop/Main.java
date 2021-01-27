package gameloop;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import Input.Keyboard;
import Input.Mouse;
import entity.Entity;
import entity.player.Player;
import etc.Cam;
import etc.Id;
import etc.Sound;
import gui.Button;
import gui.Sprite;
import gui.SpriteSheet;
import gui.StartScreen;
import mode.BossMode;

public class Main extends Canvas implements Runnable {
	
	private Thread thread;

	private static BufferedImage stage1;
	private static BufferedImage stage2;
	private static BufferedImage bonusStage;
	private static BufferedImage playimage;
	
	private boolean running = false;				// 쓰레드 실행 여부
	private static boolean stageChanged = false;
	
	// 윈도우 크기
	public static final int WIDTH = 270;
	public static final int HEIGHT = 270;
	public static final int SCALE = 5;
	
	public static int stage = 1;
	public static int coinNum = 0;
	public static int liveNum = 5;					// 플레이어 목숨 개수
	public static int liveScreenTime = 0; 			// 목숨 화면 지속 시간

	public static boolean playing = false;			// 게임 실행 여부
	public static boolean showLiveScreen = false; 	// 목숨 화면 표시
	public static boolean gameOver = false;
	public static boolean inBonusTime = false;
	public static boolean showScoreScreen = false; //점수화면 실행 여부 

	public static Controller controller;
	public static SpriteSheet sheet;
	public static Cam cam;
	public static StartScreen startscreen;
	public static ImageIcon background;
	public static Mouse mouse;
	public static ImageIcon gameoverscreen;
	
	public static Sprite wall;
	public static Sprite mushroom;
	public static Sprite randombox;
	public static Sprite usedrandombox;
	public static Sprite coin;
	public static Sprite pipe;
	public static Sprite piranha;
	public static Sprite star;
	public static Sprite koopashell;		// 쿠파 등껍질
	public static Sprite liveApple;
	public static Sprite poisionApple;
	public static Sprite poop;
	public static Sprite yoshiegg;
	public static Sprite diebox;
	public static Sprite diedbox;
	public static Sprite fireflower;
	public static Sprite fireball;
	public static Sprite movingtile;
	public static Sprite deadPlayer;
	public static Sprite bossWeaken;
	public static Sprite heart;
	
	public static Sprite[] player;
	public static Sprite[] shinningPlayer;
	public static Sprite[] fireplayer;
	public static Sprite[] goomba;
	public static Sprite[] goal;
	public static Sprite[] koopa;
	public static Sprite[] bird;
	public static Sprite[] ridingyoshi;
	public static Sprite[] boss;
	public static Sprite[] bossSpin;
	public static Sprite[] peach;
	
	public static Sound themeSound;
	public static Sound jumpSound_small;
	public static Sound jumpSound_big;
	public static Sound coinSound;
	public static Sound bumpSound;
	public static Sound stompSound;
	public static Sound pipeSound;
	public static Sound goalSound;
	public static Sound stageClearSound;
	public static Sound worldClearSound;
	public static Sound dieSound;
	public static Sound gameOverSound;
	
	
	/* 윈도우 생성 */
	public Main() {
		Dimension size = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMinimumSize(size);
	}
	
	private void init() {		
		controller = new Controller();
		sheet = new SpriteSheet("/spritesheet.png");	// 이 시트에서 구성 요소들의 이미지를 가져온다.
		cam = new Cam();
		startscreen = new StartScreen();
		mouse = new Mouse();	
		background = new ImageIcon("C:\\Users\\최영주\\eclipse-workspace\\SuperMario\\img\\startscreen.png");
		gameoverscreen = new ImageIcon("C:\\Users\\최영주\\eclipse-workspace\\SuperMario\\img\\gameover.png");
		
		addKeyListener(new Keyboard());
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		wall = new Sprite(sheet, 1, 1);
		mushroom = new Sprite(sheet, 2, 1);
		randombox = new Sprite(sheet, 3, 1);
		usedrandombox = new Sprite(sheet, 4, 1);
		coin = new Sprite(sheet, 5, 1);
		pipe = new Sprite(sheet, 4, 2);	
		piranha = new Sprite(sheet, 5, 2);
		star = new Sprite(sheet, 7, 1);	
		koopashell = new Sprite(sheet, 2, 8);	
		liveApple = new Sprite(sheet, 6, 2);
		poisionApple = new Sprite(sheet, 7, 2);
		poop = new Sprite(sheet, 10, 1);
		yoshiegg = new Sprite(sheet, 6, 9);
		diebox = new Sprite(sheet, 8, 2);
		diedbox = new Sprite(sheet, 9, 2);
		fireflower = new Sprite(sheet, 9, 1);
		fireball = new Sprite(sheet, 8, 1);
		movingtile = new Sprite(sheet, 13, 14);
		deadPlayer = new Sprite(sheet, 8, 9);
		bossWeaken = new Sprite(sheet, 7, 9);
		heart = new Sprite(sheet, 8, 8);
		
		player = new Sprite[8];
		shinningPlayer = new Sprite[8];
		fireplayer = new Sprite[8];
		goomba = new Sprite[8];
		goal = new Sprite[3];
		koopa = new Sprite[8];
		bird = new Sprite[8];
		ridingyoshi = new Sprite[8];
		boss = new Sprite[8];
	    bossSpin = new Sprite[8];
	    peach = new Sprite[8];

		for(int i=0; i<player.length; i++)
			player[i] = new Sprite(sheet, i+1, 16);
		for(int i=0; i<shinningPlayer.length; i++)
	         shinningPlayer[i] = new Sprite(sheet, i+1, 5);
		for(int i=0; i<fireplayer.length; i++)
			fireplayer[i] = new Sprite(sheet, i+1, 12);
		for(int i=0; i<goomba.length; i++)
			goomba[i] = new Sprite(sheet, i+1, 14);
		for(int i=0; i<goal.length; i++)
			goal[i] = new Sprite(sheet, i+1, 2); 
		for(int i=0; i<koopa.length;i++)
			koopa[i] = new Sprite(sheet, i+1, 15);
		for(int i=0; i<bird.length; i++)
			bird[i] = new Sprite(sheet, i+1, 6);
		for(int i=0; i<ridingyoshi.length; i++)
			ridingyoshi[i] = new Sprite(sheet, i+1, 7);
		for(int i=0; i<boss.length; i++)
	         boss[i] = new Sprite(sheet, i+1, 11);
		for(int i=0; i<bossSpin.length; i++)
			bossSpin[i] = new Sprite(sheet, i+1, 10);
		for(int i=0; i<peach.length; i++)
	         peach[i] = new Sprite(sheet, i+9,16);
		
		// 레벨 이미지를 가져온다.
		try {
			stage1 = ImageIO.read(getClass().getResource("/level1.png"));
			stage2 = ImageIO.read(getClass().getResource("/level2.png"));
			bonusStage = ImageIO.read(getClass().getResource("/bonusstage.png"));
			playimage = ImageIO.read(getClass().getResource("/playingimage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		themeSound = new Sound("/snd/maintheme.wav");
		jumpSound_small = new Sound("/snd/smb_jump-small.wav");
		jumpSound_big = new Sound("/snd/smb_jump-super.wav");
		coinSound = new Sound("/snd/smb_coin.wav");
		bumpSound = new Sound("/snd/smb_bump.wav");
		stompSound = new Sound("/snd/smb_stomp.wav");
		pipeSound = new Sound("/snd/smb_pipe.wav");
		goalSound = new Sound("/snd/smb_flagpole.wav");
		stageClearSound = new Sound("/snd/smb_stage_clear.wav");
		worldClearSound = new Sound("/snd/smb_world_clear.wav");
		dieSound = new Sound("/snd/smb_mariodie.wav");
		gameOverSound = new Sound("/snd/smb_gameover.wav");
		
		// 스테이지 변경
		if(!stageChanged)
			controller.createLevel(stage1);
		else if(stageChanged && stage==2)
			controller.createLevel(bonusStage);
		else if(stageChanged && stage==3)
			controller.createLevel(stage2);
	}
	
	/* 게임 시작 시 쓰레드를 하나씩 생성 */
	private synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}
	
	/* 쓰레드 실행 */
	public void run() {
		init();
		requestFocus();	
		
		long lastTime = System.nanoTime();	
		long timer = System.currentTimeMillis();	
		double delta = 0.0;
		double ns = 1000000000.0/60.0;	
		int frames = 0;
		int ticks = 0;	
		
		// 1초에 60개 정도의 프레임을 화면에 내보낸다.
		while(running) {
			long now = System.nanoTime();	
			delta += (now-lastTime)/ns;
			lastTime = now;	
			while(delta >= 1) {			// 구성 요소의 위치 업데이트
				update();
				ticks++;
				delta--;
			}
			render();					// 프레임 업데이트
			frames++;
			if (System.currentTimeMillis()-timer > 1000) {
				timer += 1000;
				System.out.println(frames + " Frames Per Second " + ticks + " Updates Per Second");
				frames = 0;
				ticks = 0;
			}
		}
		stop();							// 쓰레드 중지
	}
	
	/* 게임 종료 시 쓰레드 하나만 남기고 나머진 블락 */
	private synchronized void stop() {
		if (!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/* 프레임 생성 */
	public void render() {
		
		// 더블 버퍼링
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(4);
			return;
		}
		
		// 배경 그래픽 설정
		Graphics g = bs.getDrawGraphics();
		g.drawImage(playimage, 0, 0, getWidth(), getHeight(), null);	// 게임 화면 배경
		
		if(!gameOver) {

			// 코인 개수 표시
			g.drawImage(coin.getBufferedImage(), 20, 20, 50, 50, null);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 20));
			g.drawString("x" + coinNum, 80, 50);

			// 스테이지 표시
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 20));
			if(!stageChanged)
				g.drawString("STAGE 1", 1250, 45);
			else if(stageChanged && stage==2)
				g.drawString("BONUS TIME", 1200, 45);
			else if(stageChanged && stage==3)
				g.drawString("STAGE 2", 1245, 45);
		}		
		
		// 메인 화면 생성
		if(!playing && !gameOver) {
			g.drawImage(background.getImage(), 0, 0, getFrameWidth()+20, HEIGHT*3, null);
			startscreen.render(g);
		}		
		
		// 게임 화면 생성
		 if(playing && !showLiveScreen && !showScoreScreen)  {
			g.translate(cam.getX(), cam.getY()+300);
			controller.render(g);
		}
		
		if(showLiveScreen) {
			
			// 목숨 화면 생성
			if(!gameOver) {
				coinNum = 0; 	//동전 개수 초기화 
				
				g.setColor(Color.BLACK);
	            g.fillRect(0, 0, getWidth(), getHeight());
	         
	            g.setColor(Color.WHITE);
	            g.setFont(new Font("Courier", Font.BOLD, 50));
	            g.drawImage(Main.player[0].getBufferedImage(), 550, 400, 100, 100, null);
	            g.drawString("x" + liveNum, 670, 500);     
	         }
			
			// 게임 종료 화면 생성
			else {
				g.drawImage(gameoverscreen.getImage(), 0, 0, getFrameWidth()+20, HEIGHT*2+195, null);
			}
		}	
		
		// 점수 화면 생성
		if(showScoreScreen && !gameOver) {
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, getWidth(), getHeight()); 

			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 100));
			g.drawString("FINISH", 670, 250);
			g.drawString("코인 " + coinNum + "개 획득", 670, 500);      
		}
		
		g.dispose();
		bs.show();
	}
	
	/* 구성요소 위치 업데이트 */
	public void update() {
		if(playing)
			controller.update();
		
		for(Entity en: controller.entity) {
			if(en.getId() == Id.player)
				cam.update(en);
		}
		
		// 기존 레벨 초기화 및 새로 시작
		if (showLiveScreen && playing) 
			liveScreenTime++;
		
		// 목숨 화면에서 2초 후 게임 화면이나 메인 화면으로 전환
		if(liveScreenTime >= 120) {
			if(!gameOver) {
				liveScreenTime = 0;
				controller.clearLevel();
				
				if(stage==1 || stage==2)
					controller.createLevel(stage1);
				else if(stage == 3)
					controller.createLevel(stage2);
					
				showLiveScreen = false;
				themeSound.play();
			}
			else if(gameOver) {
				liveScreenTime = 0;
				playing = false;
				showLiveScreen = false;
			}
		}
	}
	
	public static void nextStage() {
		stageChanged = true;
		stage++;
		controller.clearLevel();
		if(stage == 2) {
			controller.createLevel(bonusStage);
			inBonusTime = true;
			themeSound.close();
			stageClearSound.play();
		}
		else if(stage == 3) {
			controller.createLevel(stage2);
			inBonusTime = false;
			stageClearSound.play();
		}
		else if(stage==4) {
			worldClearSound.play();
			showScoreScreen = true;  //@@@@@@@@@@@@@@@@@
		}	
	}
	
	public static int getFrameWidth() {
		return WIDTH*SCALE;
	}
	public static int getFrameHeight() {
		return HEIGHT*SCALE;
	}
	
	public static void main(String[] args) {
		Main game = new Main();
		JFrame frame = new JFrame("Super Mairo Platform Game");;
		frame.add(game);					
		frame.pack();					
		frame.setResizable(false);								// 사용자가 프레임 크기 변경 불가
		frame.setLocationRelativeTo(null);						// 프레임을 화면 가운데에 배치
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 윈도우 창 닫으면 프로세스 종료
		frame.setVisible(true);
		game.start();											// 쓰레드 생성
	}
}