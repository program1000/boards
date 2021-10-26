package framework;

public class Player {

	private Score score;
	private String name;
	private GameSession session;
	
	public Player(String playerName) {
		score = new Score();
		name=playerName;
	}
	
	public void addSession( GameSession newSession ) {
		session = newSession;
	}

	public String getName() {
		return name;
	}

	public void addPoints(int points) {
		score.add(points);
	}

	public int getScore() {
		return score.getValue();
	}
}
