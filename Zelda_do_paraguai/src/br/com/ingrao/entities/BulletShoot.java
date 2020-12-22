package br.com.ingrao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.ingrao.World.Camera;
import br.com.ingrao.main.Game;

public class BulletShoot extends Entity {
	
	private int dx, dy;
	private double spd = 4;
	
	private int life = 30 , curLife = 0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int dx , int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x += dx * spd;
		y += dy * spd;
		curLife++;
		if(curLife == life) {
			//curLife = 0;
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(Entity.BULLET_EN , (int)(this.getX() - Camera.x) ,(int)( this.getY() - Camera.y ), null);
	}
}
