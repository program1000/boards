package boards.scrabble;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import scrabble.board.BoardProcessor;
import scrabble.board.Piece;
import scrabble.board.model.Board;
import scrabble.board.model.BoardException;
import scrabble.board.model.BoardWord;
import scrabble.board.model.ExtendedWord;
import scrabble.board.model.Letter;
import scrabble.board.model.Tile;
import scrabble.board.model.Tile.Value;
import scrabble.input.model.InputLetter;
import scrabble.input.model.Word;
import scrabble.util.CharToLetterMap;

public class BoardProcessorTest {
	
	@Test
	public void checkInputTest() {
		BoardProcessor proc = new BoardProcessor();
		CharToLetterMap map = new CharToLetterMap();
		proc.setLetterMap(map);
		Board board = new Board();
		board.init(3, 3);
		proc.setBoard(board);
		Word word = new Word(true);
		BoardException e = assertThrows(BoardException.class, () -> {
		    proc.checkInput(word);
		  });
		assertEquals(BoardProcessor.ERR_EMPTY_WORD, e.getMessage());
		
		word.addLetter(new InputLetter(0, 0, "3"));
		e = assertThrows(BoardException.class, () -> {
		    proc.checkInput(word);
		  });
		assertEquals(BoardProcessor.ERR_LETTER + "3" + BoardProcessor.ERR_LETTER_NOT_VALID, e.getMessage());
		
		map.add(new Letter("a", 0));
		map.add(new Letter("b", 0));
		Word word2 = new Word(true);
		word2.addLetter(new InputLetter(-1, 0, "a"));
		e = assertThrows(BoardException.class, () -> {
		    proc.checkInput(word2);
		  });
		assertEquals(BoardProcessor.ERR_LETTER + "a" + BoardProcessor.ERR_LETTER_LOCATION_OUT_OF_BOUNDS, e.getMessage());
		
		Word word3 = new Word(true);
		word3.addLetter(new InputLetter(0, 3, "a"));
		e = assertThrows(BoardException.class, () -> {
		    proc.checkInput(word3);
		  });
		assertEquals(BoardProcessor.ERR_LETTER + "a" + BoardProcessor.ERR_LETTER_LOCATION_OUT_OF_BOUNDS, e.getMessage());
		
		Tile tile = new Tile(Value.NONE);
		tile.addPiece(new Piece(new Letter("a",1)));
		board.setTile(0, 0, tile);
		Word word4 = new Word(true);
		word4.addLetter(new InputLetter(0, 0, "a"));
		e = assertThrows(BoardException.class, () -> {
		    proc.checkInput(word4);
		  });
		assertEquals(BoardProcessor.ERR_LETTER + "a" + BoardProcessor.ERR_LETTER_ON_OCCUPIED_TILE, e.getMessage());
		
		board.setTile(0, 1, new Tile(Value.NONE));
		board.setTile(1, 2, new Tile(Value.NONE));
		Word word5 = new Word(true);
		word5.addLetter(new InputLetter(0, 1, "a"));
		word5.addLetter(new InputLetter(1, 2, "b"));
		e = assertThrows(BoardException.class, () -> {
		    proc.checkInput(word5);
		  });
		assertEquals(BoardProcessor.ERR_LETTER + "b" + BoardProcessor.ERR_LETTER_DIFFERENT_ROW, e.getMessage());
		
		Word word6 = new Word(false);
		word6.addLetter(new InputLetter(0, 1, "a"));
		word6.addLetter(new InputLetter(1, 2, "b"));
		e = assertThrows(BoardException.class, () -> {
		    proc.checkInput(word6);
		  });
		assertEquals(BoardProcessor.ERR_LETTER + "b" + BoardProcessor.ERR_LETTER_DIFFERENT_COLUMN, e.getMessage());
		
		board.setTile(1, 1, new Tile(Value.NONE));
		Word word7 = new Word(true);
		word7.addLetter(new InputLetter(0, 1, "a"));
		InputLetter il = new InputLetter(1, 1, "a");
		il.setExists();
        word7.addLetter(il);
        e = assertThrows(BoardException.class, () -> {
            proc.checkInput(word7);
          });
        assertEquals(BoardProcessor.ERR_LETTER + "a" + BoardProcessor.ERR_LETTER_ON_EMPTY_TILE, e.getMessage());
	}
	
	@Test
	public void checkStartPositionTest() {
		BoardProcessor proc = new BoardProcessor();
		Board board = new Board();
		board.init(3, 3);
		proc.setBoard(board);
		
		board.setStartTile(1, 2);
		Word word = new Word(true);
		word.addLetter(new InputLetter(0, 2, "a"));
		word.addLetter(new InputLetter(1, 2, "b"));
		
		assertDoesNotThrow(()-> {
			proc.checkStartPosition(word);
		});
		
		Word word2 = new Word(true);
		word2.addLetter(new InputLetter(0, 2, "a"));
		BoardException e = assertThrows(BoardException.class, () -> {
		    proc.checkStartPosition(word2);
		  });
		assertEquals(BoardProcessor.ERR_WORD_NOT_ON_START_POSITION, e.getMessage());
	}
	
	@Test
	public void checkConnectedTest() {
		BoardProcessor proc = new BoardProcessor();
		ExtendedWord exWord = new ExtendedWord(1, 0, "", false);
		List<BoardWord> allWords = new ArrayList<BoardWord>();
		assertDoesNotThrow(()-> {
			proc.checkConnected(exWord, allWords);
		});
		
		ExtendedWord exWord2 = new ExtendedWord(0, 1, "", false);
		assertDoesNotThrow(()-> {
			proc.checkConnected(exWord2, allWords);
		});
		
		ExtendedWord exWord3 = new ExtendedWord(0, 0, "", false);
		allWords.add(new BoardWord(0, 0, 0, 0, 1, ""));
		assertDoesNotThrow(()-> {
			proc.checkConnected(exWord3, allWords);
		});
		
		ExtendedWord exWord4 = new ExtendedWord(0, 0, "", false);
		List<BoardWord> allWords2 = new ArrayList<BoardWord>();
		BoardException e = assertThrows(BoardException.class, () -> {
		    proc.checkConnected(exWord4, allWords2);
		  });
		assertEquals(BoardProcessor.ERR_WORD_NOT_CONNECTED_ON_BOARD, e.getMessage());
	}

	@Test
	public void createWordTest() {
		BoardProcessor proc = new BoardProcessor();
		CharToLetterMap map = new CharToLetterMap();
		map.add(new Letter("a", 1));
		map.add(new Letter("b", 1));
		map.add(new Letter("c", 1));
		proc.setLetterMap(map);
		Board board = new Board();
		board.init(3, 3);
		for(int i=0;i<3;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		proc.setBoard(board);
		
		Word word = new Word(true);
		word.addLetter(new InputLetter(1,1,"a"));
		word.addLetter(new InputLetter(2,1,"b"));
		word.addLetter(new InputLetter(3,1,"c"));
		ExtendedWord result = proc.createWord(word);
		assertEquals("abc", result.getWord() );
		assertEquals(0, result.getPreLen() );
		assertEquals(0, result.getPostLen() );
		
		board.getTile(0, 0).addPiece(new Piece(map.get("a")));
		word = new Word(true);
		word.addLetter(new InputLetter(1,0,"b"));
		result = proc.createWord(word);
		assertEquals("ab", result.getWord() );
		assertEquals(1, result.getPreLen() );
		assertEquals(0, result.getPostLen() );
		
		word = new Word(false);
		word.addLetter(new InputLetter(0,1,"c"));
		result = proc.createWord(word);
		assertEquals("ac", result.getWord() );
		assertEquals(1, result.getPreLen() );
		assertEquals(0, result.getPostLen() );
		
		board.getTile(2, 2).addPiece(new Piece(map.get("c")));
		word = new Word(true);
		word.addLetter(new InputLetter(1,2,"b"));
		result = proc.createWord(word);
		assertEquals("bc", result.getWord() );
		assertEquals(0, result.getPreLen() );
		assertEquals(1, result.getPostLen() );
		
		word = new Word(false);
		word.addLetter(new InputLetter(2,1,"a"));
		result = proc.createWord(word);
		assertEquals("ac", result.getWord() );
		assertEquals(0, result.getPreLen() );
		assertEquals(1, result.getPostLen() );
		
		board.getTile(0, 2).addPiece(new Piece(map.get("a")));
		word = new Word(true);
		word.addLetter(new InputLetter(1,2,"b"));
		result = proc.createWord(word);
		assertEquals("abc", result.getWord() );
		assertEquals(1, result.getPreLen() );
		assertEquals(1, result.getPostLen() );
		
		word = new Word(false);
		word.addLetter(new InputLetter(0,1,"b"));
		result = proc.createWord(word);
		assertEquals("aba", result.getWord() );
		assertEquals(1, result.getPreLen() );
		assertEquals(1, result.getPostLen() );
	}
	
	@Test
	public void playWordTest() {
		// . b .
		// a[e]c
		// . d .
		BoardProcessor proc = new BoardProcessor();
		CharToLetterMap map = new CharToLetterMap();
		map.add(new Letter("a", 0));
		map.add(new Letter("b", 0));
		map.add(new Letter("c", 0));
		map.add(new Letter("d", 0));
		map.add(new Letter("e", 0));
		proc.setLetterMap(map);
		Board board = new Board();
		board.init(3, 3);
		for(int i=0;i<3;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		board.getTile(0, 1).addPiece(new Piece(map.get("a")));
		board.getTile(1, 0).addPiece(new Piece(map.get("b")));
		board.getTile(2, 1).addPiece(new Piece(map.get("c")));
		board.getTile(1, 2).addPiece(new Piece(map.get("d")));
		proc.setBoard(board);
		Word word = new Word(true);
		word.addLetter(new InputLetter(1, 1, "e"));
		List<BoardWord> allWords = proc.playWord(word);
		assertEquals(allWords.size(), 1);
		assertEquals(allWords.get(0).getValue(), "bed");
	
		assertEquals("e", board.getTile(0, 1).getPiece().getRight().getChar());
		assertEquals("e", board.getTile(1, 0).getPiece().getDown().getChar());
		assertEquals("e", board.getTile(2, 1).getPiece().getLeft().getChar());
		assertEquals("e", board.getTile(1, 2).getPiece().getUp().getChar());
		
		//  . b.
		// [a]e[c]
		//  . d .
		Board board2 = new Board();
		board2.init(3, 3);
		for(int i=0;i<3;i++) {
			board2.setTile(i, 0, new Tile(Value.NONE));
			board2.setTile(i, 1, new Tile(Value.NONE));
			board2.setTile(i, 2, new Tile(Value.NONE));
		}
		proc.setBoard(board2);
		Word word2 = new Word(false);
		word2.addLetter(new InputLetter(1, 0, "b"));
		word2.addLetter(new InputLetter(1, 1, "e"));
		word2.addLetter(new InputLetter(1, 2, "d"));
		List<BoardWord> allWords2 = proc.playWord(word2);
		assertTrue(allWords2.isEmpty());
		
		Word word3 = new Word(true);
		word3.addLetter(new InputLetter(0, 1, "a"));
		InputLetter dot = new InputLetter(1, 1, ".");
		dot.setExists();
		word3.addLetter(dot);
		word3.addLetter(new InputLetter(2, 1, "c"));
		List<BoardWord> allWords3 = proc.playWord(word3);
		assertTrue(allWords3.isEmpty());

		assertEquals("a", board.getTile(1, 1).getPiece().getLeft().getChar());
		assertEquals("c", board.getTile(1, 1).getPiece().getRight().getChar());
	}
	
	@Test
	public void collectWordTest() {
		// e . g .
		//[a b c d]
		// . f h .
		BoardProcessor proc = new BoardProcessor();
		CharToLetterMap map = new CharToLetterMap();
		map.add(new Letter("a", 0));
		map.add(new Letter("b", 0));
		map.add(new Letter("c", 0));
		map.add(new Letter("d", 0));
		map.add(new Letter("e", 0));
		map.add(new Letter("f", 0));
		map.add(new Letter("g", 0));
		map.add(new Letter("h", 0));
		proc.setLetterMap(map);
		Board board = new Board();
		board.init(3, 4);
		for(int i=0;i<4;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		
		board.getTile(0, 0).addPiece(new Piece(map.get("e")));
		board.getTile(2, 0).addPiece(new Piece(map.get("g")));
		board.getTile(2, 2).addPiece(new Piece(map.get("h")));
		proc.setBoard(board);
		
		Word word = new Word(true);
		word.addLetter(new InputLetter(0, 1, "a"));
		word.addLetter(new InputLetter(1, 1, "b"));
		word.addLetter(new InputLetter(2, 1, "c"));
		word.addLetter(new InputLetter(3, 1, "d"));
		List<BoardWord> result = proc.playWord(word);
		assertEquals(2, result.size() );
		assertEquals("ea", result.get(0).getValue() );
		assertEquals(0, result.get(0).getHeadX() );
		assertEquals(0, result.get(0).getHeadY() );
		assertEquals(0, result.get(0).getMainX() );
		assertEquals(1, result.get(0).getMainY() );
		assertEquals(2, result.get(0).getLen() );
		assertEquals("gch", result.get(1).getValue() );
		assertEquals(2, result.get(1).getHeadX() );
		assertEquals(0, result.get(1).getHeadY() );
		assertEquals(2, result.get(1).getMainX() );
		assertEquals(1, result.get(1).getMainY() );
		assertEquals(3, result.get(1).getLen() );
		
		word = new Word(true);
		word.addLetter(new InputLetter(1, 2, "f"));
		result = proc.playWord(word);

		assertEquals(1, result.size() );
		assertEquals("bf", result.get(0).getValue() );
		assertEquals(1, result.get(0).getHeadX() );
		assertEquals(1, result.get(0).getHeadY() );
		assertEquals(1, result.get(0).getMainX() );
		assertEquals(2, result.get(0).getMainY() );	
		assertEquals(2, result.get(0).getLen() );
		
		board = new Board();
		board.init(3, 4);
		for(int i=0;i<4;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		
		board.getTile(0, 0).addPiece(new Piece(map.get("e")));
		board.getTile(1, 2).addPiece(new Piece(map.get("f")));
		proc.setBoard(board);
		
		word = new Word(true);
		word.addLetter(new InputLetter(0, 1, "a"));
		word.addLetter(new InputLetter(1, 1, "b"));
		word.addLetter(new InputLetter(2, 1, "c"));
		word.addLetter(new InputLetter(3, 1, "d"));
		result = proc.playWord(word);
		assertEquals(2, result.size() );
		assertEquals("ea", result.get(0).getValue() );
		assertEquals("bf", result.get(1).getValue() );
		
		word = new Word(false);
		word.addLetter(new InputLetter(2, 0, "g"));
		InputLetter dot = new InputLetter(2, 1, ".");
		dot.setExists();
		word.addLetter(dot);
		word.addLetter(new InputLetter(2, 2, "h"));
		result = proc.playWord(word);
		
		assertEquals(1, result.size() );
		assertEquals("fh", result.get(0).getValue() );
		assertEquals(1, result.get(0).getHeadX() );
		assertEquals(2, result.get(0).getHeadY() );
		assertEquals(2, result.get(0).getMainX() );
		assertEquals(2, result.get(0).getMainY() );
		assertEquals(2, result.get(0).getLen() );
		
		board = new Board();
		board.init(3, 4);
		for(int i=0;i<4;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		
		board.getTile(0, 0).addPiece(new Piece(map.get("e")));
		board.getTile(2, 0).addPiece(new Piece(map.get("g")));
		board.getTile(2, 2).addPiece(new Piece(map.get("h")));
		board.getTile(1, 2).addPiece(new Piece(map.get("f")));
		proc.setBoard(board);
		
		word = new Word(true);
		word.addLetter(new InputLetter(2, 1, "c"));
		word.addLetter(new InputLetter(3, 1, "d"));
		result = proc.playWord(word);
		
		assertEquals(1, result.size() );
		assertEquals("gch", result.get(0).getValue() );
		assertEquals(2, result.get(0).getHeadX() );
		assertEquals(0, result.get(0).getHeadY() );
		assertEquals(2, result.get(0).getMainX() );
		assertEquals(1, result.get(0).getMainY() );
		assertEquals(3, result.get(0).getLen() );
		
		word = new Word(true);
		word.addLetter(new InputLetter(0, 1, "a"));
		word.addLetter(new InputLetter(1, 1, "b"));
		result = proc.playWord(word);

		assertEquals(2, result.size() );
		assertEquals("ea", result.get(0).getValue() );
		assertEquals(0, result.get(0).getHeadX() );
		assertEquals(0, result.get(0).getHeadY() );
		assertEquals(0, result.get(0).getMainX() );
		assertEquals(1, result.get(0).getMainY() );
		assertEquals(2, result.get(0).getLen() );
		assertEquals("bf", result.get(1).getValue() );
		assertEquals(1, result.get(1).getHeadX() );
		assertEquals(1, result.get(1).getHeadY() );
		assertEquals(1, result.get(1).getMainX() );
		assertEquals(1, result.get(1).getMainY() );
		assertEquals(2, result.get(1).getLen() );

		board = new Board();
		board.init(3, 4);
		for(int i=0;i<4;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		
		board.getTile(2, 0).addPiece(new Piece(map.get("g")));
		board.getTile(2, 2).addPiece(new Piece(map.get("h")));
		board.getTile(1, 2).addPiece(new Piece(map.get("f")));
		proc.setBoard(board);
		
		word = new Word(true);
		word.addLetter(new InputLetter(0, 1, "a"));
		word.addLetter(new InputLetter(1, 1, "b"));
		word.addLetter(new InputLetter(2, 1, "c"));
		word.addLetter(new InputLetter(3, 1, "d"));
		proc.playWord(word);
		
		word = new Word(false);
		word.addLetter(new InputLetter(0, 0, "e"));
		result = proc.playWord(word);
		
		assertTrue(result.isEmpty() );
	}
	
	@Test
	public void scoreOneWordTest() {
		//[a b c]
		BoardProcessor proc = new BoardProcessor();
		CharToLetterMap map = new CharToLetterMap();
		map.add(new Letter("a", 1));
		map.add(new Letter("b", 2));
		map.add(new Letter("c", 4));
		proc.setLetterMap(map);
		Board board = new Board();
		board.init(1, 3);
		for(int i=0;i<3;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
		}
		proc.setBoard(board);
		
		Word word = new Word(true);
		word.addLetter(new InputLetter(0, 0, "a"));
		word.addLetter(new InputLetter(1, 0, "b"));
		word.addLetter(new InputLetter(2, 0, "c"));
		ExtendedWord exWord = new ExtendedWord(0, 0, "", false);
		List<BoardWord> otherWords = proc.playWord(word);
		//test letter value
		assertEquals( 7, proc.score( exWord, word, otherWords) );
		
		board = new Board();
		board.init(1, 3);
		board.setTile(0, 0, new Tile(Value.NONE));
		board.setTile(1, 0, new Tile(Value.DL));
		board.setTile(2, 0, new Tile(Value.TL));
		proc.setBoard(board);
		otherWords = proc.playWord(word);
		//test tile letter score
		assertEquals( 17, proc.score( exWord, word, otherWords) );
		
		board = new Board();
		board.init(1, 3);
		board.setTile(0, 0, new Tile(Value.NONE));
		board.setTile(1, 0, new Tile(Value.DW));
		board.setTile(2, 0, new Tile(Value.TW));
		proc.setBoard(board);
		otherWords = proc.playWord(word);
		//test tile word score
		assertEquals( 35, proc.score( exWord, word, otherWords) );
		
		board = new Board();
		board.init(1, 7);
		board.setTile(0, 0, new Tile(Value.NONE));
		board.setTile(1, 0, new Tile(Value.DL));
		board.setTile(2, 0, new Tile(Value.DW));
		board.setTile(3, 0, new Tile(Value.DL));
		board.setTile(4, 0, new Tile(Value.DL));
		board.setTile(5, 0, new Tile(Value.NONE));
		board.setTile(6, 0, new Tile(Value.NONE));
		proc.setBoard(board);
		word.addLetter(new InputLetter(3, 0, "a"));
		word.addLetter(new InputLetter(4, 0, "b"));
		word.addLetter(new InputLetter(5, 0, "c"));
		word.addLetter(new InputLetter(6, 0, "a"));
		otherWords = proc.playWord(word);
		//test tile letter combined with word score
		assertEquals( 90, proc.score( exWord, word, otherWords ) );
		
		board = new Board();
		board.init(1, 6);
		board.setTile(0, 0, new Tile(Value.NONE));
		board.setTile(1, 0, new Tile(Value.NONE));
		board.setTile(2, 0, new Tile(Value.DW));
		board.setTile(3, 0, new Tile(Value.NONE));
		board.setTile(4, 0, new Tile(Value.DL));
		board.setTile(5, 0, new Tile(Value.DL));
		proc.setBoard(board);
		word = new Word(true);
		word.addLetter(new InputLetter(0, 0, "a"));
		word.addLetter(new InputLetter(1, 0, "a"));
		otherWords = proc.playWord(word);
		assertEquals( 2, proc.score( exWord, word, otherWords ) );
		word = new Word(true);
		word.addLetter(new InputLetter(4, 0, "c"));
		word.addLetter(new InputLetter(5, 0, "c"));
		otherWords = proc.playWord(word);
		assertEquals( 16, proc.score( exWord, word, otherWords ) );
		word = new Word(true);
		word.addLetter(new InputLetter(2, 0, "b"));
		word.addLetter(new InputLetter(3, 0, "b"));
		otherWords = proc.playWord(word);
		assertEquals( 8, proc.score( exWord, word, otherWords ) );
		ExtendedWord exWord2 = new ExtendedWord(2, 2, "aabbcc", false);
		//test score from exWord
		//(2 + 4 + 8) * 2
		assertEquals( 28, proc.score( exWord2, word, otherWords ) );
		
		//test horizontal
		board = new Board();
		board.init(5, 1);
		board.setTile(0, 0, new Tile(Value.NONE));
		board.setTile(0, 1, new Tile(Value.NONE));
		board.setTile(0, 2, new Tile(Value.DW));
		board.setTile(0, 3, new Tile(Value.DL));
		board.setTile(0, 4, new Tile(Value.DL));
		proc.setBoard(board);

		word = new Word(false);
		word.addLetter(new InputLetter(0, 0, "a"));
		word.addLetter(new InputLetter(0, 1, "a"));
		otherWords = proc.playWord(word);
		assertEquals( 2, proc.score( exWord, word, otherWords ) );
		word = new Word(false);
		word.addLetter(new InputLetter(0, 3, "c"));
		word.addLetter(new InputLetter(0, 4, "c"));
		otherWords = proc.playWord(word);
		assertEquals( 16, proc.score( exWord, word, otherWords ) );
		word = new Word(false);
		word.addLetter(new InputLetter(0, 2, "b"));
		otherWords = proc.playWord(word);
		assertEquals( 4, proc.score( exWord, word, otherWords ) );
		assertEquals( 24, proc.score( exWord2, word, otherWords ) );
	}
	
	@Test
	public void scoreMultipleWordsTest() {
		// d . f    .  .  .
		//[a b c]   DL .  DW
		// . e g    .  TW TL
		BoardProcessor proc = new BoardProcessor();
		CharToLetterMap map = new CharToLetterMap();
		map.add(new Letter("a", 1));
		map.add(new Letter("b", 2));
		map.add(new Letter("c", 4));
		map.add(new Letter("d", 8));
		map.add(new Letter("e", 10));
		map.add(new Letter("f", 20));
		map.add(new Letter("g", 40));
		proc.setLetterMap(map);
		Board board = new Board();
		board.init(3, 3);
		for(int i=0;i<3;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		board.setTile(0, 1, new Tile(Value.DL));
		board.setTile(2, 1, new Tile(Value.DW));
		board.setTile(1, 2, new Tile(Value.TW));
		board.setTile(2, 2, new Tile(Value.TL));
		
		board.getTile(0, 0).addPiece(new Piece(map.get("d")));
		board.getTile(1, 2).addPiece(new Piece(map.get("e")));
		board.getTile(2, 0).addPiece(new Piece(map.get("f")));
		board.getTile(2, 2).addPiece(new Piece(map.get("g")));
		proc.setBoard(board);
		
		Word word = new Word(true);
		word.addLetter(new InputLetter(0, 1, "a"));
		word.addLetter(new InputLetter(1, 1, "b"));
		word.addLetter(new InputLetter(2, 1, "c"));
		ExtendedWord exWord = new ExtendedWord(0, 0, "", false);
		List<BoardWord> otherWords = proc.playWord(word);
		
		List<BoardWord> otherWordsEmpty = new ArrayList<BoardWord>();
		assertEquals( 16, proc.score( exWord, word, otherWordsEmpty ) );
		
		//16 + 10 + 12 + 128
		assertEquals( 166, proc.score( exWord, word, otherWords ) );
		
		
		//  |a|d    .  DL .
		// e|b|     TW .  .
		// g|c|f    TL DW .
		board = new Board();
		board.init(3, 3);
		for(int i=0;i<3;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		board.setTile(1, 0, new Tile(Value.DL));
		board.setTile(1, 2, new Tile(Value.DW));
		board.setTile(0, 1, new Tile(Value.TW));
		board.setTile(0, 2, new Tile(Value.TL));
		
		board.getTile(2, 0).addPiece(new Piece(map.get("d")));
		board.getTile(0, 1).addPiece(new Piece(map.get("e")));
		board.getTile(2, 2).addPiece(new Piece(map.get("f")));
		board.getTile(0, 2).addPiece(new Piece(map.get("g")));
		proc.setBoard(board);
		
		word = new Word(false);
		word.addLetter(new InputLetter(1, 0, "a"));
		word.addLetter(new InputLetter(1, 1, "b"));
		word.addLetter(new InputLetter(1, 2, "c"));
		List<BoardWord> otherWords2 = proc.playWord(word);
		assertEquals( 16, proc.score( exWord, word, otherWordsEmpty ) );
		
		//16 + 10 + 12 + 128
		assertEquals( 166, proc.score( exWord, word, otherWords2 ) );
		
		//    e
		//[a] b [c]
		// d     f
		board = new Board();
		board.init(3, 3);
		for(int i=0;i<3;i++) {
			board.setTile(i, 0, new Tile(Value.NONE));
			board.setTile(i, 1, new Tile(Value.NONE));
			board.setTile(i, 2, new Tile(Value.NONE));
		}
		board.getTile(0, 2).addPiece(new Piece(map.get("d")));
		board.getTile(1, 0).addPiece(new Piece(map.get("e")));
		board.getTile(2, 2).addPiece(new Piece(map.get("f")));
        proc.setBoard(board);
        
        word = new Word(false);
        word.addLetter(new InputLetter(1, 1, "b"));
        proc.playWord(word);
        exWord = new ExtendedWord(1, 0, "eb", false);
        assertEquals( 12, proc.score( exWord, word, otherWordsEmpty ) );
		
		word = new Word(true);
		word.addLetter(new InputLetter(0, 1, "a"));
		InputLetter dot = new InputLetter(1, 1, ".");
		dot.setExists();
		word.addLetter(dot);
		word.addLetter(new InputLetter(2, 1, "c"));
		exWord = new ExtendedWord(0, 0, "abc", true);
		List<BoardWord> otherWords3 = proc.playWord(word);
        assertEquals( 7, proc.score( exWord, word, otherWordsEmpty ) );
		
		//7 + 9 + 24
		assertEquals( 40, proc.score( exWord, word, otherWords3 ) );
	}
}
