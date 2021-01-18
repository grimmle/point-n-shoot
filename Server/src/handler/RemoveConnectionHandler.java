package handler;

import messages.RemoveConnectionMsg;
import server.Connection;
import server.Server;
import server.ServerGame;

public class RemoveConnectionHandler implements ServerHandler<RemoveConnectionMsg> {

	@Override
	public void handle(RemoveConnectionMsg msg, Connection c) {
		System.out.println("Player " + c.id + " left");
		Server.connections.get(c.id).ready = false;
		Server.connections.get(c.id).close();
		ServerGame.playersUpdated = true;
	}

	@Override
	public String getMsgType() {
		return "RemoveConnectionMsg";
	}
	

}
