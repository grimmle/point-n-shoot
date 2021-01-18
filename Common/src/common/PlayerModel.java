package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class PlayerModel extends GameObject {
	
	private static final long serialVersionUID = 8617330445173219602L;
	public int id;
	private Color color;
	private float buffed = 1.0f;
	private int health = 100;
	private boolean up = false, down = false, right = false, left = false;
	private int size = 30;
	
	public Agent agent;

	public PlayerModel(int x, int y, int id, Color color) {
		super(x, y, TYPE.Player);
		this.id = id;
		this.color = color;
	}

	@Override
	public void tick() {
	}
	
	@Override
	public void render(Graphics g) {
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
	
	public void addBuff(float buff) {
		this.buffed += buff;
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

	public Color getColor() {
		return color;
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

	public void setColor(Color c) {
		color = c;
	}

	public void setBuff(int b) {
		this.buffed = b;
	}

}
