package scrabble.game;

import framework.GameSession;

public class Scrabble {

	public static void main(String[] args) {
		Scrabble s = new Scrabble();
		ConsoleSession session = new ConsoleSession(); 
		s.run( session, session );

	}
	
	private void run(GameSession session, ScrabbleSession scrabbleSession) {
		Config config = new Config();
		Control control = new Control();
		control.init(config, session, scrabbleSession);
		control.start();
		
	}
	

	
	

}
