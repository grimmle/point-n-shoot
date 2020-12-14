package client;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;
import java.util.Random;

public class WorldTile {

	int x, y;
    long lastUsed;
    public double z[][];
    Random rand = new Random();
    Color randomColor;
    
//    private QuadTree qt;
    
    public WorldTile(int x, int y) {
    	this.x = x;
    	this.y = y;
    	this.lastUsed = new Date().getTime();
    	randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    }
    
    
    void render(Graphics g) {
//    	System.out.println("tile render " + x + ", " + y);
    	int scale = World.SIZE/50;
    	
    	g.setColor(Color.green);
    	g.fillRect(x*500, y*500, 40, 40);
    	for(int tileY = 0; tileY < scale; tileY++) {
			for(int tileX = 0; tileX < scale; tileX++) {
				if(z[tileX][tileY] == 255) {
					//draw box and add to quadtree
					g.setColor(randomColor);
					g.fillRect(x*World.SIZE + (tileX*50), y*World.SIZE + (tileY*50), 49, 49);
				}
			}
		}
    	g.setColor(Color.white);
    	g.drawRect(x*500, y*500, 500, 500);
    }
}
