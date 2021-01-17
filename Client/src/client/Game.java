package client;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import common.*;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public final static int WIDTH = 800;
	public final static int HEIGHT = 600;
	
	private volatile static boolean isRunning = false;
	private static Thread thread;
	private Camera camera;
	
	public static CopyOnWriteArrayList<GameObject> dynamicObjects = new CopyOnWriteArrayList<GameObject>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	
	static Player player;
	private World world;

	
	public Game(long seed) {
		new Window(WIDTH, HEIGHT, "Game - Client " + Client.id, this);

		player = players.get(Client.id);
		camera = new Camera(player.getX() - WIDTH/2, player.getY() - HEIGHT/2);
		
		this.addKeyListener(new KeyInput());
		this.addMouseListener(new MouseInput(camera));
		
		// WORLD	
		world = new World(seed, true);
		World.checkIfTilesInCache(player.getX(), player.getY());

		start();
	}
	
	

	private void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	public static void stop() {
		isRunning = false;
		Client.close();
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
		player = players.get(Client.id);
		player.tick();
		camera.tick(player);
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
		g.setColor(new Color(196, 196, 177));
		g.fillRect(0, 0, WIDTH, HEIGHT);
				
		//// DRAW STUFF
		
		g2d.translate(-camera.getX(), -camera.getY());
		
		
		world.render(g);

		for(GameObject obj : dynamicObjects) {
			obj.render(g);
		}
		for(GameObject player : players) {
			player.render(g);
		}
		
//		g.drawString("HEALTH: " + players.get(Client.id).getHealth(), 100, 100);
		
		g2d.translate(camera.getX(), camera.getY());
		
		////
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Game g = new Game(123);
	}
}
