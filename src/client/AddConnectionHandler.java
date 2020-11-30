package client;

import common.*;

// receiving messages
public class AddConnectionHandler implements Handler<AddConnectionMsg> {

	@Override
	public void handle(AddConnectionMsg msg) {
		if(Client.id == -1) {
			Client.id = msg.id;
			System.out.println("I got assigned the ID " + msg.id);
		} else
			System.out.println("Player " + msg.id + " joined the Game!");
	}

	@Override
	public String getMsgType() {
//		Class c = this.getClass();
//		String s = c.getSimpleName();
		return "AddConnectionMsg";
	}

}
