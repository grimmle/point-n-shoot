package handler;

import client.Client;
import client.Game;
import common.*;

public class MovePlayerMsgHandler implements Handler<MovePlayerMsg> {

	@Override
	public void handle(MovePlayerMsg msg) {
		if(Game.players != null && Game.staticMap != null) {
			if(Game.players.size() == msg.players.size()) {
				for(int i = 0; i < msg.players.size(); i++) {
					Player s = msg.players.get(i);
					client.Player p = Game.players.get(i);
//					System.out.println("Player " + p.id + " moved: " + p.getX() + " " + p.getY());
					p.setX(s.getX());
					p.setY(s.getY());
					p.setBuff(s.getBuff());
					if(p.getHealth() != s.getHealth()) System.out.println(p.id + " " + s.getHealth());
					p.setHealth(s.getHealth());
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
