package client;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import common.OpenSimplex2F;

public class World {
    public static final int SIZE = 500;
    
    private static OpenSimplex2F OS;
    
    public World(long seed) {
    	OS = new OpenSimplex2F(seed);
    }

    //besere Datenstruktur wählen? HashMap z.B.?
    WorldTile[] cachedTiles = new WorldTile[30];
//    static WorldTile[][] cache = new WorldTile[5][5];
    static ConcurrentHashMap<CoordinatesKey, WorldTile> cache = new ConcurrentHashMap<CoordinatesKey, WorldTile>();
    LinkedHashMap<Integer, String> kek = new LinkedHashMap<>();
    
    double getZ(int x, int y) {
        int tileX = x / SIZE;
        int tileY = y / SIZE;
        int xInsideTile = x % SIZE;
        int yInsideTile = y % SIZE;

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
        int tileX = playerX / SIZE;
        int tileY = playerY / SIZE;
        System.out.println("tileX: " + tileX + " tileY: " + tileY);
        
        // suche, ob Kachel mit Koordinaten kachelX-1, kachelY im Cache enthalten ist
        // falls nicht -> generieren
        // analog für rechts, oben, unten bzw. 3x3 Feld
        	
//        if(cache[tileX-1][tileY-1] == null) generateTile(tileX-1, tileY-1);
//        if(cache[tileX][tileY-1] == null) generateTile(tileX, tileY-1);
//        if(cache[tileX+1][tileY-1] == null) generateTile(tileX+1, tileY-1);
//        
//        if(cache[tileX-1][tileY] == null) generateTile(tileX-1, tileY);
//        if(cache[tileX][tileY] == null) generateTile(tileX, tileY);
//        if(cache[tileX+1][tileY] == null) generateTile(tileX+1, tileY);
//        
//        if(cache[tileX-1][tileY+1] == null) generateTile(tileX-1, tileY+1);
//        if(cache[tileX][tileY+1] == null) generateTile(tileX, tileY+1);
//        if(cache[tileX+1][tileY+1] == null) generateTile(tileX+1, tileY+1);
        
//        if(cache.get(new CoordinatesKey(tileX-1, tileY-1)) == null) generateTile(tileX-1, tileY-1);
//        if(cache.get(new CoordinatesKey(tileX, tileY-1)) == null) generateTile(tileX, tileY-1);
//        if(cache.get(new CoordinatesKey(tileX+1, tileY-1)) == null) generateTile(tileX+1, tileY-1);
//        
//        if(cache.get(new CoordinatesKey(tileX-1, tileY)) == null) generateTile(tileX-1, tileY);
        if(cache.get(new CoordinatesKey(tileX, tileY)) == null) generateTile(tileX, tileY); // players current tile
//        if(cache.get(new CoordinatesKey(tileX+1, tileY)) == null) generateTile(tileX+1, tileY);
//        
//        if(cache.get(new CoordinatesKey(tileX-1, tileY+1)) == null) generateTile(tileX-1, tileY+1);
//        if(cache.get(new CoordinatesKey(tileX, tileY+1)) == null) generateTile(tileX, tileY+1);
//        if(cache.get(new CoordinatesKey(tileX+1, tileY+1)) == null) generateTile(tileX+1, tileY+1);
    }

    
    private static void generateTile(int tileX, int tileY) {
        //Bereich laden / generieren oder Mischung aus beidem
    	System.out.println("generating tile: " + tileX + ", " + tileY);
    	
    	WorldTile tile = new WorldTile(tileX, tileY);
    	tile.z = new double[SIZE/20][SIZE/50];
    	
    	float yoff = tileY + 0.0f;
		for(int y = 0; y < SIZE/50; y++) {
			float xoff = tileX + 0.0f;
			for(int x = 0; x < SIZE/20; x++) {
				double out = map(OS.noise2(xoff, yoff), -1, 1, 0, 255);
//				System.out.println("VALUE @ x:" + x + ", y: " + y + " -> " + out);
				tile.z[x][y] = out > 190 ? 255 : 0;
				xoff += 0.1;
			}
			yoff += 0.1;
		}

		cache.put(new CoordinatesKey(tileX, tileY), tile);
    }
    
    private static double map(double value, int start1, int stop1, int start2, int stop2) {
		return start2 + (stop2 - start2) * (value - start1) / (stop1 - start1);
	}
    
    void render(Graphics g) {
//    	System.out.println("world render");
    	for(WorldTile tile : cache.values()) {
    		tile.render(g);
    	}
    }
}