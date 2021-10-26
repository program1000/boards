package boards.scrabble;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import scrabble.board.BoardProcessor;
import scrabble.board.model.Letter;
import scrabble.input.model.InputLetter;
import scrabble.input.model.Word;
import scrabble.player.PlayerException;
import scrabble.player.model.Hand;

public class HandTest {

	@Test
	public void addTest() {
		Hand hand = new Hand();
		assertEquals("",hand.toString());
		
		List<Letter> l = new ArrayList<Letter>();
		l.add(new Letter("a",0));
		hand.add(l);
		assertEquals("a",hand.toString());
		
		l.add(new Letter("b",0));
		hand.add(l);
		//a a b
		String str = hand.toString();
		assertEquals(5,str.length());
		assertTrue(str.contains("a"));
		assertTrue(str.contains("b"));
	}
	
	@Test
	public void checkTest() {
		Hand hand = new Hand();
		List<Letter> l = new ArrayList<Letter>();
		l.add(new Letter("a",0));
		l.add(new Letter("b",0));
		l.add(new Letter("a",0));
		hand.add(l);
		
		Word word = new Word(true);
		word.addLetter(new InputLetter(0, 0, "b"));
		assertDoesNotThrow(()-> {
			Map<String,Integer> result = hand.checkWord(word);
			assertEquals(1,result.size());
			assertEquals(2,result.get("a"));
		});
		
		Word word2 = new Word(true);
		word2.addLetter(new InputLetter(0, 0, "a"));
		word2.addLetter(new InputLetter(0, 0, "a"));
		assertDoesNotThrow(()-> {
			Map<String,Integer> result = hand.checkWord(word2);
			assertEquals(1,result.size());
			assertNull(result.get("a"));
			assertEquals(1,result.get("b"));
		});
		
		Word word3 = new Word(true);
		word3.addLetter(new InputLetter(0, 0, "a"));
		word3.addLetter(new InputLetter(0, 0, "c"));
		PlayerException e = assertThrows( PlayerException.class, () -> {
			hand.checkWord(word3);
		});
		assertEquals(BoardProcessor.ERR_LETTER + "c" + Hand.ERR_LETTER_DOES_NOT_MATCH_HAND, e.getMessage());
	}
	
	@Test
	public void newHandTest() {
		Hand hand = new Hand();
		List<Letter> l = new ArrayList<Letter>();
		l.add(new Letter("a",0));
		hand.add(l);
		
		Map<String, Integer> newLetterMap = new HashMap<String, Integer>();
		newLetterMap.put("b",1);
		List<Letter> newLetters = new ArrayList<Letter>();
		hand.newHand(newLetterMap, newLetters);
		assertEquals("b",hand.toString());
		
		newLetterMap = new HashMap<String, Integer>();
		newLetters.add(new Letter("c",0));
		newLetters.add(new Letter("c",0));
		newLetterMap.put("b",2);
		hand.newHand(newLetterMap, newLetters);
		String str = hand.toString();
		//b b c c
		assertEquals(7,str.length());
		assertTrue(str.contains("b"));
		assertTrue(str.contains("c"));
		assertFalse(str.contains("a"));
	}
}
