package handler;

import common.*;
import server.*;

public class MovePlayerMsgHandler implements ServerHandler<MovePlayerMsg> {

	@Override
	public void handle(MovePlayerMsg msg, Connection c) {
		
		Player p = ServerGame.players.get(c.id);
		//check if that move is possible/legit
		
//		p.setX((int) (p.getX() + msg.velX));
//		p.setY((int) (p.getY() + msg.velY));
		p.setVelX(msg.velX);
		p.setVelY(msg.velY);
//		System.out.println(c.id + " moved");
		
//		Server.connections.forEach((i, connection) -> {
//			//if(connection.id != c.id)
//			connection.sendObject(msg);
//		});
	}

	@Override
	public String getMsgType() {
		return "MovePlayerMsg";
	}

}
