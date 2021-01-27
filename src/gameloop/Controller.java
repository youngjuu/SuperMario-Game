package gameloop;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import brick.AppleBox;
import brick.Brick;
import brick.DieBrick;
import brick.Goal;
import brick.Pipe;
import brick.RandomBox;
import brick.Wall;
import entity.Entity;
import entity.enemy.Bird;
import entity.enemy.Boss;
import entity.enemy.Goomba;
import entity.enemy.Koopa;
import entity.enemy.MovingTile;
import entity.enemy.Peach;
import entity.enemy.Piranha;
import entity.item.Coin;
import entity.item.LiveApple;
import entity.item.YoshiEgg;
import entity.player.Player;
import etc.Id;

public class Controller {
	
	public LinkedList<Entity> entity = new LinkedList<Entity>();	// Entity Ŭ������ ��ӹ��� ���� ��� ����Ʈ
	public LinkedList<Brick> brick = new LinkedList<Brick>();		// Brick Ŭ������ ��ӹ��� ���� ��� ����Ʈ
	
	// �� ����Ʈ�� �ִ� ���� ��ҵ��� �̹��� ����
	public void render(Graphics g) {
		for(int i=0; i<entity.size(); i++) {
			Entity en = entity.get(i);
			en.render(g);
		}
		for (int i=0; i<brick.size(); i++) {
			Brick br = brick.get(i);
			br.render(g);
		}
	}
	
	// �� ����Ʈ�� �ִ� ���� ��ҵ��� �̺�Ʈ ����
	public void update() {		
		for(int i=0; i<entity.size(); i++) {
			Entity en = entity.get(i);
			en.update();
		}
		for (int i=0; i<brick.size(); i++) {
			Brick br = brick.get(i);
			br.update();
		}	
	}
	
	public void addEntity(Entity en) {
		entity.add(en);
	}	
	public void addBrick(Brick br) {
		brick.add(br);
	}
	
	public void killEntity(Entity en) {
		entity.remove(en);
	}
	public void removeBrick(Brick br) {
		brick.remove(br);
	}
	
	/* �� ���� */
	public void createLevel(BufferedImage level) {
		int width = level.getWidth();
		int height = level.getHeight();
		
		// ���� �̹����� �ȼ��� �ϳ��� ��ĵ�Ѵ�.
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				int pixel = level.getRGB(x, y);
				
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff; 
				int blue = (pixel) & 0xff;

				// �ȼ��� ���� ���� �ʿ��� ���� ��Ҹ� �߰��Ѵ�.
				if(red==0 && green==0 && blue==0)		// �������� ��
					addBrick(new Wall(x*64, y*64, 64, 64, true, Id.brick, this));		
				if(red==0 && green==0 && blue==255) 	// �Ķ����� �÷��̾�
					addEntity(new Player(x*64, y*64, 70, 70, Id.player, this));
				if(red==255 && green==0 && blue==255)	// ������ ����
					//addBrick(new Goal(x*65, y*64, 64, 64*5, true, Id.goal, this));
					addEntity(new Goomba(x*64, x*64, 80, 80, Id.goomba, this));			
				if(red==100 && green==100 && blue==0)   // ��ο� ����� ����
	                addEntity(new Coin(x*64, y*64, 70, 70, Id.coin, this));				
				if(red==0 && green==255 && blue==255)   // �ϴû��� ���� �ڽ�
	                addBrick(new RandomBox(x*65, y*64, 64, 64, true, Id.randombox, this));
				if(red==0 && green==100 && blue==100)   // ������� ��� �ڽ�
	                addBrick(new AppleBox(x*65, y*64, 64, 64, true, Id.applebox, this));			
				if(red==0 && green==255 && blue==0)		// ���λ��� ������
					addBrick(new Pipe(x*64, y*64, 64, 64*13, true, Id.pipe, this));
				if(red==255 && green==0 && blue==0) 	// �������� �Ƕ�� �Ĺ�
					addEntity(new Piranha(x*64, y*64, 64, 64, Id.piranha, this));
				if(red==200 && green==0 && blue==200)   // ������� ��
	                addBrick(new Goal(x*65, y*64, 64, 64*5, true, Id.goal, this));
				if(red==255 && green==119 && blue==0)	// ��Ȳ���� ����
					addEntity(new Koopa(x*64, y*64, 80, 80, Id.koopa, this));
				if(red==0 && green==255 && blue==100)
					addEntity(new LiveApple(x*64, y*64, 64, 64, Id.liveapple, this));
				if(red==100 && green==100 && blue==100)
					addEntity(new Bird(x*64, y*64, 64, 64, Id.bird, this));
				if(red==100 && green==255 && blue==100)
					addEntity(new YoshiEgg(x*64, y*64, 64, 64, Id.yoshiegg, this));
				if(red==10 && green==10 && blue==10)
					addBrick(new DieBrick(x*65, y*64, 64, 64, true, Id.diebox, this));
				//if(red==50 && green==50 && blue==50)	// ȸ���� �����̴� Ÿ��
					//addEntity(new MovingTile(x*64, y*64, 80, 80, Id.movingtile, this));
				if(red==255 && green==255 && blue==0)
					addEntity(new Boss(x*64, y*64, 100, 100, Id.boss, this, 3));
				if(red==240 && green==100 && blue==240)
					addEntity(new Peach(x*64, y*64, 64, 64, Id.peach, this));
			}
		}
	}

	public void clearLevel() {
		entity.clear();
		brick.clear();
	}
}
