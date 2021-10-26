package scrabble.util;

import java.util.stream.Stream;

import scrabble.dictionary.DictonaryDb;

public class DictionaryParser implements ParseInterface{
	
	private DictonaryDb dictionaryDb;
	
	public DictionaryParser(DictonaryDb newDictionaryDb) {
		dictionaryDb = newDictionaryDb;
	}

	@Override
	public long parse(Stream<String> lines) {
		dictionaryDb.startBatch();
		int minLength = dictionaryDb.getMin();
		int maxLength = dictionaryDb.getMax();
		long amount = lines.filter(line->line.length()>=minLength&&line.length()<=maxLength).
				filter( line-> line.matches("[^\\s\\p{Punct}]*")).
				map( word -> dictionaryDb.addToBatch(word)).count();
		dictionaryDb.executeBatch();
		return amount;
	}

}
