package br.com.ingrao.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import br.com.ingrao.World.World;
import br.com.ingrao.entities.BulletShoot;
import br.com.ingrao.entities.Enemy;
import br.com.ingrao.entities.Entity;
import br.com.ingrao.entities.Player;
import br.com.ingrao.graficos.Spritesheet;
import br.com.ingrao.graficos.UI;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	public static final int WIDTH = 240;
	public static final int HEIGTH = 160;
	public static final int SCALE = 3;
	private Thread thread;
	private boolean isRunning;
	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;
	public UI ui;

	public int CURRENT_LEVEL = 1, MAX_LEVEL = 2;
	public static String gameState = "MENU";

	public static boolean showMessageGameOver = true;
	public int framesGameOver = 0;
	public boolean restartGame = false;

	public static Menu menu;

	public boolean saveGame = false;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
	
	public Font newfont ;

	public Game() {
		Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGTH * SCALE));
		initFrame();
		// iniciando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGTH, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		// world = new World("/map.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");

		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(16f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menu = new Menu();

	}

	public void initFrame() {
		frame = new JFrame("GAME #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();

	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public void tick() {// atualizar
		if (gameState == "NORMAL") {

			if (this.saveGame) {
				this.saveGame = false;
				String[] opt1 = { "level" };
				int[] opt2 = { this.CURRENT_LEVEL };
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Jogo salvo");
			}
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
				;

			}
			if (enemies.size() == 0) {

				if (CURRENT_LEVEL == MAX_LEVEL) {
					CURRENT_LEVEL = 1;
				}
				CURRENT_LEVEL++;
				String newWorld = "level" + CURRENT_LEVEL + ".png";
				// System.out.println(newWorld);
				Game.world.restartGame(newWorld);
			}
		} else if (gameState == "GAME_OVER") {
			framesGameOver++;
			if (framesGameOver == 30) {
				framesGameOver = 0;
				if (showMessageGameOver) {
					showMessageGameOver = false;
				} else {
					showMessageGameOver = true;
				}
			}
		} else if (gameState == "MENU") {
			menu.tick();
		}
	}

	public void render() {// renderizar
		// int xI =0 , yI=0;
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGTH);
		/****/
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGTH * SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Pedras : " + player.ammo, 600, 20);
		
		if (gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGTH * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 32));
			g.setColor(Color.white);
			g.drawString("Game over", (WIDTH * SCALE) / 2 - 80, (HEIGHT * SCALE) / 2 + 200);
			g.setFont(new Font("arial", Font.BOLD, 25));
			if (showMessageGameOver) {
				g.drawString(">Pressione Enter Para Reniciar", (WIDTH * SCALE) / 2 - 150, (HEIGHT * SCALE) / 2 + 250);
			}

			if (restartGame) {
				CURRENT_LEVEL = 1;
				gameState = "NORMAL";
				String newWorld = "level" + CURRENT_LEVEL + ".png";
				// System.out.println(newWorld);
				Game.world.restartGame(newWorld);
			}
		} else if (gameState == "MENU") {
			menu.render(g);
		}
		bs.show();

	}

	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();

		while (isRunning) {
			long now = System.nanoTime();
			// System.out.println(ns);
			delta += (now - lastTime) / ns;
			// System.out.println(now - lastTime);
			lastTime = now;
			if (delta >= 1) {
				// System.out.println();
				tick();
				render();
				frames++;
				delta--;

			}
			if (System.currentTimeMillis() - timer >= 1000) {

				System.out.println("FPS " + frames);
				frames = 0;
				timer += 1000;

			}
			// System.out.println("o jogo ta rodando");
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// System.out.println("direita");
			player.right = true;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			// System.out.println("esquerda");
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			// System.out.println("baixo");
			player.down = true;
			if (gameState == "MENU") {
				menu.down = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			// System.out.println("cima");
			player.up = true;
			if (gameState == "MENU") {
				menu.up = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Player.shoot = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
			if (gameState == "MENU") {
				menu.pressionado = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (gameState == "NORMAL") {
				this.saveGame = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// System.out.println("direita");
			player.right = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			// System.out.println("esquerda");
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			// System.out.println("baixo");
			player.down = false;
		} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			// System.out.println("cima");
			player.up = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Player.shoot = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = false;
			if (gameState == "MENU") {
				menu.pressionado = false;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
