package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Date;

public class Bullet extends GameObject {

	private static final long serialVersionUID = 3702825069143883684L;
	
	public int id;
	private int size;
	private Color color;
	public long timestamp;

	/*
	 * x, y are center of player
	 * mx, my where the player is clicking
	 * */
	public Bullet(int x, int y, int mx, int my, float buff, int id, Color color) {
		super(x, y, TYPE.Bullet);
		this.id = id;
		this.size = (int) (8*buff);
		this.color = color;
		this.timestamp = new Date().getTime();
		
		int fromX = (int) (x-(buff/2));
		int fromY = (int) (y-(buff/2));
		int toX = mx;
		int toY = my;
		double distance = Math.sqrt(Math.pow((toX - fromX), 2) + Math.pow((toY - fromY), 2));
	    double speed = 8;
	    velY = (float)((toY - fromY) * speed / distance);
	    velX = (float)((toX - fromX) * speed / distance);
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
		int offsetX = x-(size/2);
		int offsetY = y-(size/2);
		
		g.setColor(color);
		g.fillOval(offsetX, offsetY, size, size);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, size, size);
	}

}
