package scrabble.game;

import scrabble.board.model.BoardException;

public class PlacementCheckAction extends ControlAction{

	public PlacementCheckAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		try {
			control.checkInputWithBoard(context.getWord());
		} catch (BoardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.setCheckSucces(false);
		}
	}
	
	

}
