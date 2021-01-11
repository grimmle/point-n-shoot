package server;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import common.Agent;
import common.Block;
import common.Bullet;
import common.GameObject;
import common.PlayerModel;
import common.TYPE;
import common.Vector2D;
import common.World;
import common.WorldTile;
import messages.DynamicObjectsUpdateMsg;
import messages.MovePlayerMsg;

public class ServerGame implements Runnable {
	
	private volatile boolean isRunning = false;
	private Thread thread;
	
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
				tick(System.currentTimeMillis() - timer);
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
	
	public void tick(long timer) {
		//update players positions & other stats
		if(players.size() > 0) {
			checkPlayerCollision(timer);
			checkBulletCollision();
		}
	}
	
	public void checkPlayerCollision(long timer) {
		for(PlayerModel p : players) {
			if(p.getVelX() != 0 || p.getVelY() != 0) {
				int newX = (int) (p.getX() + p.getVelX());
				int newY = (int) (p.getY() + p.getVelY());
				p.setX(newX);
				p.setY(newY);
				playersUpdated = true;
				
				World.checkIfTilesInCache(newX, newY);
		        WorldTile current = World.getTileAt(newX, newY);
		        WorldTile left = World.getTileAt(newX-World.TILE_SIZE, newY);
		        WorldTile right = World.getTileAt(newX+World.TILE_SIZE, newY);
		        WorldTile up = World.getTileAt(newX, newY-World.TILE_SIZE);
		        WorldTile down = World.getTileAt(newX, newY+World.TILE_SIZE);
		        WorldTile[] tiles = { current, left, right, up, down };
		        
		        //CHECK COLLISION WITH BLOCKS WITHIN 300px
		        Rectangle playerRange = new Rectangle(p.getX()-150, p.getY()-150, 300, 300);
		        //Rectangle playerRange = current.getBounds();
		        List<Block> foundBlocks = new ArrayList<Block>();
		        for (WorldTile tile : tiles) {
		        	foundBlocks.addAll(tile.getObstacles().query(playerRange));
				}
		        
		        //CHECK COLLISION WITH WORLD BLOCKS
				for(GameObject block : foundBlocks) {
					if(p.getBounds().intersects(block.getBounds()) || block.getBounds().intersects(p.getBounds())) {
						System.out.println("COLLISION!!!");
						p.setX((int) (p.getX() + (p.getVelX() * -1)));
						p.setY((int) (p.getY() + (p.getVelY() * -1)));
						break;
					}
				}
				
				//GENERATE PICKUPS AND CHECK FOR COLLISION
				for (WorldTile tile : tiles) {
					GameObject hit = null;
					if(tile.pickup != null) {
						dynamicObjects.addIfAbsent(tile.pickup);
						dynamicsUpdated = true;
						if(p.getBounds().intersects(tile.pickup.getBounds())) {
							System.out.println("PICKUP");
							hit = tile.pickup;
							dynamicsUpdated = true;
							if(p.getBuff() < 5) p.addBuff(0.5f);
							tile.pickup = null;
							if(p.agent == null) p.setAgent(new Agent(p.getX(), p.getY(), p.getColor()));
						}
					}
					if(hit != null) dynamicObjects.remove(hit);
				}
			}
			
			//UPDATE PLAYER AGENT
			updateAgent(p, timer);
		}
	}
	
	public void updateAgent(PlayerModel p, long timer) {
		if(p.agent != null) {
			p.agent.setTarget(p.getX(), p.getY());
			int x = p.agent.getX();
			int y = p.agent.getY();
			
			World.checkIfTilesInCache(x, y);
	        WorldTile current = World.getTileAt(x, y);
	        WorldTile left = World.getTileAt(x-World.TILE_SIZE, y);
	        WorldTile right = World.getTileAt(x+World.TILE_SIZE, y);
	        WorldTile up = World.getTileAt(x, y-World.TILE_SIZE);
	        WorldTile down = World.getTileAt(x, y+World.TILE_SIZE);
	        
	        WorldTile[] tiles = { current, left, right, up, down };
	        int r = p.agent.queryRange;
			Rectangle range = new Rectangle(x-r/2, y-r/2, r, r);
			List<Block> foundBlocks = new ArrayList<Block>();
			for(WorldTile tile : tiles) {
				foundBlocks.addAll(tile.getObstacles().query(range));
			}
			p.agent.updateSurroundings(foundBlocks);
			
			p.agent.tick();
			playersUpdated = true;
			
			int [] closestPlayer = {-1, Integer.MAX_VALUE};
			for(PlayerModel enemy : players) {
				if(p.id == enemy.id) continue;
				
				Vector2D vec = Vector2D.subtract(p.getLocation(), enemy.getLocation());
				double d = vec.getLength();
				if(d < closestPlayer[1]) {							
					closestPlayer = new int[]{ enemy.id, (int)d};
				}
			}

			//SHOOT AT CLOSEST ENEMY IF WITHIN 500px
			if(closestPlayer[1] < 500) {
				if(Math.round(timer/10)*10 % 500 == 0) {
					PlayerModel en = players.get(closestPlayer[0]);
					dynamicObjects.add(new Bullet(p.agent.getX(), p.agent.getY(), en.getX(), en.getY(), 1, p.id, p.getColor()));
				}
			}
		}
	}
	
	public void checkBulletCollision() {
		for(GameObject obj : dynamicObjects) {
			if(obj.getType() == TYPE.Bullet) {
				
				//REMOVE BULLET IF IT HAS TRAVELED A MAX TIME
				if((System.currentTimeMillis() - ((Bullet)obj).timestamp) > 3000) {
					dynamicObjects.remove(obj);
					System.out.println("REMOVED TIME");
				} else {
					int newX = (int) (obj.getX() + obj.getVelX());
					int newY = (int) (obj.getY() + obj.getVelY());
					World.checkIfTilesInCache(newX, newY);
					WorldTile current = World.getTileAt(newX, newY);
					obj.setX(newX);
					obj.setY(newY);
					
					//CHECK COLLISION WITH PLAYERS
					for(PlayerModel p : players) {
						if(((Bullet) obj).id != p.id) {
							WorldTile player = World.getTileAt(p.getX(), p.getY());
							if(current.x == player.x && current.y == player.y) {
								if(p.getBounds().intersects(obj.getBounds())) {
									//int damage = (int) players.get(((Bullet)obj).id).getBuff() * 10;
									int damage = 10;
									p.setHealth(p.getHealth() - damage);
									if(p.getHealth() <= 0) {
										System.out.println("KILLED");
										p.setX(100000);
										p.setY(100000);
										p.setHealth(100);
									}
									playersUpdated = true;
									dynamicObjects.remove(obj);
									System.out.println("REMOVED HIT");
								}
							}
						}
					}
					
					//CHECK COLLISION WITH BLOCKS WITHIN 300px
					Rectangle range = new Rectangle(obj.getX()-150, obj.getY()-150, 300, 300);
					List<Block> foundBlocks = current.getObstacles().query(range);
					
					for(GameObject block : foundBlocks) {
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
			dynamicsUpdated = false;
			playersUpdated = false;
		}
	}
}
