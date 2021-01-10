package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

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
		
		//SHOW BOUNDING SHERE
//		g.drawOval((int)getCenter().x - getOuterRadius(), (int)getCenter().y -  getOuterRadius(), getOuterRadius()*2, getOuterRadius()*2);
//		g.drawRect(x, y, size, size);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, size, size);
	}
	
	public Shape getBoundingsSphere() {
		return new Ellipse2D.Double(getCenter().x - getOuterRadius(), getCenter().y -  getOuterRadius(), getOuterRadius()*2, getOuterRadius()*2);
	}
	
	public Vector2D getCenter() {
		return new Vector2D(x + World.BLOCK_SIZE/2, y + World.BLOCK_SIZE/2);
	}
	
	public int getOuterRadius() {
		return (int)((size) * Math.sqrt(2)) / 2;
	}
}
