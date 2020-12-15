package common;

import java.util.ArrayList;
import java.util.List;


class Node {
	int x, y;
	GameObject value;

	Node(int x, int y, GameObject value) {
		this.x = x;
		this.y = y;
		this.value = value; /* some data*/ 
	}
}

class Boundary {
	public int getxMin() {
		return xMin;
	}

	public int getyMin() {
		return yMin;
	}

	public int getxMax() {
		return xMax;
	}

	public int getyMax() {
		return yMax;
	}

	public Boundary(int xMin, int yMin, int xMax, int yMax) {
		super();
		/*
		 *  Storing two diagonal points 
		 */
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}

	public boolean inRange(int x, int y) {
		return (x >= this.getxMin() && x <= this.getxMax()
				&& y >= this.getyMin() && y <= this.getyMax());
	}

	int xMin, yMin, xMax, yMax;

}

public class QuadTree {
	final int MAX_CAPACITY = 4;
	int level = 0;
	List<Node> nodes;
	QuadTree northWest = null;
	QuadTree northEast = null;
	QuadTree southWest = null;
	QuadTree southEast = null;
	Boundary boundary;

	QuadTree(int level, Boundary boundary) {
		this.level = level;
		nodes = new ArrayList<Node>();
		this.boundary = boundary;
	}

	/* Traveling the Graph using Depth First Search*/
	public static void dfs(QuadTree tree) {
		if (tree == null)
			return;

		System.out.printf("\nLevel = %d [X1=%d Y1=%d] \t[X2=%d Y2=%d] ",
				tree.level, tree.boundary.getxMin(), tree.boundary.getyMin(),
				tree.boundary.getxMax(), tree.boundary.getyMax());

		for (Node node : tree.nodes) {
			System.out.printf(" \n\t  x=%d y=%d", node.x, node.y);
		}
		if (tree.nodes.size() == 0) {
			System.out.printf(" \n\t  Leaf Node.");
		}
		dfs(tree.northWest);
		dfs(tree.northEast);
		dfs(tree.southWest);
		dfs(tree.southEast);

	}

	void split() {
		int xOffset = this.boundary.getxMin()
				+ (this.boundary.getxMax() - this.boundary.getxMin()) / 2;
		int yOffset = this.boundary.getyMin()
				+ (this.boundary.getyMax() - this.boundary.getyMin()) / 2;

		northWest = new QuadTree(this.level + 1, new Boundary(
				this.boundary.getxMin(), this.boundary.getyMin(), xOffset,
				yOffset));
		northEast = new QuadTree(this.level + 1, new Boundary(xOffset,
				this.boundary.getyMin(), xOffset, yOffset));
		southWest = new QuadTree(this.level + 1, new Boundary(
				this.boundary.getxMin(), xOffset, xOffset,
				this.boundary.getyMax()));
		southEast = new QuadTree(this.level + 1, new Boundary(xOffset, yOffset,
				this.boundary.getxMax(), this.boundary.getyMax()));

	}

	void insert(int x, int y, GameObject value) {
		if (!this.boundary.inRange(x, y)) {
			return;
		}

		Node node = new Node(x, y, value);
		if (nodes.size() < MAX_CAPACITY) {
			nodes.add(node);
			return;
		}
		// Exceeded the capacity so split it in FOUR
		if (northWest == null) {
			split();
		}

		// Check coordinates belongs to which partition 
		if (this.northWest.boundary.inRange(x, y))
			this.northWest.insert(x, y, value);
		else if (this.northEast.boundary.inRange(x, y))
			this.northEast.insert(x, y, value);
		else if (this.southWest.boundary.inRange(x, y))
			this.southWest.insert(x, y, value);
		else if (this.southEast.boundary.inRange(x, y))
			this.southEast.insert(x, y, value);
		else
			System.out.printf("ERROR : Unhandled partition %d %d", x, y);
	}

	public static void main(String args[]) {
		QuadTree anySpace = new QuadTree(1, new Boundary(0, 0, 1000, 1000));
//		anySpace.insert(100, 100, 1);
//		anySpace.insert(500, 500, 1);
//		anySpace.insert(600, 600, 1);
//		anySpace.insert(700, 600, 1);
//		anySpace.insert(800, 600, 1);
//		anySpace.insert(900, 600, 1);
//		anySpace.insert(510, 610, 1);
//		anySpace.insert(520, 620, 1);
//		anySpace.insert(530, 630, 1);
//		anySpace.insert(540, 640, 1);
//		anySpace.insert(550, 650, 1);
//		anySpace.insert(555, 655, 1);
//		anySpace.insert(560, 660, 1);
		//Traveling the graph
		QuadTree.dfs(anySpace);
	}
}
