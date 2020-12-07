package client;

import common.GameObject;

public class Camera {
	
	private float x, y;
	
	public Camera(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(GameObject object) {
		x += ((object.getX() - x) - Game.WIDTH/2) * 0.05f;
		y += ((object.getY() - y) - Game.HEIGHT/2) * 0.05f;
//		System.out.println("cam moved");
		if(x <= 0) x = 0;
		if(x >= 800) x = 800;
		if(y <= 0) y = 0;
		if(y >= 600) y = 600;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
