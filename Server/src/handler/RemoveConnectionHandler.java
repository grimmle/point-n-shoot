package handler;

import messages.RemoveConnectionMsg;
import server.Connection;
import server.Server;

public class RemoveConnectionHandler implements ServerHandler<RemoveConnectionMsg> {

	@Override
	public void handle(RemoveConnectionMsg msg, Connection c) {
		Server.connections.get(c.id).close();
	}

	@Override
	public String getMsgType() {
		return "RemoveConnectionMsg";
	}
	

}
