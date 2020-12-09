package common;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MovePlayerMsg implements Msg {

	private static final long serialVersionUID = -8483999996857446656L;
	public int id;
	public float velX;
	public float velY;
	public int x;
	public int y;
	public ArrayList<Player> players;
}
