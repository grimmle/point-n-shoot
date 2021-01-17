package handler;

import common.*;
import messages.MovePlayerMsg;
import server.*;

public class MovePlayerMsgHandler implements ServerHandler<MovePlayerMsg> {

	@Override
	public void handle(MovePlayerMsg msg, Connection c) {
		
		PlayerModel p = ServerGame.players.get(c.id);
		p.setVelX(msg.velX);
		p.setVelY(msg.velY);
	}

	@Override
	public String getMsgType() {
		return "MovePlayerMsg";
	}

}
