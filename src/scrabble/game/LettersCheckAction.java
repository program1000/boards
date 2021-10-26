package scrabble.game;

import scrabble.player.PlayerException;

public class LettersCheckAction extends ControlAction {

	public LettersCheckAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		try {
			context.setLetterMap(control.checkLetterWithHand(context.getWord()));
		} catch (PlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.setCheckSucces(false);
		}
		
	}

}
