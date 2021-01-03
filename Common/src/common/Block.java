package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block extends GameObject {

	private static final long serialVersionUID = -1660008728728386605L;
	private int size = 32;

	public Block(int x, int y) {
		super(x, y, TYPE.Block);
	}
	
	public Block(int x, int y, int size) {
		super(x, y, TYPE.Block);
		this.size = size;
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(x, y, size, size);
//		g.drawRect(x, y, size, size);
//		g.fillRect(x+size/2, y+size/2, 1, 1);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, size, size);
	}

}
