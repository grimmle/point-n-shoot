package handler;

import java.awt.Color;
import java.util.Random;

import common.*;
import messages.AddConnectionMsg;
import server.*;

public class AddConnectionHandler implements ServerHandler<AddConnectionMsg> {

	public static Color[] colorSet = new Color[]{ 
			new Color(224, 52, 88),
			new Color(114, 9, 183),
			new Color(6, 214, 160),
			new Color(17, 138, 178),
			new Color(7, 59, 76),
			new Color(255, 209, 102),
	};
	
	@Override
	public void handle(AddConnectionMsg msg, Connection c) {
		Color color;
		if(c.id > colorSet.length) {
			Random rand = new Random();
			
			float[] hsb =  new float[] { 0f, 1f, 0.5f };
			hsb = Color.RGBtoHSB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), hsb);
			
			int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
			int red = (rgb>>16)&0xFF;
			int green = (rgb>>8)&0xFF;
			int blue = rgb&0xFF;
			
			color = new Color(red, green, blue);
		} else {			
			color = colorSet[c.id];
		}
		
		PlayerModel p = new PlayerModel(100050, 100050, c.id, color);
		ServerGame.players.put(c.id, p);
		
		World.checkIfTilesInCache(p.getX(), p.getY());
		
		System.out.println("Player " + c.id + " joined the Game!");
//		System.out.println("players on server " + ServerGame.players);
		
		Server.connections.forEach((i, connection) -> {
			AddConnectionMsg m = new AddConnectionMsg();
			if(connection.id == c.id) m.seed = ServerGame.SEED;
			m.id = c.id;
			m.players = ServerGame.players;
			connection.sendObject(m);
		});
	}

	@Override
	public String getMsgType() {
		return "AddConnectionMsg";
	}
}
