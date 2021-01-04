package handler;

import java.util.concurrent.CopyOnWriteArrayList;

import client.Game;
import common.GameObject;
import messages.GetStaticMapMsg;

public class GetStaticMapHandler implements Handler<GetStaticMapMsg> {

	@Override
	public void handle(GetStaticMapMsg msg) {
		Game.dynamicObjects = (CopyOnWriteArrayList<GameObject>) msg.dynamic;
	}

	@Override
	public String getMsgType() {
		return "GetStaticMapMsg";
	}

}
