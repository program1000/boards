package scrabble.game;

import scrabble.input.model.Word;

public interface ScrabbleSession {
	public void connectToControl( GameEventProducer producer, GameEventListener listener );
	public void connectToPlayerControl( GameEventProducer producer );

	public void close();
	public Word getWord();
	
	public Turn getTurn();
	
	public enum Turn {PLAY_WORD,SWAP,QUIT};

}
