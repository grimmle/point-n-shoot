package handler;

import java.util.Map.Entry;

import client.*;
import common.PlayerModel;
import messages.AddConnectionMsg;


public class AddConnectionHandler implements Handler<AddConnectionMsg> {

	@Override
	public void handle(AddConnectionMsg msg) {
		
		if(msg.players.size() != Game.players.size()) {
			Game.players.clear();
			for (Entry<Integer, PlayerModel> entry : msg.players.entrySet()) {
				PlayerModel player = entry.getValue();
				Game.players.put(player.id, new Player(player.getX(), player.getY(), player.id, player.getColor()));
			}
		}
		
		if(Client.id == -1) {
			Client.id = msg.id;
//			System.out.println("I got assigned the ID " + msg.id);
			new Game(msg.seed);
		} else {
			System.out.println("Player " + msg.id + " joined the Game!");
		}
		
		System.out.println(Game.players.size() + " Player(s) online.");
	}

	@Override
	public String getMsgType() {
		return "AddConnectionMsg";
	}

}
