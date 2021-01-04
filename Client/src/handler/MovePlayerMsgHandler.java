package handler;

import client.Client;
import client.Game;
import client.Player;
import common.World;
import messages.MovePlayerMsg;
import common.Agent;
import common.PlayerModel;

public class MovePlayerMsgHandler implements Handler<MovePlayerMsg> {

	@Override
	public void handle(MovePlayerMsg msg) {
		if(Game.players != null) {
			if(Game.players.size() == msg.players.size()) {
				for(int i = 0; i < msg.players.size(); i++) {
					PlayerModel s = msg.players.get(i);
					Player p = Game.players.get(i);
//					System.out.println("Player " + p.id + " moved: " + p.getX() + " " + p.getY());

					int tileX = p.getX() / World.TILE_SIZE;
			        int tileY = p.getY() / World.TILE_SIZE;
//			        System.out.println("current tile " + tileX + " " + tileY);
					p.setX(s.getX());
					p.setY(s.getY());
					
					//updated pos
					if (p.id == Client.id) {
//						Game.player = p;
						if (p.getX() / World.TILE_SIZE != tileX || p.getY() / World.TILE_SIZE != tileY) {
	//						System.out.println("new tile " + p.getX() / World.TILE_SIZE + " " + p.getY() / World.TILE_SIZE);
							World.checkIfTilesInCache(p.getX(), p.getY());
						}
					}
			        
					p.setBuff(s.getBuff());
					if(p.getHealth() != s.getHealth()) System.out.println(p.id + " " + s.getHealth());
					p.setHealth(s.getHealth());
					
					Agent a = s.getAgent();
					if(a != null)
						p.setAgent(a);
//						p.agent.setLocation(a.getLocation()); 
				}
			}
		}
		
	}

	@Override
	public String getMsgType() {
		return "MovePlayerMsg";
	}

}
