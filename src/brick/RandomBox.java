package brick;

import java.awt.Graphics;
import java.util.Random;

import entity.item.FireFlower;
import entity.item.Mushroom;
import entity.item.PowerStar;
import etc.Id;
import gameloop.Controller;
import gameloop.Main;
import gui.Sprite;

public class RandomBox extends Brick {
	
	private Sprite randombox;
	private Random random = new Random();
	private boolean poppedUp = false;	
	private int spriteY = getY();
	
	public RandomBox(int x, int y, int width, int height, boolean block, Id id, Controller controller) {
		super(x, y, width, height, block, id, controller);
		
		int item = random.nextInt(3);
		if(item == 0)
			this.randombox = Main.mushroom;
		else if(item == 1)
			this.randombox = Main.star;
		else
			this.randombox = Main.fireflower;
	}
	
	/* 랜덤 박스 그래픽 */
	public void render(Graphics g) {
		if(!poppedUp)
			g.drawImage(randombox.getBufferedImage(), x, spriteY, width, height, null);
		
		if (!activate) 
			g.drawImage(Main.randombox.getBufferedImage(), x, y ,width, height, null);
		else
			g.drawImage(Main.usedrandombox.getBufferedImage(), x, y, width, height, null );
		
	}
	
	/* 랜덤 박스 이벤트 */
	public void update() {
		
		// 플레이어가 랜덤 박스와 충돌했으나 아무것도 튀어나오지 않았다면 버섯을 생성한다. 
		if(activate && !poppedUp) {
			
			// 버섯의 y좌표는 랜덤 박스보다 높은 좌표에 위치해야 한다.
			spriteY--;
			if(spriteY <= y-height) {
				if(randombox == Main.mushroom)
					controller.addEntity(new Mushroom(x, spriteY, width, height, Id.mushroom, controller));
				else if(randombox == Main.star)
					controller.addEntity(new PowerStar(x, spriteY, width, height, Id.star, controller));
				else if(randombox == Main.fireflower)
					controller.addEntity(new FireFlower(x, spriteY, width, height, Id.fireflower, controller));

				poppedUp = true;
			}
		}		
	}
}