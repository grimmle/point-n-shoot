package client;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import common.*;

public class KeyInput extends KeyAdapter {
	
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W) Game.player.setUp(true);
		if(key == KeyEvent.VK_A) Game.player.setLeft(true);
		if(key == KeyEvent.VK_S) Game.player.setDown(true);
		if(key == KeyEvent.VK_D) Game.player.setRight(true);
		
//		MovePlayerMsg move = new MovePlayerMsg();
//		if(key == KeyEvent.VK_W) move.velY = -5;
//		if(key == KeyEvent.VK_A) move.velX = -5;
//		if(key == KeyEvent.VK_S) move.velY = 5;
//		if(key == KeyEvent.VK_D) move.velX = 5;
//		Client.sendObject(move);
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_W) Game.player.setUp(false);
		if(key == KeyEvent.VK_A) Game.player.setLeft(false);
		if(key == KeyEvent.VK_S) Game.player.setDown(false);
		if(key == KeyEvent.VK_D) Game.player.setRight(false);
		
//		MovePlayerMsg move = new MovePlayerMsg();
//		if(key == KeyEvent.VK_W) move.velY = 0;
//		if(key == KeyEvent.VK_A) move.velX = 0;
//		if(key == KeyEvent.VK_S) move.velY = 0;
//		if(key == KeyEvent.VK_D) move.velX = 0;
//		Client.sendObject(move);
	}
}
