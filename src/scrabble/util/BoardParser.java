package scrabble.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import scrabble.board.model.Board;
import scrabble.board.model.Tile;
import scrabble.board.model.Tile.Value;

public class BoardParser implements ParseInterface {
	
	public static final String START_VALUE="*";
	
	private Board board;
	private Tile startTile;
	int rows;
	
	public BoardParser( Board newBoard ) {
		board = newBoard;
		startTile = null;
	}

	@Override
	public long parse(Stream<String> lines) {
		int x=0;
		int y=0;
		
		List<Tile> tiles = lines.flatMap(line -> {
			String[] row= new String[line.length()/2];
			for(int i=0;i<line.length();i+=2) {
				row[i/2]=line.substring(i, i+2);
			}
			rows++;
			return Stream.of(row);
		}).map(value -> createTile(value)).collect(Collectors.toList());
		
		int columns = tiles.size()/rows;
		board.init(rows, columns);
		for(Tile tile: tiles) {
			board.setTile(x, y, tile);
			if ( tile==startTile ) {
				board.setStartTile(x,y);
			}
			x++;
			if( x==columns ) {
				x=0;
				y++;
			}
		}
		return tiles.size();
	}
	
	private Value parseValue(String value) {
		try {
		    return Value.valueOf(value);
		}catch( IllegalArgumentException e) {
			return Value.NONE;
		}
	}
	
	private Tile createTile(String value) {
		if(value.trim().equals(START_VALUE)) {
			Tile tile = new Tile(Value.DW);
			startTile = tile;
			return tile;
		}
		return new Tile( parseValue(value));
	}

}
