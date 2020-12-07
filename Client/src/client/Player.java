package client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import common.*;

public class Player extends GameObject {
	
	private static final long serialVersionUID = 3216281663303148467L;
	
//	GameHandler handler;
	public int id;
	private float buffed = 1.0f;
	private int health = 100;
	
	private boolean up = false, down = false, right = false, left = false;
	

	public Player(int x, int y, int id) {
		super(x, y, ID.Player);
		this.id = id;
//		this.handler = handler;
	}

	@Override
	public void tick() {
		
//		x += velX;
//		y += velY;
		
		//collision();
		
		float tempX = velX;
		float tempY = velY;
		//player movement
		if(up) velY = -5;
		else if(!down) velY = 0;
		
		if(down) velY = 5;
		else if(!up) velY = 0;
		
		if(right) velX = 5;
		else if(!left) velX = 0;
		
		if(left) velX = -5;
		else if(!right) velX = 0;
		
		if(tempX != velX || tempY != velY) {
			System.out.println("vel changed " + velX + " " + velY);
			MovePlayerMsg move = new MovePlayerMsg();
			move.velX = velX;
			move.velY = velY;
			Client.sendObject(move);
		}
		
		
//		if(handler.isUp()) velY = -5;
//		else if(!handler.isDown()) velY = 0;
//		
//		if(handler.isDown()) velY = 5;
//		else if(!handler.isUp()) velY = 0;
//		
//		if(handler.isRight()) velX = 5;
//		else if(!handler.isLeft()) velX = 0;
//		
//		if(handler.isLeft()) velX = -5;
//		else if(!handler.isRight()) velX = 0;
	}
	
//	private void collision() {
//		GameObject tempObject;
//		for(int i = 0; i < handler.objects.size(); i++) {
//			tempObject = handler.objects.get(i);
//			if(tempObject.getId() == ID.Block) {
//				//check if collided with block
//				if(getBounds().intersects(tempObject.getBounds())) {
//					x += velX * -1;
//					y += velY * -1;
//				}
//			} else if(tempObject.getId() == ID.Pickup) {
//				//check if collided with pickup
//				if(getBounds().intersects(tempObject.getBounds())) {
//					buffed += 0.5;
//					handler.removeObject(tempObject);
//				}
//			}
//		}
//		tempObject = null;
//	}
	
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
	
	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
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
