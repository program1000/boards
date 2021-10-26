package scrabble.game;

public class ChangeToNormalTurnAction extends ControlAction {

	public ChangeToNormalTurnAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		control.changeToNormalTurn();
	}

}
