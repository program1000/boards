package scrabble.game;

public class ChangeLettersAction extends ControlAction{

	public ChangeLettersAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		control.changeLetters(context.getWord(), context.getLetterMap());
	}

}
