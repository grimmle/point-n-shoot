package server;

import common.*;

public class AddConnectionHandler implements ServerHandler<AddConnectionMsg> {

	@Override
	public void handle(AddConnectionMsg msg, Connection c) {
		msg.id = c.id;
		
		System.out.println("Player " + c.id + " joined the Game!");
		Server.connections.forEach((i, connection) -> {
			//if(connection.id != c.id) 
			connection.sendObject(msg);
		});
		
	}

	@Override
	public String getMsgType() {
		return "AddConnectionMsg";
	}


}
