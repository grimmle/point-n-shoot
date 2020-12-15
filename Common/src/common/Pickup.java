package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Pickup extends GameObject {

	private static final long serialVersionUID = -198770244994707947L;
	private double angle = 0;

	public Pickup(int x, int y) {
		super(x, y, TYPE.Pickup);
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
//		angle += 0.001;
//		Graphics2D g2d = (Graphics2D) g.create();
//		AffineTransform old = g2d.getTransform();
//        g2d.rotate(angle, x+12, y+12);           
        //draw stuff or render images it works both
		g.setColor(Color.white);
		g.fillRect(x, y, 24, 24);
        
//        g2d.setTransform(old);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, 24, 24);
	}
}
