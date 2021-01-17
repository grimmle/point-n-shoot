package common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import util.Helper;

public class Agent extends GameObject {

	private static final long serialVersionUID = -5932757367571221657L;
	private final int size = 20;
	private final Color color;
	
	public final int queryRange = 500;
	
	private final double MAX_SPEED = 8;
	private final double MAX_STEER_FORCE = 0.5;
	
	private final int MAX_DISTANCE = 350;
	private final int MIN_DISTANCE = 30;
	
	private final int MAX_SEE_AHEAD = 220;
	private final double MAX_AVOID_FORCE = 2.5;
	
	private Vector2D target;
	private Vector2D acceleration;
	private Vector2D velocity;
	private Vector2D location;
	
	Vector2D ahead = new Vector2D(0, 0);
	Vector2D ahead2 = new Vector2D(0, 0);
	Vector2D ahead3 = new Vector2D(0, 0);
	Vector2D ahead4 = new Vector2D(0, 0);
	
	Vector2D rotated1 = new Vector2D(0, 0);
	Vector2D rotated2 = new Vector2D(0, 0);
	
	Vector2D rotated3 = new Vector2D(0, 0);
	Vector2D rotated4 = new Vector2D(0, 0);
	
	Vector2D avoid = new Vector2D(0, 0);
	Vector2D steer = new Vector2D(0, 0);
	
	private transient List<Block> surroundings; 

	private boolean showVectors = false;

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
		avoid = avoid();
		//force()
		applyForce(steer, avoid);
		//update()
		location.add(velocity);
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
		
		if(showVectors) {
			//SHOW AHEAD VECTORS
			g.setColor(Color.red);
			g.drawLine((int)location.x, (int)location.y, (int)ahead.x, (int)ahead.y);
			g.setColor(Color.green);
			g.drawLine((int)location.x, (int)location.y, (int)rotated1.x, (int)rotated1.y);
			g.drawLine((int)location.x, (int)location.y, (int)rotated2.x, (int)rotated2.y);
			g.drawLine((int)location.x, (int)location.y, (int)rotated3.x, (int)rotated3.y);
			g.drawLine((int)location.x, (int)location.y, (int)rotated4.x, (int)rotated4.y);
			
			g.drawLine((int)location.x, (int)location.y, (int)ahead2.x, (int)ahead2.y);
			g.setColor(Color.blue);
			g.drawLine((int)location.x, (int)location.y, (int)ahead3.x, (int)ahead3.y);
			
			//SHOW QT QUERY RANGE
			g.drawRect((int)location.x-queryRange/2, (int)location.y-queryRange/2, queryRange, queryRange);
			
			//SHOW STEERING VECTOR
	//		g.setColor(Color.magenta);
	//		Vector2D s = location.getAdded(steer);
	//		System.out.println("STEER: " + s.x + " " + s.y);
	//		g.drawLine((int)location.x, (int)location.y, (int)s.x, (int)s.y);
			
			//SHOW AVOIDANCE VECTOR
			g.setColor(Color.orange);
			Vector2D ax = avoid.getMultiplied(100);
			Vector2D a = location.getAdded(ax);
			g.drawLine((int)location.x, (int)location.y, (int)a.x, (int)a.y);
		}
	}

	
	public void applyForce(Vector2D steer, Vector2D avoid) {
		Vector2D force = steer.getAdded(avoid);
		acceleration.add(force);
	}
	
	public Vector2D arrive(Vector2D target) {
		Vector2D desired = Vector2D.subtract(target, location);
		double d = desired.getLength();
		double speed = MAX_SPEED;
		
		double steer_force = MAX_STEER_FORCE;
		
		if(d <= MIN_DISTANCE) {
			speed = 0;
		} else if(d < MAX_DISTANCE) {
			speed = Helper.map(d, 0, MAX_DISTANCE, 0, MAX_SPEED);
			steer_force = d;
		}
		desired.normalize();
		desired.multiply(speed);
		
		Vector2D steer = Vector2D.subtract(desired, velocity);
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
	
	public Vector2D avoid() {
		Vector2D vel = velocity;
		double dynamic_length = velocity.getLength();
		if(dynamic_length != 0.0) {
			vel = velocity.getNormalized();
			ahead.multiply(dynamic_length);
		}
		ahead = vel.getMultiplied(MAX_SEE_AHEAD);
		
		// COLLISION FEELERS
		rotated1 = rotateVectorBy(ahead, 25);
		rotated2 = rotateVectorBy(ahead, -25);
		rotated3 = rotateVectorBy(ahead, 75);
		rotated4 = rotateVectorBy(ahead, -75);
<<<<<<< HEAD
		rotated5 = rotateVectorBy(ahead, 40);
		rotated6 = rotateVectorBy(ahead, -40);
		rotated1.multiply(0.6);
		rotated2.multiply(0.6);
		rotated3.multiply(0.15);
		rotated4.multiply(0.15);
		rotated5.multiply(0.25);
		rotated6.multiply(0.25);
=======
		
		rotated1.multiply(0.5);
		rotated2.multiply(0.5);
		rotated3.multiply(0.2);
		rotated4.multiply(0.2);
>>>>>>> parent of 5eb6315... even more tweaking
		
		ahead2 = ahead.getMultiplied(0.5);
		ahead3 = ahead.getMultiplied(0.25);
		ahead4 = ahead.getMultiplied(0.1);
		
		ahead.add(location);
		ahead2.add(location);
		ahead3.add(location);
		ahead4.add(location);
		rotated1.add(location);
		rotated2.add(location);
		rotated3.add(location);
		rotated4.add(location);
		
		Block mostThreatening = findMostThreateningObstacle();
	    Vector2D avoidance = new Vector2D(0, 0);
	    
	    double avoid_force = MAX_AVOID_FORCE;
	    
	    if (mostThreatening != null) {
	        avoidance.x = ahead4.x - mostThreatening.getCenter().x;
	        avoidance.y = ahead4.y - mostThreatening.getCenter().y;
	        
	        double d = distance(location, mostThreatening.getCenter());
		    if(d < MAX_DISTANCE) {
		    	avoid_force /= d;
		    	avoid_force *= 50;
		    	if(d < 100) {
		    		avoid_force *= 2;
		    	}
		    }
	        avoidance.normalize();
	        avoidance.multiply(avoid_force);
	    } else {
	        avoidance.multiply(0);
	    }
	    
		return avoidance;
	}
	
	
	private Block findMostThreateningObstacle() {
	    Block mostThreatening = null;
	    for (int i = 0; i < surroundings.size(); i++) {
	        Block obstacle = surroundings.get(i);
	        
	        boolean collision = isIntersecting(obstacle);
	        if (collision && (mostThreatening == null || distance(location, obstacle.getCenter()) < distance(location, mostThreatening.getCenter()))) {
	            mostThreatening = obstacle;
	        }
	    }
	    return mostThreatening;
	}
	
	
	private boolean isIntersecting(Block obstacle) {
		return distance(obstacle.getCenter(), location) <= obstacle.getOuterRadius() 
				|| distance(obstacle.getCenter(), ahead) <= obstacle.getOuterRadius() 
				|| distance(obstacle.getCenter(), ahead2) <= obstacle.getOuterRadius() 
				|| distance(obstacle.getCenter(), ahead3) <= obstacle.getOuterRadius()
				|| distance(obstacle.getCenter(), ahead4) <= obstacle.getOuterRadius()
				|| distance(obstacle.getCenter(), rotated1) <= obstacle.getOuterRadius()
				|| distance(obstacle.getCenter(), rotated2) <= obstacle.getOuterRadius()
				|| distance(obstacle.getCenter(), rotated3) <= obstacle.getOuterRadius()
				|| distance(obstacle.getCenter(), rotated4) <= obstacle.getOuterRadius()
				|| obstacle.getBoundingsSphere().intersects(getBounds());
	}
	
//	private double distance(Vector2D one, Vector2D two) {
//		return Vector2D.subtract(one, two).getLength();
//	}
	
	private Vector2D rotateVectorBy(Vector2D v, double deg) {
		double theta = Math.toRadians(deg);
		double cs = Math.cos(theta);
		double sn = Math.sin(theta);
		double px = v.x * cs - v.y * sn;
		double py = v.x * sn + v.y * cs;
		return new Vector2D(px, py);
	}
	
	private double distance(Vector2D a, Vector2D b) {
	    return Math.sqrt((a.x - b.x) * (a.x - b.x)  + (a.y - b.y) * (a.y - b.y));
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

}
