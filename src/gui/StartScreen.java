package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class StartScreen {

	public Button[] btns;
	
	public StartScreen() {
		btns = new Button[2];
		
		btns[0] = new Button(500, 400, 350, 100, "START GAME");
		btns[1] = new Button(500, 520, 350, 100, "QUIT");
	}
	
	public void render(Graphics g) {
		for(int i=0; i<btns.length; i++) {
			btns[i].render(g);
		}
	}
	

}
