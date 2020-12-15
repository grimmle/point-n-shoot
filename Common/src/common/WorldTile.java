package common;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class WorldTile {

	int x, y;
    long lastUsed;
    public double z[][];
    Random rand = new Random();
    Color randomColor;
    
    public Pickup pickup;
    ArrayList<Block> blocks = new ArrayList<Block>();
    QuadTree obstacles = new QuadTree(1, new Boundary(x*World.TILE_SIZE, y*World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE));
    
    public WorldTile(int x, int y) {
    	this.x = x;
    	this.y = y;
    	this.lastUsed = new Date().getTime();
    	randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    }
    
    
    void render(Graphics g) {
//    	System.out.println("tile render " + x + ", " + y);
    	int amount = World.BLOCKS_AMOUNT;
    	int tile = World.TILE_SIZE;
    	int size = World.BLOCK_SIZE;
    	
//    	test square
//    	g.setColor(Color.green);
//    	g.fillRect(x*500, y*500, 40, 40);
    	
//    	for(int tileY = 0; tileY < amount; tileY++) {
//			for(int tileX = 0; tileX < amount; tileX++) {
//				if(z[tileX][tileY] == 255) {
//					//draw box and add to quadtree
//					g.setColor(Color.black);
//					g.fillRect(x*tile + tileX*size, y*tile + tileY*size, size, size);
//				}
//			}
//		}
    	
    	for(Block b : blocks) {
    		b.render(g);
    	}
    	
    	g.setColor(Color.white);
    	g.drawRect(x*500, y*500, 500, 500);
    }


	public ArrayList<Block> getBlocks() {
		return blocks;
	}


	public QuadTree getObstacles() {
		return obstacles;
	}
}
