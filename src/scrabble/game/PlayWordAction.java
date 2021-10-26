package scrabble.game;

public class PlayWordAction extends ControlAction {

	public PlayWordAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		context.setWordList( control.playWord(context.getWord() ) );
	}

}
