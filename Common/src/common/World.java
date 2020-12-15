package common;

import java.awt.Graphics;
import java.util.concurrent.ConcurrentHashMap;

public class World {
    public static final int TILE_SIZE = 500;
    public static final int BLOCK_SIZE = 50;
    public static final int BLOCKS_AMOUNT= TILE_SIZE/BLOCK_SIZE;
    
    private static OpenSimplex2F OS;
    
    public World(long seed) {
    	OS = new OpenSimplex2F(seed);
    }

    //besere Datenstruktur wählen? HashMap z.B.?
    WorldTile[] cachedTiles = new WorldTile[30];
    static ConcurrentHashMap<CoordinatesKey, WorldTile> cache = new ConcurrentHashMap<CoordinatesKey, WorldTile>();
    
    double getZ(int x, int y) {
        int tileX = x / TILE_SIZE;
        int tileY = y / TILE_SIZE;
        int xInsideTile = x % TILE_SIZE;
        int yInsideTile = y % TILE_SIZE;

        for (int i = 0; i < cachedTiles.length; ++i) {
            if (cachedTiles[i].x == tileX && cachedTiles[i].y == tileY) {
                // nutzungszeitstempel aktualisieren
                return cachedTiles[i].z[xInsideTile][yInsideTile];
            }
        }
        // hier Ladebildschirm!! Daten sind noch nicht da!
        throw new RuntimeException("Daten nicht da!");
    }

    
    // Aufrufen z.B. bei Bewegungsnachrichten
    public static void checkIfTilesInCache(int playerX, int playerY) {
        int tileX = playerX / TILE_SIZE;
        int tileY = playerY / TILE_SIZE;
//        System.out.println("tileX: " + tileX + " tileY: " + tileY);
        
        // suche, ob Kachel mit Koordinaten kachelX-1, kachelY im Cache enthalten ist
        // falls nicht -> generieren
        // analog für rechts, oben, unten bzw. 3x3 Feld
        
        if(getCache().get(new CoordinatesKey(tileX-1, tileY-1)) == null) generateTile(tileX-1, tileY-1);
        if(getCache().get(new CoordinatesKey(tileX, tileY-1)) == null) generateTile(tileX, tileY-1);
        if(getCache().get(new CoordinatesKey(tileX+1, tileY-1)) == null) generateTile(tileX+1, tileY-1);
        
        if(getCache().get(new CoordinatesKey(tileX-1, tileY)) == null) generateTile(tileX-1, tileY);
        if(getCache().get(new CoordinatesKey(tileX, tileY)) == null) generateTile(tileX, tileY); // players current tile
        if(getCache().get(new CoordinatesKey(tileX+1, tileY)) == null) generateTile(tileX+1, tileY);
        
        if(getCache().get(new CoordinatesKey(tileX-1, tileY+1)) == null) generateTile(tileX-1, tileY+1);
        if(getCache().get(new CoordinatesKey(tileX, tileY+1)) == null) generateTile(tileX, tileY+1);
        if(getCache().get(new CoordinatesKey(tileX+1, tileY+1)) == null) generateTile(tileX+1, tileY+1);
    }

    
    private static void generateTile(int tileX, int tileY) {
        //Bereich laden / generieren oder Mischung aus beidem
//    	System.out.println("generating tile: " + tileX + ", " + tileY);
    	
    	WorldTile tile = new WorldTile(tileX, tileY);
    	
    	tile.z = new double[BLOCKS_AMOUNT][BLOCKS_AMOUNT];
    	
    	float yoff = tileY + 0.0f;
		for(int y = 0; y < BLOCKS_AMOUNT; y++) {
			float xoff = tileX + 0.0f;
			for(int x = 0; x < BLOCKS_AMOUNT; x++) {
				double out = map(OS.noise2(xoff, yoff), -1, 1, 0, 255);
//				System.out.println("VALUE @ x:" + x + ", y: " + y + " -> " + out);
				tile.z[x][y] = out > 190 ? 255 : 0;
				if(tile.z[x][y] == 255) {
					tile.blocks.add(new Block(tileX*TILE_SIZE + x*BLOCK_SIZE, tileY*TILE_SIZE + y*BLOCK_SIZE, BLOCK_SIZE));
					tile.obstacles.insert(tileX*TILE_SIZE + x*BLOCK_SIZE, tileY*TILE_SIZE + y*BLOCK_SIZE, new Block(tileX*TILE_SIZE + x*BLOCK_SIZE, tileY*TILE_SIZE + y*BLOCK_SIZE, BLOCK_SIZE));
				}
				
				xoff += 0.1;
			}
			yoff += 0.1;
		}
		int tries = 0;
		while(tries < 25) {
			int min = 0;
		    int max = BLOCKS_AMOUNT;
		    int xValue = (int) (min + Math.random() * (max - min));
		    int yValue = (int) (min + Math.random() * (max - min));
		    if(tile.z[xValue][yValue] == 0) {
		    	tile.pickup = new Pickup(tileX*TILE_SIZE + xValue*BLOCK_SIZE, tileY*TILE_SIZE + yValue*BLOCK_SIZE);
		    	break;
		    }
		    tries++;
		}
		cache.put(new CoordinatesKey(tileX, tileY), tile);
    }
    
    private static double map(double value, int start1, int stop1, int start2, int stop2) {
		return start2 + (stop2 - start2) * (value - start1) / (stop1 - start1);
	}
    
    public void render(Graphics g) {
//    	System.out.println("world render");
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