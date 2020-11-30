package client;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import common.*;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	
	private boolean isRunning = false;
	private Thread thread;
	private GameHandler handler;
	private Camera camera;
	
	public Game() {
		new Window(WIDTH, HEIGHT, "Game - Client " + Client.id, this);
		start();
		
		handler = new GameHandler();
		camera = new Camera(0, 0);
		this.addKeyListener(new KeyInput(handler));
		this.addMouseListener(new MouseInput(handler, camera));
		
		handler.addObject(new Player(100,100, handler));
		Client.staticMap.forEach((object) -> {
			handler.addObject(object);
		});
//		handler.addObject(new Block(100, 250));
//		handler.addObject(new Block(320, 760));
//		handler.addObject(new Block(580, 20));
//		handler.addObject(new Pickup(150, 250));
//		handler.addObject(new Pickup(550, 250));
//		handler.addObject(new Pickup(275, 300));
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
		for(int i = 0; i < handler.objects.size(); i++) {
			if(handler.objects.get(i).getId() == ID.Player) {
				camera.tick(handler.objects.get(i));
			}
		}
		handler.tick();
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
		
		
		handler.render(g);
		
		g2d.translate(camera.getX(), camera.getY());
		
		////
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Game();
	}
}
