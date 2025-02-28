package common;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public abstract class GameObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected int x, y;
	protected float velX = 0, velY = 0;
	protected TYPE type;
	
	public GameObject(int x, int y, TYPE type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	
	public TYPE getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

}
