package client;

import common.*;

public class MovePlayerMsgHandler implements Handler<MovePlayerMsg> {

	@Override
	public void handle(MovePlayerMsg msg) {
		// send move command
		
	}

	@Override
	public String getMsgType() {
		return "MovePlayerMsg";
	}

}
