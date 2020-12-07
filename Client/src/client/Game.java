package client;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import common.*;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public final static int WIDTH = 800;
	public final static int HEIGHT = 600;
	
	private volatile boolean isRunning = false;
	private Thread thread;
	private GameHandler handler;
	private Camera camera;
	static Player player;
	
	public static ArrayList<GameObject> staticMap = new ArrayList<GameObject>();
	public static ArrayList<GameObject> movingObjects = new ArrayList<GameObject>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	
	public Game(Player player) {
		new Window(WIDTH, HEIGHT, "Game - Client " + Client.id, this);
		start();
		
		handler = new GameHandler();
		camera = new Camera(0, 0);
		Game.player = player;
		
		this.addKeyListener(new KeyInput());
		this.addMouseListener(new MouseInput(handler, camera));
	}
	

	private void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	private void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frames = 0;
			}
		}
		stop();
	}
	
	public void tick() {
//		System.out.println("CLIENT PLAYAZ: " + players.size());
		//check if player moved
		player.tick();
		camera.tick(players.get(Client.id));
//		MovePlayerMsg move = new MovePlayerMsg();
//		move.velX = player.getVelX();
//		move.velY = player.getVelY();
//		Client.sendObject(move);
		//handler.tick();
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		
		//background
		g.setColor(Color.red);
		g.fillRect(0, 0, WIDTH, HEIGHT);
				
		//// DRAW SHIT
		
		g2d.translate(-camera.getX(), -camera.getY());
		
		
//		handler.render(g);
		for(GameObject player : players) {
			player.render(g);
		}
		for(GameObject obj : staticMap) {
			obj.render(g);
		}
		for(GameObject obj : movingObjects) {
			obj.render(g);
		}
		
		g2d.translate(camera.getX(), camera.getY());
		
		////
		g.dispose();
		bs.show();
	}
}
