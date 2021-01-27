package Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gui.Button;
import gameloop.Main;

public class Mouse implements MouseListener, MouseMotionListener  {
	public int x, y;
	
	public void mouseDragged(MouseEvent ev) {
		
	}

	public void mouseMoved(MouseEvent ev) {
		x = ev.getX();
		y = ev.getY();		
	}

	public void mouseClicked(MouseEvent ev) {
		
	}

	public void mouseEntered(MouseEvent ev) {
		
	}

	public void mouseExited(MouseEvent ev) {
		
	}

	public void mousePressed(MouseEvent ev) {
		for(int i=0; i<Main.startscreen.btns.length; i++) {
			Button button = Main.startscreen.btns[i];		
			
			// 버튼 내부 클릭 여부 감지
			if(x>=button.getX() && y>=button.getY() && x<=button.getX()+button.getWidth() && y<=button.getY()+button.getHeight()) {
				button.buttonEvent();
			}
		}
	}

	public void mouseReleased(MouseEvent m) {
		
	}
}
