package handler;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import client.Game;
import common.GameObject;
import common.GetStaticMapMsg;

public class GetStaticMapHandler implements Handler<GetStaticMapMsg> {

	@Override
	public void handle(GetStaticMapMsg msg) {
		Game.staticMap = (ArrayList<GameObject>) msg.staticMap;
		Game.dynamicObjects = (CopyOnWriteArrayList<GameObject>) msg.dynamic;	
	}

	@Override
	public String getMsgType() {
		return "GetStaticMapMsg";
	}

}
