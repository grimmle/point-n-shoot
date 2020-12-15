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
		Bullet b = new Bullet(p.getX()+15, p.getY()+15, mx, my, ((Player) p).getBuff(), p.id, p.getColor());
		DynamicObjectsUpdateMsg dyn = new DynamicObjectsUpdateMsg();
		dyn.id = Client.id;
		dyn.content = b;
		Client.sendObject(dyn);
		
//		for(int i = 0; i < handler.objects.size(); i++) {
//			GameObject tempObject = handler.objects.get(i);
//			if(tempObject.getId() == ID.Player) {
//				handler.addObject(new Bullet(tempObject.getX()+15, tempObject.getY()+15, ID.Bullet, handler, mx, my, ((Player) tempObject).getBuff()));
//			}
//		}
	}
}
