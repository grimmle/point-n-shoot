package messages;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import common.Msg;
import common.PlayerModel;

public class MovePlayerMsg implements Msg {

	private static final long serialVersionUID = -8483999996857446656L;
	public int id;
	public float velX;
	public float velY;
	public int x;
	public int y;
	public ConcurrentHashMap<Integer, PlayerModel> players;
}
