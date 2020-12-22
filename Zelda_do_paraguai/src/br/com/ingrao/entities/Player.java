package br.com.ingrao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import br.com.ingrao.World.Camera;
import br.com.ingrao.World.World;
import br.com.ingrao.graficos.Spritesheet;
import br.com.ingrao.main.Game;

public class Player extends Entity {

	public boolean left, right, up, down;
	public int dir = 0, right_dir = 0, left_dir = 1;
	public double speed = 1.4;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 4;
	private boolean moved = false;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] playerDamage;

	public boolean isDamage = false;

	private int damageframes = 0, indexDamage = 0;

	public double life = 100, maxlife = 100;
	public int ammo = 0;
	
	private boolean hasGun = false;
	
	public static boolean shoot = false;
	

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = new BufferedImage[2];

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
		}
		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
		for (int i = 0; i < 2; i++) {
			playerDamage[i] = Game.spritesheet.getSprite(0 + (i * 16), 16, 16, 16);
		}
	}

	public void tick() {
		moved = false;
		if (right && World.isFree((int) (getX() + speed), (int) getY())) {
			moved = true;
			dir = right_dir;
			setX(x += speed);

		} else if (left && World.isFree((int) (getX() - speed), (int) getY())) {
			moved = true;
			dir = left_dir;
			setX(x -= speed);
		}
		if (up && World.isFree((int) (getX()), (int) (getY() - speed))) {
			moved = true;
			setY(y -= speed);

		} else if (down && World.isFree((int) getX(), (int) (getY() + speed))) {
			moved = true;
			setY(y += speed);
		}
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index == maxIndex) {
					index = 0;
				}
			}
		}
		if (isDamage) {
			this.damageframes++;
			if (this.damageframes == 15) {
				this.damageframes = 0;
				isDamage = false;
			}
			if ((this.damageframes % 5) == 1) {
				indexDamage = 1;
				//System.out.println("entrou");
			} else {
				indexDamage = 0;
			}
		}
		if(shoot && ammo > 0 && hasGun) {
			//criar balas e atirar
			shoot = false;
			ammo--;
			int dx = 0;
			if( dir == right_dir) {
				dx = 1;
			}else {
				dx = -1;
			}
			BulletShoot bullet = new BulletShoot((int)this.getX(),(int)this.getY(),3 ,3 ,null ,dx ,0);
			Game.bullets.add(bullet);
			//System.out.println("atirando");
			
			
		}
		if (life == 0) {
			life = 0;
			Game.gameState = "GAME_OVER";
		}
	
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		Camera.x = Camera.clamp((int) (getX() - (Game.WIDTH / 2)), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp((int) (getY() - (Game.HEIGTH / 2)), 0, World.HEIGHT * 16 - Game.HEIGTH);
	}

	public void checkCollisionLifePack() {

		for (int i = 0; i < Game.entities.size(); i++) {// fazer listas separadas para melhor desempenho
			Entity atual = Game.entities.get(i);
			if (atual instanceof LifePack) {
				if (Entity.isColidding(this, atual)) {
					life += 10;
					if (life >= 100) {
						life = 100;
					}
					Game.entities.remove(atual);
				}
			}
		}
	}

	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {// fazer listas separadas para melhor desempenho
			Entity atual = Game.entities.get(i);
			if (atual instanceof Bullet) {
				if (Entity.isColidding(this, atual)) {
					ammo += 3;
					if (ammo >= 20) {
						ammo = 20;
					}
					Game.entities.remove(atual);
				}
			}
		}
	}
	public void checkCollisionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {// fazer listas separadas para melhor desempenho
			Entity atual = Game.entities.get(i);
			if (atual instanceof Weapon) {
				if (Entity.isColidding(this, atual)) {
					hasGun = true;
					Game.entities.remove(atual);
				}
			}
		}
	}
	public void render(Graphics g) {
		int xg  = (int)this.getX(); 
		int yg = (int) this.getY();
		if (!isDamage) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.WEAPON_EN , xg - Camera.x+8 , yg - Camera.y-3 ,null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.WEAPON_EN , xg - Camera.x-8 , yg - Camera.y-3 ,null);
				}
			}
		} else {
			g.drawImage(playerDamage[indexDamage], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
		}
	}
}
