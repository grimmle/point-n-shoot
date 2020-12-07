package handler;

import client.Client;
import client.Game;
import common.*;

// receiving messages
public class AddConnectionHandler implements Handler<AddConnectionMsg> {

	@Override
	public void handle(AddConnectionMsg msg) {
		System.out.println("Players in MSG: " + msg.players.size());
		
		if(Client.id == -1) {
			Client.id = msg.id;
			System.out.println("I got assigned the ID " + msg.id);
			new Game(new client.Player(msg.players.get(0).getX(), msg.players.get(0).getY(), msg.players.get(0).id));
		} else {
			System.out.println("Player " + msg.id + " joined the Game!");
		}
		if(msg.players.size() != Game.players.size()) {
			System.out.println("ungleich players");
			Game.players.clear();
			for(Player p : msg.players) {
				Game.players.add(new client.Player(p.getX(), p.getY(), p.id));
			}
		}
		System.out.println(Game.players.size() + " Player(s) online.");
	}

	@Override
	public String getMsgType() {
		return "AddConnectionMsg";
	}

}
