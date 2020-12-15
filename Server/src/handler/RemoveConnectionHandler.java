package handler;

import messages.RemoveConnectionMsg;
import server.Connection;

public class RemoveConnectionHandler implements ServerHandler<RemoveConnectionMsg> {

	@Override
	public void handle(RemoveConnectionMsg msg, Connection c) {
		c.close();
	}

	@Override
	public String getMsgType() {
		return "RemoveConnectionMsg";
	}
	

}
