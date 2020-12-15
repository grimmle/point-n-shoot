package handler;

import java.awt.Color;
import java.util.Random;

import common.*;
import messages.AddConnectionMsg;
import server.*;

public class AddConnectionHandler implements ServerHandler<AddConnectionMsg> {

	@Override
	public void handle(AddConnectionMsg msg, Connection c) {
		Random rand = new Random();
		Color randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		PlayerModel p = new PlayerModel(100000, 100000, c.id, randomColor);
		ServerGame.players.add(p);
		
		World.checkIfTilesInCache(p.getX(), p.getY());
		
		System.out.println("Player " + c.id + " joined the Game!");
		System.out.println("players on server " + ServerGame.players);
		
		Server.connections.forEach((i, connection) -> {
			AddConnectionMsg m = new AddConnectionMsg();
			if(connection.id == c.id) m.seed = ServerGame.SEED;
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
