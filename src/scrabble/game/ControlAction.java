package scrabble.game;

public abstract class ControlAction implements ActionInterface {
	
	protected ControlActionInterface control;
	
	public ControlAction(ControlActionInterface newControl) {
		control = newControl;
	}

}
