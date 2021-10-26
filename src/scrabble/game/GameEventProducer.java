package scrabble.game;

import java.util.ArrayList;
import java.util.List;

public abstract class GameEventProducer {
	private List<GameEventListener> listeners;
	
	public GameEventProducer() {
		listeners = new ArrayList<GameEventListener>();
	}
	
	public void addListener( GameEventListener listener) {
		listeners.add(listener);
	}
	
	public void fire(GameEvent event) {
		for( GameEventListener listener: listeners ) {
			listener.receive(event);
		}
	}
	
	public void send(GameEvent event, Object ... obj) {
		for( GameEventListener listener: listeners ) {
			listener.receiveData(event, obj);
		}
	}

}
