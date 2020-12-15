package messages;

import java.util.ArrayList;

import common.Msg;
import common.PlayerModel;

public class AddConnectionMsg implements Msg {

	private static final long serialVersionUID = -2559408686211142093L;
	public int id;
	public ArrayList<PlayerModel> players;
	public long seed;
}
