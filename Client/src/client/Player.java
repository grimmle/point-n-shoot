package client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import common.*;
import messages.MovePlayerMsg;

public class Player extends GameObject {
	
	private static final long serialVersionUID = 3216281663303148467L;
	
	public int id;
	private float buffed = 1.0f;
	private int health = 100;
	private Color color;
	private int size = 30;

	private static boolean up = false, down = false, right = false, left = false;
	
	public Agent agent;

	public Player(int x, int y, int id, Color color) {
		super(x, y, TYPE.Player);
		this.id = id;
		this.color = color;
	}

	@Override
	public void tick() {

		//player movement
		float tempX = velX;
		float tempY = velY;
		
		if(up) velY = -5;
		else if(!down) velY = 0;
		
		if(down) velY = 5;
		else if(!up) velY = 0;
		
		if(right) velX = 5;
		else if(!left) velX = 0;
		
		if(left) velX = -5;
		else if(!right) velX = 0;
		
		if(tempX != velX || tempY != velY) {
			MovePlayerMsg move = new MovePlayerMsg();
			move.velX = velX;
			move.velY = velY;
			Client.sendObject(move);
		}
	}
	
	@Override
	public void render(Graphics g) {
		int offsetX = x-(size/2);
		int offsetY = y-(size/2);
		
		Color x = color;
		int h = (100 - health) / 20;
		for(int i = 0; i < h; i++) {
			x = x.darker();
		}
		g.setColor(x);
		g.fillOval(offsetX, offsetY, size, size);

		if(agent != null) agent.render(g);
	}

	@Override
	public Rectangle getBounds() {
		int offsetX = x-(size/2);
		int offsetY = y-(size/2);
		return new Rectangle(offsetX, offsetY, size, size);
	}
	
	public Vector2D getLocation() {
		return new Vector2D(x, y);
	}
	
	public boolean isUp() {
		return up;
	}

	public static void setUp(boolean up) {
		Player.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public static void setDown(boolean down) {
		Player.down = down;
	}

	public boolean isRight() {
		return right;
	}

	public static void setRight(boolean right) {
		Player.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public static void setLeft(boolean left) {
		Player.left = left;
	}
	
	public void addBuff(float buff) {
		this.buffed += buff;
	}
	
	public float getBuff() {
		return buffed;
	}
	
	public void setBuff(float buff) {
		this.buffed = buff;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}
	
	public void setBuff(int b) {
		this.buffed = b;
	}

}
