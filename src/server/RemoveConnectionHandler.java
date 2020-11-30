package server;

import common.*;

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
