package handler;

import java.util.Map.Entry;

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
			if(Game.players.size() != msg.players.size()) {
				Game.players.clear();
				for (Entry<Integer, PlayerModel> entry : msg.players.entrySet()) {
					PlayerModel player = entry.getValue();
					Game.players.put(player.id, new Player(player.getX(), player.getY(), player.id, player.getColor()));
				}
			}
			if(Game.players.size() == msg.players.size()) {
				for (Entry<Integer, PlayerModel> entry : msg.players.entrySet()) {
					PlayerModel s = entry.getValue();
					Player p = Game.players.get(s.id);

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
					p.setHealth(s.getHealth());
					p.setAgent(s.getAgent());
				}
			}
		}
		
	}

	@Override
	public String getMsgType() {
		return "MovePlayerMsg";
	}

}
