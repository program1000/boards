package scrabble.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scrabble.board.PieceWalker.Direction;
import scrabble.board.model.Board;
import scrabble.board.model.Tile;

public class TileWalker {
	
	private Map<Direction, Integer> xMap;
	private Map<Direction, Integer> yMap;
	private Board board;

	public TileWalker() {
		xMap = new HashMap<Direction, Integer>();
		xMap.put(Direction.LEFT,-1);
		xMap.put(Direction.RIGHT,1);
		xMap.put(Direction.UP,0);
		xMap.put(Direction.DOWN,0);
		yMap = new HashMap<Direction, Integer>();
		yMap.put(Direction.LEFT,0);
		yMap.put(Direction.RIGHT,0);
		yMap.put(Direction.UP,-1);
		yMap.put(Direction.DOWN,1);
	}
	
	public void setBoard( Board newBoard) {
		board = newBoard;
	}
	
	public Tile getNextTile(int x, int y, Direction direction) {
		return board.getTile(getNextX(x, direction), getNextY(y, direction));
	}
	
	protected int getNextX(int x, Direction direction) {
		return x+xMap.get(direction);
	}
	
	protected int getNextY(int y, Direction direction) {
		return y+yMap.get(direction);
	}
	
	public List<Tile> collect(int x, int y, int len, boolean addHead, Direction direction) {
		List<Tile> result = new ArrayList<Tile>();
		int begin=0;
		if ( addHead ) {
			result.add(board.getTile(x,y));
			begin++;
		}
		for(int i=begin;i<len;i++) {
			x = getNextX(x, direction);
			y = getNextY(y, direction);
			result.add( board.getTile(x,y) );
		}
		return result;
	}
}
