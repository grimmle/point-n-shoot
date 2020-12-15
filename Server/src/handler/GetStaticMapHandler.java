package handler;

import common.*;
import messages.GetStaticMapMsg;
import server.*;

public class GetStaticMapHandler implements ServerHandler<GetStaticMapMsg> {

	@Override
	public void handle(GetStaticMapMsg msg, Connection c) {
		msg.id = c.id;
		msg.staticMap = ServerGame.staticMap;
		msg.dynamic = ServerGame.dynamicObjects;
		
		c.ready = true;
		c.sendObject(msg);
		
	}

	@Override
	public String getMsgType() {
		return "GetStaticMapMsg";
	}
}
