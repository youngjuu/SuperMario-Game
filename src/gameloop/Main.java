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
	
	private boolean running = false;				// ������ ���� ����
	private static boolean stageChanged = false;
	
	// ������ ũ��
	public static final int WIDTH = 270;
	public static final int HEIGHT = 270;
	public static final int SCALE = 5;
	
	public static int stage = 1;
	public static int coinNum = 0;
	public static int liveNum = 5;					// �÷��̾� ��� ����
	public static int liveScreenTime = 0; 			// ��� ȭ�� ���� �ð�

	public static boolean playing = false;			// ���� ���� ����
	public static boolean showLiveScreen = false; 	// ��� ȭ�� ǥ��
	public static boolean gameOver = false;
	public static boolean inBonusTime = false;
	public static boolean showScoreScreen = false; //����ȭ�� ���� ���� 

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
	public static Sprite koopashell;		// ���� ���
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
	
	
	/* ������ ���� */
	public Main() {
		Dimension size = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMinimumSize(size);
	}
	
	private void init() {		
		controller = new Controller();
		sheet = new SpriteSheet("/spritesheet.png");	// �� ��Ʈ���� ���� ��ҵ��� �̹����� �����´�.
		cam = new Cam();
		startscreen = new StartScreen();
		mouse = new Mouse();	
		background = new ImageIcon("C:\\Users\\�ֿ���\\eclipse-workspace\\SuperMario\\img\\startscreen.png");
		gameoverscreen = new ImageIcon("C:\\Users\\�ֿ���\\eclipse-workspace\\SuperMario\\img\\gameover.png");
		
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
		
		// ���� �̹����� �����´�.
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
		
		// �������� ����
		if(!stageChanged)
			controller.createLevel(stage1);
		else if(stageChanged && stage==2)
			controller.createLevel(bonusStage);
		else if(stageChanged && stage==3)
			controller.createLevel(stage2);
	}
	
	/* ���� ���� �� �����带 �ϳ��� ���� */
	private synchronized void start() {
		if (running) return;
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}
	
	/* ������ ���� */
	public void run() {
		init();
		requestFocus();	
		
		long lastTime = System.nanoTime();	
		long timer = System.currentTimeMillis();	
		double delta = 0.0;
		double ns = 1000000000.0/60.0;	
		int frames = 0;
		int ticks = 0;	
		
		// 1�ʿ� 60�� ������ �������� ȭ�鿡 ��������.
		while(running) {
			long now = System.nanoTime();	
			delta += (now-lastTime)/ns;
			lastTime = now;	
			while(delta >= 1) {			// ���� ����� ��ġ ������Ʈ
				update();
				ticks++;
				delta--;
			}
			render();					// ������ ������Ʈ
			frames++;
			if (System.currentTimeMillis()-timer > 1000) {
				timer += 1000;
				System.out.println(frames + " Frames Per Second " + ticks + " Updates Per Second");
				frames = 0;
				ticks = 0;
			}
		}
		stop();							// ������ ����
	}
	
	/* ���� ���� �� ������ �ϳ��� ����� ������ ��� */
	private synchronized void stop() {
		if (!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/* ������ ���� */
	public void render() {
		
		// ���� ���۸�
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(4);
			return;
		}
		
		// ��� �׷��� ����
		Graphics g = bs.getDrawGraphics();
		g.drawImage(playimage, 0, 0, getWidth(), getHeight(), null);	// ���� ȭ�� ���
		
		if(!gameOver) {

			// ���� ���� ǥ��
			g.drawImage(coin.getBufferedImage(), 20, 20, 50, 50, null);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 20));
			g.drawString("x" + coinNum, 80, 50);

			// �������� ǥ��
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 20));
			if(!stageChanged)
				g.drawString("STAGE 1", 1250, 45);
			else if(stageChanged && stage==2)
				g.drawString("BONUS TIME", 1200, 45);
			else if(stageChanged && stage==3)
				g.drawString("STAGE 2", 1245, 45);
		}		
		
		// ���� ȭ�� ����
		if(!playing && !gameOver) {
			g.drawImage(background.getImage(), 0, 0, getFrameWidth()+20, HEIGHT*3, null);
			startscreen.render(g);
		}		
		
		// ���� ȭ�� ����
		 if(playing && !showLiveScreen && !showScoreScreen)  {
			g.translate(cam.getX(), cam.getY()+300);
			controller.render(g);
		}
		
		if(showLiveScreen) {
			
			// ��� ȭ�� ����
			if(!gameOver) {
				coinNum = 0; 	//���� ���� �ʱ�ȭ 
				
				g.setColor(Color.BLACK);
	            g.fillRect(0, 0, getWidth(), getHeight());
	         
	            g.setColor(Color.WHITE);
	            g.setFont(new Font("Courier", Font.BOLD, 50));
	            g.drawImage(Main.player[0].getBufferedImage(), 550, 400, 100, 100, null);
	            g.drawString("x" + liveNum, 670, 500);     
	         }
			
			// ���� ���� ȭ�� ����
			else {
				g.drawImage(gameoverscreen.getImage(), 0, 0, getFrameWidth()+20, HEIGHT*2+195, null);
			}
		}	
		
		// ���� ȭ�� ����
		if(showScoreScreen && !gameOver) {
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, getWidth(), getHeight()); 

			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", Font.BOLD, 100));
			g.drawString("FINISH", 670, 250);
			g.drawString("���� " + coinNum + "�� ȹ��", 670, 500);      
		}
		
		g.dispose();
		bs.show();
	}
	
	/* ������� ��ġ ������Ʈ */
	public void update() {
		if(playing)
			controller.update();
		
		for(Entity en: controller.entity) {
			if(en.getId() == Id.player)
				cam.update(en);
		}
		
		// ���� ���� �ʱ�ȭ �� ���� ����
		if (showLiveScreen && playing) 
			liveScreenTime++;
		
		// ��� ȭ�鿡�� 2�� �� ���� ȭ���̳� ���� ȭ������ ��ȯ
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
		frame.setResizable(false);								// ����ڰ� ������ ũ�� ���� �Ұ�
		frame.setLocationRelativeTo(null);						// �������� ȭ�� ����� ��ġ
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// ������ â ������ ���μ��� ����
		frame.setVisible(true);
		game.start();											// ������ ����
	}
}