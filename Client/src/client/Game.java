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
	
	private volatile boolean isRunning = false;
	private Thread thread;
	private Camera camera;
	static Player player;
	
	public static ArrayList<GameObject> staticMap = new ArrayList<GameObject>();
	public static CopyOnWriteArrayList<GameObject> dynamicObjects = new CopyOnWriteArrayList<GameObject>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	
	public OpenSimplex2F OS;
	private int scale = 40;
	int cols = 25;
	int rows = 25;
	private double[][] map;

	private World world;
	
	
	public Game(Player player) {
		new Window(WIDTH, HEIGHT, "Game - Client " + Client.id, this);
		
		Game.player = player;
		camera = new Camera(player.getX(), player.getY());
		
		
		this.addKeyListener(new KeyInput());
		this.addMouseListener(new MouseInput(camera));
		
		// WORLD
		long seed = 42069;		
		world = new World(seed);
		world.checkIfTilesInCache(player.getX(), player.getY());

		
		start();
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
		player.tick();
		camera.tick(players.get(Client.id));
	}
	
	public void render() {
//		System.out.println(OS.noise2(players.get(Client.id).getX(), players.get(Client.id).getX()));
		
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
		
		
		world.render(g);
//		for(int y = 0; y < rows; y++) {
//			for(int x = 0; x < cols; x++) {
//				if(map[x][y] == 255) {
//					//draw box
//					g.setColor(Color.black);
//					g.fillRect(x*scale, y*scale, scale, scale);
//				}
//			}
//		}
		
//		handler.render(g);
		for(GameObject obj : staticMap) {
			obj.render(g);
		}
		for(GameObject obj : dynamicObjects) {
			obj.render(g);
		}
		for(GameObject player : players) {
			player.render(g);
		}
		
		g2d.translate(camera.getX(), camera.getY());
		
		////
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Game g = new Game(new Player(0, 0, 69));
	}
}
