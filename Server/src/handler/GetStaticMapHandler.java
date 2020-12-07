package handler;

import common.*;
import server.*;

public class GetStaticMapHandler implements ServerHandler<GetStaticMapMsg> {

	@Override
	public void handle(GetStaticMapMsg msg, Connection c) {
		msg.id = c.id;
		msg.content = ServerGame.staticMap;
		
		c.ready = true;
		c.sendObject(msg);
		
	}

	@Override
	public String getMsgType() {
		return "GetStaticMapMsg";
	}
}
