package br.com.ingrao.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.ingrao.World.Camera;
import br.com.ingrao.World.World;
import br.com.ingrao.main.Game;
import br.com.ingrao.main.Sound;

public class Enemy extends Entity {

	private double speed = 0.6;

	private int maskx = 8, masky = 8, maskw = 10, maskh = 10;

	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 2;

	private BufferedImage[] sprites;
	
	private int life = 3;
	
	private int damageFrames = 10, damageCurrent = 0;
	private boolean isDamage = false;
	
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	}

	public void tick() {
		// if (Game.rand.nextInt(100) < 40) { modo 1 (random )
		maskx = 8;
		masky = 8;
		maskw = 10;
		maskh = 10;
		if (!isColinddingWithPlayer()) {
			if (((int) x < (int) Game.player.getX()) && World.isFree((int) (this.getX() + speed), (int) this.getY())
					&& !isColindding((int) (this.getX() + speed), (int) this.getY())) {
				x += speed;
			} else if (((int) x > (int) Game.player.getX())
					&& World.isFree((int) (this.getX() - speed), (int) this.getY())
					&& !isColindding((int) (this.getX() - speed), (int) this.getY())) {
				x -= speed;
			}
			if (((int) y < (int) Game.player.getY()) && World.isFree((int) (this.getX()), (int) (this.getY() + speed))
					&& !isColindding((int) (this.getX()), (int) (this.getY() + speed))) {
				y += speed;
			} else if (((int) y > (int) Game.player.getY())
					&& World.isFree((int) (this.getX()), (int) (this.getY() - speed))
					&& !isColindding((int) (this.getX()), (int) (this.getY() - speed))) {
				y -= speed;
			}

			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index == maxIndex) {
					index = 0;
				}
			}
			
		} else {
			if (Game.rand.nextInt(100) < 10) {
				Game.player.life--;
				Game.player.isDamage = true;
				Sound.hurtEffect.loop();
				
			}
			
		

		}
		collidingBullet();
		if(life<= 0) {
			destroySelf();
		}
		// }
		
		if (isDamage) {
			this.damageCurrent++;
			if( this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamage = false;
			}
		}
	}
	public void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}
	public void collidingBullet() {
		for ( int i = 0 ; i < Game.bullets.size();i++) {
			Entity e = Game.bullets.get(i);			
				if(Entity.isColidding(this, e)) {
					life--;
					isDamage = true;
					Game.bullets.remove(i);
					return;
				} 
		}
	}

	public boolean isColindding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle((int) e.getX() + maskx, (int) e.getY() + masky, maskw, maskh);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}

		}
		return false;
	}

	public boolean isColinddingWithPlayer() {
		Rectangle currentEnemy = new Rectangle((int) (this.getX() + maskx), (int) (this.getY() + masky), maskw, maskh);
		Rectangle player = new Rectangle((int) Game.player.getX(), (int) Game.player.getY(),
				(int) Game.player.getWidth(), (int) Game.player.getHeight());
		return currentEnemy.intersects(player);
	}

	public void render(Graphics g) {
		if(!isDamage) {
		g.drawImage(sprites[index], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
		}else {
			g.drawImage(Entity.ENEMY_FEEDBACK, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
		}
		// g.setColor(Color.blue);
		// g.fillRect((int) (this.getX() + maskx - Camera.x), (int) (this.getY() + masky
		// - Camera.y), maskw, maskh);
	}
}
