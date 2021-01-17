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
		
		float[] hsb =  new float[] { 0f, 1f, 0.5f };
		hsb = Color.RGBtoHSB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), hsb);
//		hsb[1] = 1f;
//		hsb[2] = 1f;
		int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
		int red = (rgb>>16)&0xFF;
		int green = (rgb>>8)&0xFF;
		int blue = rgb&0xFF;
		Color color = new Color(red, green, blue);
		PlayerModel p = new PlayerModel(100050, 100050, c.id, color);
		ServerGame.players.add(p);
		
		World.checkIfTilesInCache(p.getX(), p.getY());
		
		System.out.println("Player " + c.id + " joined the Game!");
//		System.out.println("players on server " + ServerGame.players);
		
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
