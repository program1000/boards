package boards.scrabble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import scrabble.board.model.Bag;
import scrabble.board.model.Letter;

public class BagTest {

	@Test
	public void shuffleTest() {
		Bag bag = new Bag();
		bag.add(new Letter("A",0));
		bag.add(new Letter("B",0));
		bag.add(new Letter("C",0));
		bag.add(new Letter("D",0));
		bag.add(new Letter("E",0));
		bag.shuffle();
		assertEquals(5,bag.size());
		List<String> all = new ArrayList<String>();
		all.addAll(Arrays.asList("A","B","C","D","E"));
		for( Letter letter : bag.get(all.size()) ) {
			assertTrue( all.remove(letter.getChar() ) );
		}
		assertEquals(true,all.isEmpty());
		assertEquals(0,bag.size());
	}
}
