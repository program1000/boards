package scrabble.game;

import scrabble.input.model.InputException;

public class InputCheckAction extends ControlAction {

	public InputCheckAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		try {
			control.checkInput( context.getWord() );
	    } catch (InputException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		context.setCheckSucces(false);
	    }
	}

}
