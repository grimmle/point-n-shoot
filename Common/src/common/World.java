package common;

import java.awt.Graphics;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import util.Helper;

public class World {
    public static final int TILE_SIZE = 500;
    public static final int BLOCK_SIZE = 50;
    public static final int BLOCKS_AMOUNT= TILE_SIZE/BLOCK_SIZE;
    
    private static OpenSimplex2F OS;
    
    //true for client
    private static boolean cleanCache = false;
    static ConcurrentHashMap<CoordinatesKey, WorldTile> cache = new ConcurrentHashMap<CoordinatesKey, WorldTile>();
    
    
    public World(long seed) {
    	OS = new OpenSimplex2F(seed);
    }
    
    public World(long seed, boolean clean) {
    	OS = new OpenSimplex2F(seed);
    	cleanCache = clean;
    }

    
    public static void checkIfTilesInCache(int playerX, int playerY) {
        int tileX = playerX / TILE_SIZE;
        int tileY = playerY / TILE_SIZE;
        
        checkIfTileInCache(tileX-1, tileY-1);
        checkIfTileInCache(tileX, tileY-1);
        checkIfTileInCache(tileX+1, tileY-1);
        
        checkIfTileInCache(tileX-1, tileY);
        checkIfTileInCache(tileX, tileY); // players current tile
        checkIfTileInCache(tileX+1, tileY);
        
        checkIfTileInCache(tileX-1, tileY+1);
        checkIfTileInCache(tileX, tileY+1);
        checkIfTileInCache(tileX+1, tileY+1);
    }
    
    private static void checkIfTileInCache(int tileX, int tileY) {
    	WorldTile tile = cache.get(new CoordinatesKey(tileX, tileY));
    	if(tile == null) generateTile(tileX, tileY);
        else updateLastUsed(tile);
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
		
		//CLEAN CACHE
		if(cleanCache) {
	        if(cache.size() > 25) {
	        	WorldTile oldest = tile;
	        	CoordinatesKey key = null;
	        	for (Entry<CoordinatesKey, WorldTile> entry : cache.entrySet()) {
	        	    if(entry.getValue().lastUsed < oldest.lastUsed) {
	        	    	oldest = entry.getValue();
	        	    	key = entry.getKey();
	        	    }
	        	}
	        	if(key != null) cache.remove(key);
	        }
        }
    }
    
    public void render(Graphics g) {
    	for(WorldTile tile : getCache().values()) {
    		tile.render(g);
    	}
    }
    
    public static WorldTile getTileAt(int x, int y) {
    	int tileX = x / World.TILE_SIZE;
        int tileY = y / World.TILE_SIZE;
        WorldTile tile = cache.get(new CoordinatesKey(tileX, tileY));
        updateLastUsed(tile);
    	return tile;
    }
    
    private static void updateLastUsed(WorldTile tile) {
    	tile.lastUsed = new Date().getTime();
    }

	public static ConcurrentHashMap<CoordinatesKey, WorldTile> getCache() {
		return cache;
	}
}