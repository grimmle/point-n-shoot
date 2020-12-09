package handler;

import common.DynamicObjectsUpdateMsg;
import common.GameObject;
import server.Connection;
import server.ServerGame;

public class DynamicObjectsUpdateHandler implements ServerHandler<DynamicObjectsUpdateMsg>{

	@Override
	public void handle(DynamicObjectsUpdateMsg msg, Connection c) {
		ServerGame.dynamicObjects.add((GameObject) msg.content);
	}

	@Override
	public String getMsgType() {
		return "DynamicObjectsUpdateMsg";
	}

}
