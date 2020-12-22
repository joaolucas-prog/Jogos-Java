package br.com.ingrao.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.ingrao.main.Game;

public class Tile {
	
	public static BufferedImage TAIL_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TAIL_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	public static BufferedImage TAIL_ENEMY = Game.spritesheet.getSprite(112, 16, 16, 16);
	
	
	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x , int y , BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x,y-Camera.y, null);
	}
}
