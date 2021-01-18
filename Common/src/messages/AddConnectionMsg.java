package messages;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import common.Msg;
import common.PlayerModel;

public class AddConnectionMsg implements Msg {

	private static final long serialVersionUID = -2559408686211142093L;
	public int id;
	public ConcurrentHashMap<Integer, PlayerModel> players;
	public long seed;
}
