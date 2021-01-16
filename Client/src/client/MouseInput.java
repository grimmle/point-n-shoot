package client;

import common.*;
import messages.DynamicObjectsUpdateMsg;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {
	
	private Camera camera;
	
	public MouseInput(Camera camera) {
		this.camera = camera;
	}
	public void mousePressed(MouseEvent e) {
		int mx = (int) (e.getX() + camera.getX());
		int my = (int) (e.getY() + camera.getY());
		
		Player p = Game.players.get(Client.id);
		Bullet b = new Bullet(p.getX(), p.getY(), mx, my, ((Player) p).getBuff(), p.id, p.getColor(), 10);
		DynamicObjectsUpdateMsg dyn = new DynamicObjectsUpdateMsg();
		dyn.id = Client.id;
		dyn.content = b;
		Client.sendObject(dyn);
	}
}
