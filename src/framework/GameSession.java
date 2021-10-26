package framework;

import java.util.ArrayList;
import java.util.List;

public abstract class GameSession {

	private List<Player> players;
	
	public GameSession(){
		players = new ArrayList<Player>();
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		player.addSession(this);
	}
	
	public void save() {
		
	}

	public abstract void init();

	public List<Player> getPlayers() {
		return players;
	}
}
