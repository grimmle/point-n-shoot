package handler;

import common.*;
import server.*;

public class AddConnectionHandler implements ServerHandler<AddConnectionMsg> {

	@Override
	public void handle(AddConnectionMsg msg, Connection c) {
		
		Player p = new Player(100000, 100000, c.id);
		ServerGame.players.add(p);
		
		System.out.println("Player " + c.id + " joined the Game!");
		System.out.println("players on server " + ServerGame.players);
		
		Server.connections.forEach((i, connection) -> {
//			if(connection.id != c.id)
			AddConnectionMsg m = new AddConnectionMsg();
			m.id = c.id;
			m.players = ServerGame.players;
			System.out.println(m.players.size() + " send to " + connection.id);
			connection.sendObject(m);
		});
		
	}

	@Override
	public String getMsgType() {
		return "AddConnectionMsg";
	}
}
