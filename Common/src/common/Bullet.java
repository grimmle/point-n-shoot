package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Date;

public class Bullet extends GameObject {

	private static final long serialVersionUID = 3702825069143883684L;
	
	private int size;
	public int id;
	public long timestamp;

	/*
	 * x, y are center of player
	 * mx, my where the player is clicking
	 * */
	public Bullet(int x, int y, int mx, int my, float buff, int id) {
		super(x, y, ID.Bullet);
		this.size = (int) (8*buff);
		this.id = id;
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
//		x += velX;
//		y += velY;
//		
//		for(int i = 0; i < handler.objects.size(); i++) {
//			GameObject tempObject = handler.objects.get(i);
//			//delete bullet when it hits a block
//			if(tempObject.getId() == ID.Block) {
//				if(getBounds().intersects(tempObject.getBounds())) {
//					handler.removeObject(this);
//				}
//			}
//		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(x, y, size, size);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, size, size);
	}

}
