package brick;

import java.awt.Graphics;
import java.util.Random;

import entity.item.LiveApple;
import entity.item.PoisionApple;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;
import gui.Sprite;

public class AppleBox extends Brick {
	
	private Random random = new Random();
	private Sprite whichApple;
	private boolean poppedUp = false;
	private int spriteY = getY();

	public AppleBox(int x, int y, int width, int height, boolean block, Id id, Controller controller) {
		super(x, y, width, height, block, id, controller);
		
		// 목숨사과와 독사과 중 하나 랜덤 생성
		int apple = random.nextInt(2);
		if(apple == 0)
			whichApple = Main.liveApple;
		else
			whichApple = Main.poisionApple;			
	}

	public void render(Graphics g) {
		if(!poppedUp)
			g.drawImage(whichApple.getBufferedImage(), x, spriteY, width, height, null);
		
		if (!activate) 
			g.drawImage(Main.randombox.getBufferedImage(), x, y ,width, height, null);
		else
			g.drawImage(Main.usedrandombox.getBufferedImage(), x, y, width, height, null );
	}

	public void update() {
		
		if(activate && !poppedUp) {
			spriteY--;	
			
			// 사과가 랜덤 박스 위에 나타나면
			if(spriteY <= y-height) {
				
				if(whichApple == Main.liveApple)
					controller.addEntity(new LiveApple(x, spriteY, width, height, Id.liveapple, controller));
				else if(whichApple == Main.poisionApple)
					controller.addEntity(new PoisionApple(x, spriteY, width, height, Id.poisionapple, controller));
				
				poppedUp = true;
			}
		}		
	}

}
