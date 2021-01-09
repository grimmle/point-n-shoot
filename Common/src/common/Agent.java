package common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import util.Helper;

public class Agent extends GameObject {

	private static final long serialVersionUID = -5932757367571221657L;
	private int size = 20;
	private Color color;
	
	private double MAX_SPEED = 8;
	private double MAX_FORCE = 0.5;
	
	private int MAX_DISTANCE = 500;
	private int MIN_DISTANCE = 30;
	
	private int MAX_SEE_AHEAD = 200;
	private double MAX_AVOID_FORCE = 1.8;
	
	private Vector2D target;
	
	private Vector2D acceleration;
	private Vector2D velocity;
	private Vector2D location;
	
	Vector2D ahead = new Vector2D(0, 0);
	Vector2D ahead2 = new Vector2D(0, 0);
	Vector2D ahead3 = new Vector2D(0, 0);
	
	Vector2D avoid = new Vector2D(0, 0);
	Vector2D steer = new Vector2D(0, 0);
	
	private transient List<Block> surroundings; 


	public Agent(int x, int y, Color color) {
		super(x, y, TYPE.Agent);
		this.color = color;
		acceleration = new Vector2D(0, 0);
		velocity = new Vector2D(0, -1);
		location = new Vector2D(x, y-1);
		target = new Vector2D(x, y);
	}

	@Override
	public void tick() {
		//seek()
		steer = arrive(target);
		
		//force()
		applyForce(steer);
		//update()
		location.add(velocity);
//		System.out.println("agent location: " + location.x + " " + location.y);
		velocity.add(acceleration);
		//clear acceleration
		acceleration.multiply(0);
	}

	@Override
	public void render(Graphics g) {
		int offsetX = (int)location.x-(size/2);
		int offsetY = (int)location.y-(size/2);
		
		g.setColor(color);
		g.fillOval(offsetX, offsetY, size, size);
		
		//SHOW AHEAD VECTORS
//		g.setColor(Color.red);
//		g.drawLine((int)location.x, (int)location.y, (int)ahead.x, (int)ahead.y);
//		g.setColor(Color.green);
//		g.drawLine((int)location.x, (int)location.y, (int)ahead2.x, (int)ahead2.y);
//		g.setColor(Color.blue);
//		g.drawLine((int)location.x, (int)location.y, (int)ahead3.x, (int)ahead3.y);
		
		//SHOW STEERING VECTOR
//		g.setColor(Color.magenta);
//		Vector2D s = location.getAdded(steer);
//		System.out.println("STEER: " + s.x + " " + s.y);
//		g.drawLine((int)location.x, (int)location.y, (int)s.x, (int)s.y);
		
		//SHOW AVOIDANCE VECTOR
//		g.setColor(Color.orange);
//		Vector2D ax = avoid.getMultiplied(100);
//		Vector2D a = location.getAdded(ax);
//		System.out.println("AVOID: " + a.x + " " + a.y);
//		g.drawLine((int)location.x, (int)location.y, (int)a.x, (int)a.y);
	}

	@Override
	public Rectangle getBounds() {
		int offsetX = (int)location.x-(size/2);
		int offsetY = (int)location.y-(size/2);
		
		return new Rectangle(offsetX, offsetY, size, size);
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
		acceleration.add(force);
	}
	
	public Vector2D arrive(Vector2D target) {
		Vector2D desired = Vector2D.subtract(target, location);
		double d = desired.getLength();
		double speed = MAX_SPEED;
		
		if(d <= MIN_DISTANCE) {
			speed = 0;
		} else if(d < MAX_DISTANCE) {
			speed = Helper.map(d, 0, MAX_DISTANCE, 0, MAX_SPEED);
		}
		desired.normalize();
		desired.multiply(speed);
		
		
		Vector2D steer = Vector2D.subtract(desired, velocity);
		avoid = avoid();
		steer.add(avoid);
		
		return steer;
	}
	
//	public Vector2D seek(Vector2D target) {
//		Vector2D desired = Vector2D.subtract(target, location);
//		desired.normalize();
//		desired.multiply(MAX_SPEED);
//		
//		Vector2D steer = Vector2D.subtract(desired, velocity);
//		
//		return steer;
//	}
	
	public Vector2D getAcceleration() {
		return acceleration;
	}
	
	@Override
	public int getX() {
		return (int)location.x;
	}
	
	@Override
	public int getY() {
		return (int)location.y;
	}

	public void updateSurroundings(List<Block> surroundingBlocks) {
		surroundings = surroundingBlocks;
	}
	
	//TODO: collision avoidance
	public Vector2D avoid() {
		Vector2D vel = velocity.getLength() != 0.0 ? velocity.getNormalized() : velocity;
		ahead = vel.getMultiplied(MAX_SEE_AHEAD);
		
//		double dynamic_length = vel.getLength();
//		ahead = vel.getMultiplied(dynamic_length);
		
		ahead2 = ahead.getMultiplied(0.5);
		ahead3 = ahead.getMultiplied(0.1);
		
		ahead.add(location);
		ahead2.add(location);
		ahead3.add(location);
//		System.out.println("----------------");
//		System.out.println(vel.x + " " + vel.y);
//		System.out.println(ahead.x + " " + ahead.y);
		
		
		
		Block mostThreatening = findMostThreateningObstacle(ahead, ahead2, ahead3);
	    Vector2D avoidance = new Vector2D(0, 0);
	 
	    if (mostThreatening != null) {
	        avoidance = Vector2D.subtract(ahead, mostThreatening.getCenter());
	        
	        avoidance.normalize();
	        avoidance.multiply(MAX_AVOID_FORCE);
	    } else {
	        avoidance.multiply(0);
	    }
//	    System.out.println(mostThreatening);
//	    System.out.println("AVOID: " + avoidance.x + " " + avoidance.y);
		return avoidance;
	}
	
	
	private Block findMostThreateningObstacle(Vector2D ahead, Vector2D ahead2, Vector2D ahead3) {
	    Block mostThreatening = null;
	    for (int i = 0; i < surroundings.size(); i++) {
	        Block obstacle = surroundings.get(i);
	        
	        boolean collision = isIntersecting(ahead, ahead2, ahead3, obstacle);
	        if (collision && (mostThreatening == null || distance(location, obstacle.getCenter()) < distance(location, mostThreatening.getCenter()))) {
	            mostThreatening = obstacle;
	        }
	    }
	    return mostThreatening;
	}
	
	
	private boolean isIntersecting(Vector2D ahead, Vector2D ahead2, Vector2D ahead3, Block obstacle) {
		return distance(obstacle.getCenter(), location) <= obstacle.getOuterRadius() || distance(obstacle.getCenter(), ahead) <= obstacle.getOuterRadius() || distance(obstacle.getCenter(), ahead2) <= obstacle.getOuterRadius() || distance(obstacle.getCenter(), ahead3) <= obstacle.getOuterRadius() || obstacle.getBoundingsSphere().intersects(getBounds());
	}
	
//	private double distance(Vector2D one, Vector2D two) {
//		return Vector2D.subtract(one, two).getLength();
//	}
	
	private double distance(Vector2D a, Vector2D b) {
	    return Math.sqrt((a.x - b.x) * (a.x - b.x)  + (a.y - b.y) * (a.y - b.y));
	}

}
