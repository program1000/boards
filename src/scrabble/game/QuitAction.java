package scrabble.game;

public class QuitAction extends ControlAction{

	public QuitAction(ControlActionInterface newControl) {
		super(newControl);
	}

	@Override
	public void doAction(ActionContext context) {
		control.setQuitFlag();
	}

}
