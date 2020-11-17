package server;

import common.AddConnectionMsg;
import common.Handler;
import common.Msg;
import server.*;

public class AddConnectionHandler implements Handler {

	@Override
	public void handle(Msg msg) {
		AddConnectionMsg m = (AddConnectionMsg)msg;
		
		System.out.println("Player " + m.id + " joined the Game!");
		Server.connections.forEach((i, c) -> {
			c.sendObject(new AddConnectionMsg());
		});
		
	}

	@Override
	public String getMsgType() {
//		Class c = this.getClass();
//		String s = c.getSimpleName();
		return "AddConnectionMsg";
	}

}
