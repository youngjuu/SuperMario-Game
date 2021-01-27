package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import gameloop.Main;

public class Button {
	
	public int x, y, width, height;
	public String label;
	
	public Button(int x, int y, int width, int height, String label) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Century Gothic", Font.BOLD, 50));
		g.drawRect(getX(), getY(),getWidth(), getHeight());
		
		// ���̺��� �簢���� ����� ��ġ
		FontMetrics fm = g.getFontMetrics();
		int stringX = (getWidth() - fm.stringWidth(getLabel())) / 2;
		int stringY = (fm.getAscent() + (getHeight()-(fm.getAscent()+fm.getDescent())) / 2);
		g.drawString(getLabel(), getX()+stringX, getY()+stringY);
	}
	
	public void buttonEvent() {
		if(getLabel().toLowerCase().contains("start")) {
			Main.playing = true;
			Main.showLiveScreen = true;
		}
		if(getLabel().toLowerCase().contains("quit"))
			System.exit(0);
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}	
}
