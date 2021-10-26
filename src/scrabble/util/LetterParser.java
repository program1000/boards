package scrabble.util;

import java.util.stream.Stream;

import scrabble.board.model.Bag;
import scrabble.board.model.Letter;

public class LetterParser implements ParseInterface {
	
	private Bag bag;
	private CharToLetterMap map;

	public LetterParser( Bag newBag, CharToLetterMap newMap ) {
		bag = newBag;
		map = newMap;
	}

	public long parse(Stream<String> lines) {
	
		return lines.skip(1).map(line -> {
			String[] args = line.split("\\s+");
			Letter letter = new Letter(args[0],Integer.valueOf(args[1]));
			int amount = Integer.valueOf(args[2]);
			for(int i=0;i<amount;i++) {
				bag.add(letter);				
			}
			map.add(letter);
			return letter;
			} ).count();
	}
	
}


