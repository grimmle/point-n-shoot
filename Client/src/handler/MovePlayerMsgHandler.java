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
					//System.out.println("Player " + p.id + " moved: " + p.getX() + " " + p.getY());

					int tileX = p.getX() / World.TILE_SIZE;
			        int tileY = p.getY() / World.TILE_SIZE;
					p.setX(s.getX());
					p.setY(s.getY());
					
					//updated pos, generate new tiles
					if (p.id == Client.id) {
						if (p.getX() / World.TILE_SIZE != tileX || p.getY() / World.TILE_SIZE != tileY) {
							World.checkIfTilesInCache(p.getX(), p.getY());
						}
					}
					p.setBuff(s.getBuff());
					if(p.getHealth() != s.getHealth()) System.out.println(p.id + " " + s.getHealth());
					p.setHealth(s.getHealth());
					
					Agent a = s.getAgent();
					if(a != null)
						p.setAgent(a);
				}
			}
		}
		
	}

	@Override
	public String getMsgType() {
		return "MovePlayerMsg";
	}

}
