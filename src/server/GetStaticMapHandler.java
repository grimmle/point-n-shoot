package server;

import common.*;

public class GetStaticMapHandler implements ServerHandler<GetStaticMapMsg> {

	@Override
	public void handle(GetStaticMapMsg msg, Connection c) {
		msg.id = c.id;
//		msg.content = Server.staticMap.get(0);
		msg.content = Server.staticMap;
		c.sendObject(msg);
		
	}

	@Override
	public String getMsgType() {
		return "GetStaticMapMsg";
	}

}
