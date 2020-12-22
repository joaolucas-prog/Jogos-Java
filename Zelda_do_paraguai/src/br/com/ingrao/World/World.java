package br.com.ingrao.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import br.com.ingrao.entities.Bullet;
import br.com.ingrao.entities.Enemy;
import br.com.ingrao.entities.Entity;
import br.com.ingrao.entities.LifePack;
import br.com.ingrao.entities.Player;
import br.com.ingrao.entities.Weapon;
import br.com.ingrao.graficos.Spritesheet;
import br.com.ingrao.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTILE(xx * 16, yy * 16, Tile.TAIL_FLOOR);
					/*
					 * if (pixelAtual == 0xFF000000) { // floor (chão) tiles[xx+(yy * WIDTH)] = new
					 * FloorTILE(xx*16,yy*16,Tile.TAIL_FLOOR); } else
					 */ if (pixelAtual == 0xFFFFFFFF) {
						// parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TAIL_WALL);
					} else if (pixelAtual == 0xFF0026FF) {
						// player;
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (pixelAtual == 0xFFFF0000) {
						// enemy
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if (pixelAtual == 0XFFFF00DC) {
						// arma weapon
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
					} else if (pixelAtual == 0xFF00FFFF) {
						// vida
						Game.entities.add(new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN));
					} else if (pixelAtual == 0XFF7F0037) {
						Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					}

				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void restartGame(String level ) {
		Game.enemies.clear();
		Game.entities.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16 ,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}

	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		boolean colisao1 = (tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile);
		boolean colisao2 = (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile);
		boolean colisao3 = (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile);
		boolean colisao4 = (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile);

		return !(colisao1 || colisao2 || colisao3 || colisao4);

	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGTH >> 4);
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
