package br.com.ingrao.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.com.ingrao.World.World;

public class Menu {

	public String[] options = { "novo jogo", "carregar jogo", "sair" };

	public int currentOption = 0;
	public int maxOption = options.length - 1;

	public boolean pressionado;

	public boolean up, down;

	public static boolean pause;

	public static boolean saveExist = false;
	public static boolean saveGame = false;
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for (int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch (spl2[0]) {
			case ("level"): {
				World.restartGame("level" + spl2[1] + ".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			}
			}
		}
	}

	public static String loadGame(int enconde) {
		String line = "";
		File file = new File("save.txt");
		if (file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while ((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for (int i = 0; i < val.length; i++) {
							val[i] += enconde;
							trans[1] += val[i];
						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
					}
				} catch (IOException e) {

				}
			} catch (FileNotFoundException e) {

			}
		}
		return line;
	}

	public static void saveGame(String[] val1, int[] val2, int enconde) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));

		} catch (IOException e) {

		}
		for (int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for (int n = 0; n < value.length; n++) {
				value[n] += enconde;
				current += value[n];
			}
			try {
				write.write(current);
				if (i < val1.length - 1) {
					write.newLine();
				}
			} catch (IOException e) {
			}
		}
		try {
			write.flush();
			write.close();
		} catch (IOException e) {
		}
	}

	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			 saveExist = true;
		}else {
			saveExist = false;
		}
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if (pressionado) {
			pressionado = false;
			if (options[currentOption] == "novo jogo") {
				Game.gameState = "NORMAL";
				pause = false;
				file = new File("save.txt");
				file.delete();
			} else if (options[currentOption] == "carregar jogo") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}

			} else if (options[currentOption] == "sair") {
				System.exit(1);
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGTH * Game.SCALE);
		g.setColor(Color.red);
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString(">Zelda do Paragai", (Game.WIDTH * Game.SCALE) / 2 - 130, (Game.HEIGTH * Game.SCALE) / 2 - 180);
		g.setFont(new Font("arial", Font.BOLD, 20));
		if (pause) {
			g.drawString("Resumir", (Game.WIDTH * Game.SCALE) / 2 - 70, (Game.HEIGTH * Game.SCALE) / 2 - 100);
		} else {
			g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 70, (Game.HEIGTH * Game.SCALE) / 2 - 100);
		}
		g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 70, (Game.HEIGTH * Game.SCALE) / 2 - 20);
		g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 70, (Game.HEIGTH * Game.SCALE) / 2 + 60);

		if (options[currentOption] == "novo jogo") {
			g.setColor(Color.blue);
			if (pause) {
				g.drawString(">Resumir", (Game.WIDTH * Game.SCALE) / 2 - 82, (Game.HEIGTH * Game.SCALE) / 2 - 100);

			} else {
				g.drawString(">Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 82, (Game.HEIGTH * Game.SCALE) / 2 - 100);
			}
		} else if (options[currentOption] == "carregar jogo") {
			g.setColor(Color.blue);
			g.drawString(">Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 82, (Game.HEIGTH * Game.SCALE) / 2 - 20);

		} else if (options[currentOption] == "sair") {
			g.setColor(Color.blue);
			g.drawString(">Sair", (Game.WIDTH * Game.SCALE) / 2 - 82, (Game.HEIGTH * Game.SCALE) / 2 + 60);

		}
	}

}
