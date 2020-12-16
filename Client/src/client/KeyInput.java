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
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_W) Game.player.setUp(false);
		if(key == KeyEvent.VK_A) Game.player.setLeft(false);
		if(key == KeyEvent.VK_S) Game.player.setDown(false);
		if(key == KeyEvent.VK_D) Game.player.setRight(false);
	}
}
