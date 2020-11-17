package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends GameObject {
	
	GameHandler handler;
	private float buffed = 1.0f;
	private int health = 100;
	

	public Player(int x, int y, GameHandler handler) {
		super(x, y, ID.Player);
		this.handler = handler;
	}

	@Override
	public void tick() {
		
		x += velX;
		y += velY;
		
		collision();		
		
		//player movement
		if(handler.isUp()) velY = -5;
		else if(!handler.isDown()) velY = 0;
		
		if(handler.isDown()) velY = 5;
		else if(!handler.isUp()) velY = 0;
		
		if(handler.isRight()) velX = 5;
		else if(!handler.isLeft()) velX = 0;
		
		if(handler.isLeft()) velX = -5;
		else if(!handler.isRight()) velX = 0;
	}
	
	private void collision() {
		GameObject tempObject;
		for(int i = 0; i < handler.objects.size(); i++) {
			tempObject = handler.objects.get(i);
			if(tempObject.getId() == ID.Block) {
				//check if collided with block
				if(getBounds().intersects(tempObject.getBounds())) {
					x += velX * -1;
					y += velY * -1;
				}
			} else if(tempObject.getId() == ID.Pickup) {
				//check if collided with pickup
				if(getBounds().intersects(tempObject.getBounds())) {
					buffed += 0.5;
					handler.removeObject(tempObject);
				}
			}
		}
		tempObject = null;
	}
	@Override
	public void render(Graphics g) {
//		g.setColor(Color.green);
//		g.fillRect(x,  y,  30,  30);
		g.setColor(Color.white);
		g.fillOval(x, y, 30, 30);
		//g.fillPolygon(new int[] {x, x+15, x+30}, new int[] {y+30, y, y+30}, 3);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, 30, 30);
	}
	
	public float getBuff() {
		return buffed;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

}
