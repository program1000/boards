package scrabble.game;

import scrabble.board.model.Bag;
import scrabble.board.model.Board;
import scrabble.dictionary.DictonaryDb;
import scrabble.util.BoardParser;
import scrabble.util.CharToLetterMap;
import scrabble.util.DictionaryParser;
import scrabble.util.InFileReader;
import scrabble.util.LetterParser;

public class Config {

	public void init( Bag bag, Board board, CharToLetterMap map, DictonaryDb db) {
		InFileReader ir = new InFileReader();
		LetterParser lp = new LetterParser(bag, map);
		long letters=ir.read("data", "letters.txt", lp);
		System.out.println("Loaded "+ letters + " letters");
		BoardParser bp = new BoardParser(board);
		long rows=ir.read("data", "scrabbleBoard.txt", bp);
		if(board.isValid()==false) {
			throw new RuntimeException("Board init failed"); 
		}
		System.out.println("Loaded board with "+ rows + " rows");
		
		db.initDb();
		DictionaryParser dp = new DictionaryParser(db);
		long words = ir.read("data", "words.txt",dp);
		System.out.println("Loaded "+ words + " words");
	}
}
