package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pickup extends GameObject {

	public Pickup(int x, int y) {
		super(x, y, ID.Pickup);
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, 24, 24);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, 24, 24);
	}
}
