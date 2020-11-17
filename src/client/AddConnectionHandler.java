package client;

import common.*;

// receiving messages
public class AddConnectionHandler implements Handler<AddConnectionMsg> {

	@Override
	public void handle(AddConnectionMsg msg) {
		System.out.println("Player " + msg.id + " joined the Game!");
	}

	@Override
	public String getMsgType() {
//		Class c = this.getClass();
//		String s = c.getSimpleName();
		return "AddConnectionMsg";
	}

}
