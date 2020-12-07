package handler;

import java.util.ArrayList;

import client.*;
import common.*;

public class GetStaticMapHandler implements Handler<GetStaticMapMsg> {

	@Override
	public void handle(GetStaticMapMsg msg) {
		//System.out.println("dis da map " + msg.content);
		Game.staticMap = (ArrayList<GameObject>) msg.content;
		
	}

	@Override
	public String getMsgType() {
		return "GetStaticMapMsg";
	}

}
