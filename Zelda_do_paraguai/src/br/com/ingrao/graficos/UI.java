package br.com.ingrao.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import br.com.ingrao.entities.Player;
import br.com.ingrao.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8,4 ,50,8 );
		g.setColor(Color.GREEN);
		g.fillRect(8,4 ,(int)((Game.player.life/Game.player.maxlife)*50) ,8 );
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,8));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxlife, 18, 11);
		
		
	}
}
