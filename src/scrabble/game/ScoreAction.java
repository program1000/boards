package scrabble.game;

import scrabble.board.model.WordList;

public class ScoreAction extends ControlAction{

	public ScoreAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		WordList wl = context.getWordList();
		control.score(context.getWord(), wl.getExWord(), wl.getAllWords());
	}

}
