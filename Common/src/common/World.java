package common;

import java.awt.Graphics;
import java.util.concurrent.ConcurrentHashMap;

import util.Helper;

public class World {
    public static final int TILE_SIZE = 500;
    public static final int BLOCK_SIZE = 50;
    public static final int BLOCKS_AMOUNT= TILE_SIZE/BLOCK_SIZE;
    
    private static OpenSimplex2F OS;
    
    public World(long seed) {
    	OS = new OpenSimplex2F(seed);
    }

    //TODO: add actual cache functionality for clients
    static ConcurrentHashMap<CoordinatesKey, WorldTile> cache = new ConcurrentHashMap<CoordinatesKey, WorldTile>();
    static LRUCache lru = new LRUCache(9);
    
    public static void checkIfTilesInCache(int playerX, int playerY) {
        int tileX = playerX / TILE_SIZE;
        int tileY = playerY / TILE_SIZE;
        
        if(cache.get(new CoordinatesKey(tileX-1, tileY-1)) == null) generateTile(tileX-1, tileY-1);
        if(cache.get(new CoordinatesKey(tileX, tileY-1)) == null) generateTile(tileX, tileY-1);
        if(cache.get(new CoordinatesKey(tileX+1, tileY-1)) == null) generateTile(tileX+1, tileY-1);
        
        if(cache.get(new CoordinatesKey(tileX-1, tileY)) == null) generateTile(tileX-1, tileY);
        if(cache.get(new CoordinatesKey(tileX, tileY)) == null) generateTile(tileX, tileY); // players current tile
        if(cache.get(new CoordinatesKey(tileX+1, tileY)) == null) generateTile(tileX+1, tileY);
        
        if(cache.get(new CoordinatesKey(tileX-1, tileY+1)) == null) generateTile(tileX-1, tileY+1);
        if(cache.get(new CoordinatesKey(tileX, tileY+1)) == null) generateTile(tileX, tileY+1);
        if(cache.get(new CoordinatesKey(tileX+1, tileY+1)) == null) generateTile(tileX+1, tileY+1);
    }

    
    private static void generateTile(int tileX, int tileY) {
    	WorldTile tile = new WorldTile(tileX, tileY);
    	tile.z = new double[BLOCKS_AMOUNT][BLOCKS_AMOUNT];
    	
    	//GENERATE BLOCKS FROM NOISE
    	float yoff = tileY + 0.0f;
		for(int y = 0; y < BLOCKS_AMOUNT; y++) {
			float xoff = tileX + 0.0f;
			for(int x = 0; x < BLOCKS_AMOUNT; x++) {
				double out = Helper.map(OS.noise2(xoff, yoff), -1, 1, 0, 255);
				tile.z[x][y] = out > 185 ? 255 : 0;
				if(tile.z[x][y] == 255) {
					tile.obstacles.insert(new Block(tileX*TILE_SIZE + x*BLOCK_SIZE, tileY*TILE_SIZE + y*BLOCK_SIZE, BLOCK_SIZE));
				}
				xoff += 0.1;
			}
			yoff += 0.1;
		}
		
		//GENERATE PICKUP
		//TODO: think of a better way (dynamic events)
		if(Math.random() > 0.5) {
			double rnd = Math.random();
			String effect;
			if(rnd < 0.15) effect = "agent";
			else if(rnd > 0.85) effect = "health";
			else effect = "buff";
			
			for(int i = 0; i < 5; i++) {
				int min = 0;
			    int max = BLOCKS_AMOUNT;
			    int xValue = (int) (min + Math.random() * (max - min));
			    int yValue = (int) (min + Math.random() * (max - min));
			    if(tile.z[xValue][yValue] == 0) {
			    	tile.pickup = new Pickup(tileX*TILE_SIZE + xValue*BLOCK_SIZE, tileY*TILE_SIZE + yValue*BLOCK_SIZE, effect);
			    	break;
			    }
			}
		}
		cache.put(new CoordinatesKey(tileX, tileY), tile);
    }
    
    public void render(Graphics g) {
    	for(WorldTile tile : getCache().values()) {
    		tile.render(g);
    	}
    }
    
    public static WorldTile getTileAt(int x, int y) {
    	int tileX = x / World.TILE_SIZE;
        int tileY = y / World.TILE_SIZE;
    	return cache.get(new CoordinatesKey(tileX, tileY));
    }

	public static ConcurrentHashMap<CoordinatesKey, WorldTile> getCache() {
		return cache;
	}
}