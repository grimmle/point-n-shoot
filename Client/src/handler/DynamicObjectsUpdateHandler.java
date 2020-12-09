package handler;

import java.util.concurrent.CopyOnWriteArrayList;

import client.Game;
import common.DynamicObjectsUpdateMsg;
import common.GameObject;

public class DynamicObjectsUpdateHandler implements Handler<DynamicObjectsUpdateMsg>{

	@Override
	public void handle(DynamicObjectsUpdateMsg msg) {
		Game.dynamicObjects = (CopyOnWriteArrayList<GameObject>) msg.content;
	}

	@Override
	public String getMsgType() {
		return "DynamicObjectsUpdateMsg";
	}
}
