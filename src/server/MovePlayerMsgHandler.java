package server;

import common.*;

public class MovePlayerMsgHandler implements ServerHandler<MovePlayerMsg> {

	@Override
	public void handle(MovePlayerMsg msg, Connection c) {
		// send move command
		
	}

	@Override
	public String getMsgType() {
		return "MovePlayerMsg";
	}

}
