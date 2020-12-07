package handler;

import client.Game;
import common.*;

public class MovePlayerMsgHandler implements Handler<MovePlayerMsg> {

	@Override
	public void handle(MovePlayerMsg msg) {
		System.out.println("Players on clientside " + Game.players.size());
		System.out.println("Players in msg " + msg.players.size());
		if(Game.players != null && Game.staticMap != null) {
			if(Game.players.size() == msg.players.size()) {
				for(int i = 0; i < msg.players.size(); i++) {
					Player s = msg.players.get(i);
					client.Player p = Game.players.get(i);
					System.out.println("Player " + p.id + " moved: " + p.getX() + " " + p.getY());
					p.setX(s.getX());
					p.setY(s.getY());
				}
			}
		}
//		client.Player p = Game.players.get(msg.id);
//		System.out.println(p.id);
//		System.out.println(msg.x);
//		System.out.println(msg.y);
//		p.setX(msg.x);
//		p.setY(msg.y);
		
	}

	@Override
	public String getMsgType() {
		return "MovePlayerMsg";
	}

}
