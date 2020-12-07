package client;

import common.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {
	
	private GameHandler handler;
	private Camera camera;
	
	public MouseInput(GameHandler handler, Camera camera) {
		this.handler = handler;
		this.camera = camera;
	}
	public void mousePressed(MouseEvent e) {
		int mx = (int) (e.getX() + camera.getX());
		int my = (int) (e.getY() + camera.getY());
		
		for(int i = 0; i < handler.objects.size(); i++) {
			GameObject tempObject = handler.objects.get(i);
			if(tempObject.getId() == ID.Player) {
				handler.addObject(new Bullet(tempObject.getX()+15, tempObject.getY()+15, ID.Bullet, handler, mx, my, ((Player) tempObject).getBuff()));
			}
		}
	}
}
