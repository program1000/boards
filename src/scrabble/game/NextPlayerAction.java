package scrabble.game;

public class NextPlayerAction extends ControlAction{

	public NextPlayerAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		control.nextPlayer();
	}

}
