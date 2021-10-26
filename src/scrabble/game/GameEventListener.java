package scrabble.game;

public interface GameEventListener {
	
	public void receive( GameEvent event);

	public void receiveData(GameEvent event, Object ... obj);

}
