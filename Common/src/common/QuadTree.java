package common;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class QuadTree {
	final int CAPACITY = 4;
	
	Rectangle rect;
	List<Block> blocks = new ArrayList<Block>();
	boolean divided = false;
	
	QuadTree northwest = null;
	QuadTree northeast = null;
	QuadTree southwest = null;
	QuadTree southeast = null;
	
	QuadTree(Rectangle rect) {
		this.rect = rect;
	}
	
	
	public void subdivide() {
		Rectangle nw = new Rectangle(rect.x, rect.y, rect.width/2, rect.height/2);
		Rectangle ne = new Rectangle(rect.x + rect.width/2, rect.y, rect.width/2, rect.height/2);
		Rectangle sw = new Rectangle(rect.x, rect.y + rect.height/2, rect.width/2, rect.height/2);
		Rectangle se = new Rectangle(rect.x + rect.height/2, rect.y + rect.width/2, rect.width/2, rect.height/2);
		
		northwest = new QuadTree(nw);
		northeast = new QuadTree(ne);
		southwest = new QuadTree(sw);
		southeast = new QuadTree(se);
		divided = true;
	}
	
	
	public boolean insert(Block p) {
		if(!rect.contains(p.x, p.y)) {
			return false;
		}
		
		if(blocks.size() < CAPACITY) {
			blocks.add(p);
			return true;
		} else {
			if(!divided) {
				subdivide();
			}
			if(northwest.insert(p)) return true;
			if(northeast.insert(p)) return true;
			if(southwest.insert(p)) return true;
			if(southeast.insert(p)) return true;
		}
		return false;
	}
	
	
	public List<Block> query(Rectangle range) {
		List<Block> found = new ArrayList<Block>();
		
		if(!rect.intersects(range)) {
			return found;
		} else {
			for(Block p : blocks) {
				if(range.contains(p.x, p.y)) {
					found.add(p);
				}
			}
		}
		if(divided) {
			found.addAll(northwest.query(range));
			found.addAll(northeast.query(range));
			found.addAll(southwest.query(range));
			found.addAll(southeast.query(range));
		}
		return found;
	}
}