package scrabble.input;

import scrabble.input.model.InputException;
import scrabble.input.model.Word;

public class InputProcessor {
	
	public static final String ERR_INPUT_NOT_START_OR_END_DOT="Input should not start or end with dot";
	
	public void checkInput( Word word ) throws InputException {
		if( word.getHead().isExists() || word.getTail().isExists() ) throw new InputException(ERR_INPUT_NOT_START_OR_END_DOT);
	}

}
