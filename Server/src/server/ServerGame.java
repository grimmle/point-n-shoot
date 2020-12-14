package server;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import common.Block;
import common.Bullet;
import common.DynamicObjectsUpdateMsg;
import common.GameObject;
import common.ID;
import common.MovePlayerMsg;
import common.OpenSimplex2F;
import common.Pickup;
import common.Player;

public class ServerGame implements Runnable {
	
	private volatile boolean isRunning = false;
	private Thread thread;
	
	public static ArrayList<GameObject> staticMap = new ArrayList<GameObject>();
	public static CopyOnWriteArrayList<GameObject> dynamicObjects = new CopyOnWriteArrayList<GameObject>();
	public static ArrayList<Player> players = new ArrayList<Player>();
	
	public boolean playersUpdated = false;
	public boolean dynamicsUpdated = false;

	void start() {
		initGame();
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
	
	private void initGame() {
		staticMap.add(new Block(80, 250));
		staticMap.add(new Block(320, 760));
		staticMap.add(new Block(580, 20));
		dynamicObjects.add(new Pickup(150, 250));
		dynamicObjects.add(new Pickup(550, 250));
		dynamicObjects.add(new Pickup(275, 300));
		dynamicObjects.add(new Pickup(295, 300));
		dynamicObjects.add(new Pickup(550, 500));
//		long seed = 0;
//		OpenSimplex2F OS = new OpenSimplex2F(seed);
//		OS.noise2(0, 0);
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
					int newX = (int) (p.getX() + p.getVelX());
					int newY = (int) (p.getY() + p.getVelY());
					playersUpdated = true;

					for(GameObject obj : staticMap) {
						if(p.getBounds().intersects(obj.getBounds())) {
							//collision; dont move
							System.out.println("COLLISION!!!");
							p.setX((int) (p.getX() + (p.getVelX() * -1)));
							p.setY((int) (p.getY() + (p.getVelY() * -1)));
							break;
						} else {
							p.setX(newX);
							p.setY(newY);
						}
					}
					GameObject hit = null;
					for(GameObject obj : dynamicObjects) {
						if(obj.getId() == ID.Pickup) {
							if(p.getBounds().intersects(obj.getBounds())) {
								System.out.println("PICKUP");
								hit = obj;
								dynamicsUpdated = true;
								p.addBuff(0.5f);	
							}
						}
					}
					if(hit != null) dynamicObjects.remove(hit);
				}
			}
			for(GameObject obj : dynamicObjects) {
				if(obj.getId() == ID.Bullet) {
					if((System.currentTimeMillis() - ((Bullet)obj).timestamp) > 1000) {
						dynamicObjects.remove(obj);
						System.out.println("REMOVED");
					} else {
						System.out.println("BULLET");
						int newX = (int) (obj.getX() + obj.getVelX());
						int newY = (int) (obj.getY() + obj.getVelY());
						obj.setX(newX);
						obj.setY(newY);
						for(Player p : players) {
							if(((Bullet) obj).id != p.id) {
								if(p.getBounds().intersects(obj.getBounds())) {
									p.setHealth(p.getHealth() - 20);
									if(p.getHealth() <= 0) {
										System.out.println("KILLED");
										p.setX(0);
										p.setY(0);
										p.setHealth(100);
									}
									playersUpdated = true;
									dynamicObjects.remove(obj);
									System.out.println("REMOVED");
								}
							}
						}
						for(GameObject o : staticMap) {
							if(obj.getBounds().intersects(o.getBounds())) {
								dynamicObjects.remove(obj);
								System.out.println("REMOVED");
							}
						}
					}
					dynamicsUpdated = true;
				}
			}
		}
	}
	
	public void sendUpdate() {
		if(players.size() > 0) {
			
			Server.connections.forEach((i, connection) -> {
				if(connection.ready) {
					//send player updates
					if(players.get(i) == null) System.out.println(i + "# player not found");
					else if(playersUpdated) {
						MovePlayerMsg move = new MovePlayerMsg();
						move.id = i;
						move.players = ServerGame.players;
//						System.out.println("SEND UPDATE --- connection: " + i + " --- player count: " + players.size() + " --- ");
						connection.sendObject(move);
					}
					//send dynamic objects update
					if(dynamicsUpdated) {
						System.out.println(dynamicObjects.size());
						System.out.println("DYNAMICS CHANGED");
						DynamicObjectsUpdateMsg dyn = new DynamicObjectsUpdateMsg();
						dyn.id = i;
						dyn.content = dynamicObjects;
						connection.sendObject(dyn);
					}
				}
			});
			if(dynamicsUpdated) {
				dynamicsUpdated = false;
			}
			if(playersUpdated) {
				playersUpdated = false;
			}
		}
	}
}
