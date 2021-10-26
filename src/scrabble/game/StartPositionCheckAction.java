package scrabble.game;

import scrabble.board.model.BoardException;

public class StartPositionCheckAction  extends ControlAction {

	public StartPositionCheckAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		try {
			control.checkStartPositionWithBoard(context.getWord());
		} catch (BoardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.setCheckSucces(false);
		}
	}

}
