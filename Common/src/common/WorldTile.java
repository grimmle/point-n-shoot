package common;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WorldTile {

	public int x, y;
    long lastUsed;
    public double z[][];
    Random rand = new Random();
    Color randomColor;
    
    public Pickup pickup;
    List<Block> blocks = new ArrayList<Block>();
    QuadTree obstacles; 
    
    public WorldTile(int x, int y) {
    	this.x = x;
    	this.y = y;
    	this.lastUsed = new Date().getTime();
    	randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    	obstacles = new QuadTree(new Rectangle(x*World.TILE_SIZE, y*World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE));
    }
    
    
    void render(Graphics g) {
    	Rectangle range = new Rectangle(x*World.TILE_SIZE, y*World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
    	blocks = obstacles.query(range);
    	for(Block b : blocks) {
    		b.render(g);
    	}
    	
    	g.setColor(Color.white);
    	g.drawRect(x*500, y*500, 500, 500);
    }


	public List<Block> getBlocks() {
		return blocks;
	}

	public QuadTree getObstacles() {
		return obstacles;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x * World.TILE_SIZE, y * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
	}
}
