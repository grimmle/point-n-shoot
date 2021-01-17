package common;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pickup extends GameObject {

	private static final long serialVersionUID = -198770244994707947L;
	private double angle = 0;
	private String effect;
	private Color color = Color.white;

	public Pickup(int x, int y, String effect) {
		super(x, y, TYPE.Pickup);
		this.effect = effect;
		if(effect == "agent") color = new Color(255, 91, 0);
		if(effect == "health") color = new Color(19, 145, 20);
	}

	@Override
	public void tick() {}

	@Override
	public void render(Graphics g) {
//		angle += 0.001;
//		Graphics2D g2d = (Graphics2D) g.create();
//		AffineTransform old = g2d.getTransform();
//      g2d.rotate(angle, x+12, y+12);           
        //draw stuff or render images it works both
		g.setColor(color);
		g.fillRect(x, y, 24, 24);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, 24, 24);
	}
	
	public String getEffect() {
		return effect;
	}
}
