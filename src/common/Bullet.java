package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends GameObject {
	
	private GameHandler handler;
	private float buff;

	/*
	 * x, y are center of player
	 * mx, my where the player is clicking
	 * */
	public Bullet(int x, int y, ID id, GameHandler handler, int mx, int my, float buff) {
		super(x, y, id);
		this.handler = handler;
		this.buff = buff;
		
		
		int fromX = x;
		int fromY = y;
		int toX = mx;
		int toY = my;
		double distance = Math.sqrt(Math.pow((toX - fromX), 2) + Math.pow((toY - fromY), 2));
	    double speed = 8;
	    velY = (float)((toY - fromY) * speed / distance);
	    velX = (float)((toX - fromX) * speed / distance);
	}

	@Override
	public void tick() {
		x += velX;
		y += velY;
		
		for(int i = 0; i < handler.objects.size(); i++) {
			GameObject tempObject = handler.objects.get(i);
			//delete bullet when it hits a block
			if(tempObject.getId() == ID.Block) {
				if(getBounds().intersects(tempObject.getBounds())) {
					handler.removeObject(this);
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
//		int size = (int) (8*buff);
		g.fillOval(x, y, (int)(8*buff), (int)(8*buff));
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, (int)(8*buff), (int)(8*buff));
	}

}
