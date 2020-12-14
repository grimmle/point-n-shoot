package handler;

import client.Client;
import client.Game;
import client.World;
import common.MovePlayerMsg;
import common.Player;

public class MovePlayerMsgHandler implements Handler<MovePlayerMsg> {

	@Override
	public void handle(MovePlayerMsg msg) {
		if(Game.players != null && Game.staticMap != null) {
			if(Game.players.size() == msg.players.size()) {
				for(int i = 0; i < msg.players.size(); i++) {
					Player s = msg.players.get(i);
					client.Player p = Game.players.get(i);
//					System.out.println("Player " + p.id + " moved: " + p.getX() + " " + p.getY());

					int tileX = p.getX() / World.SIZE;
			        int tileY = p.getY() / World.SIZE;
//			        System.out.println("current tile " + tileX + " " + tileY);
					p.setX(s.getX());
					p.setY(s.getY());
					
					//updated pos
					if (p.id == Client.id && (p.getX() / World.SIZE != tileX || p.getY() / World.SIZE != tileY)) {
						System.out.println("new tile " + p.getX() / World.SIZE + " " + p.getY() / World.SIZE);
						World.checkIfTilesInCache(p.getX(), p.getY());
					}
			        
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
