package br.com.ingrao.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.ingrao.World.Camera;
import br.com.ingrao.main.Game;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(96, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(112, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(96, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(112, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144, 16, 16, 16);
	
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected int maskx,masky,mwidth,mheight;
	private BufferedImage sprite;
	
	public Entity(int x , int y , int width , int height , BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth  = width;
		this.mheight = height;
	}
	public void setMask(int maskx , int masky , int mwidth ,int mheight) {
		this.maskx =  maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	public void render(Graphics g) {
		g.drawImage(sprite,(int)this.getX() - Camera.x,(int)this.getY()- Camera.y,null);
	}
	public void tick() {
		
	}
	
	public static boolean isColidding(Entity e1 , Entity e2) {
		Rectangle e1Mask = new Rectangle((int)(e1.getX()+ e1.maskx) , (int)(e1.getY() + e1.masky) , e1.mwidth , e1.mheight);
		Rectangle e2Mask = new Rectangle((int)(e2.getX()+ e2.maskx ),(int)( e2.getY() + e2.masky) , e2.mwidth , e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
	

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
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

}
