package common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import util.Helper;

public class Agent extends GameObject {

	private static final long serialVersionUID = -5932757367571221657L;
	private int size = 20;
	private Color color;
	
	private double maxspeed = 8;
	private double maxforce = 0.5;
	
	private int maxdistance = 250;
	private int mindistance = 30;
	
	private Vector2D target;
	
	private Vector2D acceleration;
	private Vector2D velocity;
	private Vector2D location;


	public Agent(int x, int y, Color color) {
		super(x, y, TYPE.Agent);
		this.color = color;
		acceleration = new Vector2D(0, 0);
		velocity = new Vector2D(0, 0);
		location = new Vector2D(x, y-5);
		target = new Vector2D(x, y);
//		System.out.println("ACC " + acceleration.x + " " + acceleration.y);
//		System.out.println("VEL " + velocity.x + " " + velocity.y);
//		System.out.println("LOC " + location.x + " " + location.y);
	}

	@Override
	public void tick() {
		//get current player position
//		Vector2D target = Game.players.get(playerId).getLocation();
//		System.out.println("TARGET " + target.x + " " + target.y);
//		System.out.println(location.x + " " + location.y);
		//seek()
		Vector2D steer = arrive(target);
		//force()
		applyForce(steer);
		//update()
		location.add(velocity);
//		System.out.println(location.x + " " + location.y + " added " + velocity.x + " " + velocity.y);
		velocity.add(acceleration);
		//clear acceleration
		acceleration.multiply(0);
	}

	@Override
	public void render(Graphics g) {
//		System.out.println(location.x + " " + location.y);
		g.setColor(color);
		g.fillOval((int)location.x, (int)location.y, size, size);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)location.x, (int)location.y, size, size);
	}
	
	public void setTarget(int x, int y) {
		target.set(x, y);
	}
	
	public void setLocation(Vector2D loc) {
		location.set(loc);
	}
	
	public Vector2D getLocation() {
		return location;
	}
	
	public void applyForce(Vector2D force) {
//		System.out.println("APPLY FORCE: " + force.x + " " + force.y);
		acceleration.add(force);
	}
	
	public Vector2D arrive(Vector2D target) {
		Vector2D desired = Vector2D.subtract(target, location);
		double d = desired.getLength();
		double speed = maxspeed;
		
		if(d <= mindistance) {
			speed = 0;
		} else if(d < maxdistance) {
			speed = Helper.map(d, 0, maxdistance, 0, maxspeed);
		}
		desired.normalize();
		desired.multiply(speed);
		
		Vector2D steer = Vector2D.subtract(desired, velocity);
		return steer;
	}
	
	public Vector2D seek(Vector2D target) {
		Vector2D desired = Vector2D.subtract(target, location);
		desired.normalize();
		desired.multiply(maxspeed);
		
		Vector2D steer = Vector2D.subtract(desired, velocity);
		return steer;
	}
	
	public Vector2D getAcceleration() {
		return acceleration;
	}

}
