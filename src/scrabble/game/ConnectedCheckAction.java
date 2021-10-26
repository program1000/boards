package scrabble.game;

import scrabble.board.model.BoardException;
import scrabble.board.model.WordList;

public class ConnectedCheckAction extends ControlAction{

	public ConnectedCheckAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		WordList wl = context.getWordList();
		try {
			control.checkConnected(wl.getExWord(), wl.getAllWords());
		} catch (BoardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.setCheckSucces(false);
		}
	}

}
