package boards.scrabble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import scrabble.board.Piece;
import scrabble.board.model.Bag;
import scrabble.board.model.Board;
import scrabble.board.model.Letter;
import scrabble.dictionary.DictonaryDb;
import scrabble.util.BoardParser;
import scrabble.util.CharToLetterMap;
import scrabble.util.DictionaryParser;
import scrabble.util.LetterParser;

public class ParserTest {
	
	@Test
	public void letterParserTest() {
		Stream<String> stream = Arrays.asList("Header","A 1 1","B 2 3", "IJ 0 2").stream();
		Bag bag=new Bag();
		CharToLetterMap map = new CharToLetterMap();
		LetterParser lp= new LetterParser(bag,map);
		assertEquals(3, lp.parse(stream));
		assertTrue(map.hasLetter("A"));
		assertEquals("A", map.get("A").getChar());
		assertTrue(map.hasLetter("B"));
		assertEquals("B", map.get("B").getChar());
		assertTrue(map.hasLetter("IJ"));
		assertEquals("IJ", map.get("IJ").getChar());
		
		assertEquals(6,bag.size());
		List<Letter> l1 = bag.get(1);
		assertEquals(1,l1.size());
		assertEquals("A",l1.get(0).getChar());
		assertEquals(1,l1.get(0).getValue());
		List<Letter> l2 = bag.get(3);
		assertEquals(3,l2.size());
		for(Letter letter:l2) {
		    assertEquals("B",letter.getChar());
		    assertEquals(2,letter.getValue());
		}
		List<Letter> l3 = bag.get(2);
		assertEquals(2,l3.size());
		for(Letter letter:l3) {
		    assertEquals("IJ",letter.getChar());
		    assertEquals(0,letter.getValue());
		}
	}
	
	@Test
	public void boardParserTest() {
		Stream<String> stream = Arrays.asList("DW. DW",". TL. ","DLTWDL").stream();
		Board board = new Board();
		BoardParser bp = new BoardParser(board);
		assertEquals(9, bp.parse(stream));
		int x=0;
		int y=0;
		for(int i=0;i<9;i++) {
			board.getTile(x, y).addPiece(new Piece(new Letter("",2)));
			x++;
			if(x==3) {
				x=0;
				y++;
			}
		}
		assertEquals(2, board.getTile(0, 0).multiplier());
		assertEquals(2, board.getTile(1, 0).score(true));
		assertEquals(2, board.getTile(2, 0).multiplier());
		assertEquals(2, board.getTile(0, 1).score(true));
		assertEquals(6, board.getTile(1, 1).score(true));
		assertEquals(2, board.getTile(2, 1).score(true));
		assertEquals(4, board.getTile(0, 2).score(true));
		assertEquals(3, board.getTile(1, 2).multiplier());
		assertEquals(4, board.getTile(2, 2).score(true));
	}

	@Test
	public void dictionaryParserTest() {
		Stream<String> stream = Arrays.asList("","23","abcde", "abcij", "telangwoord","f-k","oa.","'savonds","met spa").stream();
		DictonaryDb db = new DictonaryDb();
		DictionaryParser dp = new DictionaryParser(db);
		db.initDb(3, 5);
		assertEquals(2, dp.parse(stream));
		db.close();
		
	}
}
