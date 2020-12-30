package server;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import common.Bullet;
import common.GameObject;
import common.TYPE;
import common.PlayerModel;
import common.World;
import common.WorldTile;
import messages.DynamicObjectsUpdateMsg;
import messages.MovePlayerMsg;

public class ServerGame implements Runnable {
	
	private volatile boolean isRunning = false;
	private Thread thread;
	
	public static ArrayList<GameObject> staticMap = new ArrayList<GameObject>();
	public static CopyOnWriteArrayList<GameObject> dynamicObjects = new CopyOnWriteArrayList<GameObject>();
	public static ArrayList<PlayerModel> players = new ArrayList<PlayerModel>();
	
	public final static long SEED = 42069;
	World world;
	
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
//		staticMap.add(new Block(80, 250));
//		staticMap.add(new Block(320, 760));
//		staticMap.add(new Block(580, 20));
//		dynamicObjects.add(new Pickup(150, 250));
//		dynamicObjects.add(new Pickup(550, 250));
//		dynamicObjects.add(new Pickup(275, 300));
//		dynamicObjects.add(new Pickup(295, 300));
//		dynamicObjects.add(new Pickup(550, 500));

		world = new World(SEED);
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
			//check player collision
			for(PlayerModel p : players) {
				if(p.getVelX() != 0 || p.getVelY() != 0) {
					int newX = (int) (p.getX() + p.getVelX());
					int newY = (int) (p.getY() + p.getVelY());
					p.setX(newX);
					p.setY(newY);
					playersUpdated = true;
					
					World.checkIfTilesInCache(newX, newY);
			        ArrayList<GameObject> blocks = new ArrayList<GameObject>();
			        WorldTile current = World.getTileAt(newX, newY);
			        WorldTile left = World.getTileAt(newX-1, newY);
			        WorldTile right = World.getTileAt(newX+1, newY);
			        WorldTile up = World.getTileAt(newX, newY-1);
			        WorldTile down = World.getTileAt(newX, newY+1);
			        WorldTile[] surroundingTiles = { left, right, up, down };
			        
			        blocks.addAll(current.getBlocks());
			        for (WorldTile tile : surroundingTiles) {
						blocks.addAll(tile.getBlocks());
					}
//			        QuadTree.dfs(playerTile.getObstacles());
			        
					for(GameObject block : blocks) {
						if(p.getBounds().intersects(block.getBounds())) {
							System.out.println("COLLISION!!!");
							p.setX((int) (p.getX() + (p.getVelX() * -1)));
							p.setY((int) (p.getY() + (p.getVelY() * -1)));
							break;
						}
					}
					
					
//					for(GameObject obj : dynamicObjects) {
//						if(obj.getId() == ID.Pickup) {
//							if(p.getBounds().intersects(obj.getBounds())) {
//								System.out.println("PICKUP");
//								hit = obj;
//								dynamicsUpdated = true;
//								p.addBuff(0.5f);	
//							}
//						}
//					}
					for (WorldTile tile : surroundingTiles) {
						GameObject hit = null;
						if(tile.pickup != null) {
							dynamicObjects.addIfAbsent(tile.pickup);
							dynamicsUpdated = true;
							if(p.getBounds().intersects(tile.pickup.getBounds())) {
								System.out.println("PICKUP");
								hit = tile.pickup;
								dynamicsUpdated = true;
								if(p.getBuff() < 3) p.addBuff(0.5f);
								tile.pickup = null;
							}
						}
						if(hit != null) dynamicObjects.remove(hit);
					}
					
				}
			}
			//check bullet collision
			for(GameObject obj : dynamicObjects) {
				if(obj.getType() == TYPE.Bullet) {
					if((System.currentTimeMillis() - ((Bullet)obj).timestamp) > 3000) {
						dynamicObjects.remove(obj);
						System.out.println("REMOVED");
					} else {
//						System.out.println("BULLET");
						int newX = (int) (obj.getX() + obj.getVelX());
						int newY = (int) (obj.getY() + obj.getVelY());
						World.checkIfTilesInCache(newX, newY);
						obj.setX(newX);
						obj.setY(newY);
						for(PlayerModel p : players) {
							if(((Bullet) obj).id != p.id) {
								if(p.getBounds().intersects(obj.getBounds())) {
									p.setHealth(p.getHealth() - 20);
									if(p.getHealth() <= 0) {
										System.out.println("KILLED");
										p.setX(100000);
										p.setY(100000);
										p.setHealth(100);
									}
									playersUpdated = true;
									dynamicObjects.remove(obj);
									System.out.println("REMOVED");
								}
							}
						}
						WorldTile currentTile = World.getTileAt(newX, newY);
						for(GameObject block : currentTile.getBlocks()) {
							if(obj.getBounds().intersects(block.getBounds())) {
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
//						System.out.println(dynamicObjects.size());
//						System.out.println("DYNAMICS CHANGED");
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
