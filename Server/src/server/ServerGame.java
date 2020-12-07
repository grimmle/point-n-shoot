package server;
import java.util.ArrayList;

import common.*;

public class ServerGame implements Runnable {
	
	private volatile boolean isRunning = false;
	private Thread thread;
	
	public static ArrayList<GameObject> staticMap = new ArrayList<GameObject>();
	public static ArrayList<GameObject> movingObjects = new ArrayList<GameObject>();
	public static ArrayList<Player> players = new ArrayList<Player>();

	void start() {
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
				delta--;
				tick();
			}
			sendUpdate();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frames = 0;
			}
		}
		stop();
	}
	
	public void tick() {
		//update players positions & other stats
		if(players.size() > 0) {
			for(Player p : players) {
				if(p.getVelX() != 0 || p.getVelY() != 0) {
					p.moved = true;
				}
				p.setX((int) (p.getX() + p.getVelX()));
				p.setY((int) (p.getY() + p.getVelY()));
			}
		}
	}
	
	public void sendUpdate() {
		boolean stateChanged = false;
		if(players.size() > 0) {
			for(Player p : players) {
				if(p.moved) {
					p.moved = false;
					stateChanged = true;
					break;
				}
			}
			final boolean state = stateChanged;
			Server.connections.forEach((i, connection) -> {
				if(connection.ready) {
					
					if(players.get(i) == null) System.out.println(i + "# player not found");
					else {
						System.out.println(i + " bims hier");
						
						if(state) {
							MovePlayerMsg move = new MovePlayerMsg();
							move.id = i;
							move.players = ServerGame.players;
							System.out.println("connection: " + i + " --- player count: " + players.size() + " --- SEND UPDATE");
							connection.sendObject(move);
						}
					}
				}
			});
		}
	}
}
