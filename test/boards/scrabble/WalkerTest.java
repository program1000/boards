package boards.scrabble;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import scrabble.board.Piece;
import scrabble.board.PieceWalker;
import scrabble.board.TileWalker;
import scrabble.board.PieceWalker.Direction;
import scrabble.board.model.Board;
import scrabble.board.model.Letter;
import scrabble.board.model.Tile;
import scrabble.board.model.Tile.Value;


public class WalkerTest {

	@TestFactory
	Stream<DynamicTest> walkWithNoElementsTest() {
		Piece p = new Piece(new Letter("",0));
		PieceWalker w = new PieceWalker();
		return Arrays.stream(Direction.values()).map(direction -> DynamicTest.dynamicTest(
				direction + "",
				() -> {
					assertEquals(true, w.move(p,  direction).isEmpty());
				}));
	}
	
	@TestFactory
	Stream<DynamicTest> walkRightDownTest() {
		List<Direction> directionList = Arrays.asList(Direction.RIGHT, Direction.DOWN);
		return directionList.stream().map(direction -> DynamicTest.dynamicTest(
				direction + "",
				() -> {
					Piece p = new Piece(new Letter("0",0));
					Piece dp = new Piece(new Letter("",0));
					p.attach(dp, direction);
					PieceWalker w = new PieceWalker();
					// return 1 piece
					List<Piece> result = w.move(p,  direction);
					assertEquals(1, result.size() );
					assertEquals(dp, result.get(0) );
					// return 3 piece
					Piece dp2 = new Piece(new Letter("",0));
					Piece dp3 = new Piece(new Letter("",0));
					dp.attach(dp2, direction);
					dp2.attach(dp3, direction);
					result = w.move(p,  direction);
					assertEquals(3, result.size() );
					assertEquals(dp, result.get(0) );
					assertEquals(dp2, result.get(1) );
					assertEquals(dp3, result.get(2) );
				}));
	}
	
	@TestFactory
	Stream<DynamicTest> walkLeftUpTest() {
		List<Direction> directionList = Arrays.asList(Direction.LEFT, Direction.UP);
		return directionList.stream().map(direction -> DynamicTest.dynamicTest(
				direction + "",
				() -> {
					Piece p = new Piece(new Letter("",0));
					Piece dp = new Piece(new Letter("",0));
					p.attach(dp, direction);
					PieceWalker w = new PieceWalker();
					// return 1 piece
					List<Piece> result = w.move(p,  direction);
					assertEquals(1, result.size() );
					assertEquals(dp, result.get(0) );
					// return 3 piece
					Piece dp2 = new Piece(new Letter("",0));
					Piece dp3 = new Piece(new Letter("",0));
					dp.attach(dp2, direction);
					dp2.attach(dp3, direction);
					result = w.move(p,  direction);
					assertEquals(3, result.size() );
					assertEquals(dp3, result.get(0) );
					assertEquals(dp2, result.get(1) );
					assertEquals(dp, result.get(2) );
				}));
	}
	
	@Test
	public void tileWalkLeftTest() {
		Board board = new Board();
		board.init(1, 2);
		Tile t1 = new Tile(Value.NONE);
		Tile t2 = new Tile(Value.NONE);
		board.setTile(0, 0, t1);
		board.setTile(1, 0, t2);
		TileWalker tw = new TileWalker();
		tw.setBoard(board);
		assertEquals(t1, tw.getNextTile(1, 0, Direction.LEFT));
		List<Tile> tiles = tw.collect(1, 0, 1, false, Direction.LEFT);
		assertEquals(1, tiles.size());
		assertEquals(t1, tiles.get(0));
		tiles = tw.collect(1, 0, 2, true, Direction.LEFT);
		assertEquals(2, tiles.size());
		assertEquals(t2, tiles.get(0));
		assertEquals(t1, tiles.get(1));
	}
	
	@Test
	public void tileWalkRightTest() {
		Board board = new Board();
		board.init(1, 2);
		Tile t1 = new Tile(Value.NONE);
		Tile t2 = new Tile(Value.NONE);
		board.setTile(0, 0, t1);
		board.setTile(1, 0, t2);
		TileWalker tw = new TileWalker();
		tw.setBoard(board);
		assertEquals(t2, tw.getNextTile(0, 0, Direction.RIGHT));
		List<Tile> tiles = tw.collect(0, 0, 1, false, Direction.RIGHT);
		assertEquals(1, tiles.size());
		assertEquals(t2, tiles.get(0));
		tiles = tw.collect(0, 0, 2, true, Direction.RIGHT);
		assertEquals(2, tiles.size());
		assertEquals(t1, tiles.get(0));
		assertEquals(t2, tiles.get(1));
	}
	
	@Test
	public void tileWalkUpTest() {
		Board board = new Board();
		board.init(2, 1);
		Tile t1 = new Tile(Value.NONE);
		Tile t2 = new Tile(Value.NONE);
		board.setTile(0, 0, t1);
		board.setTile(0, 1, t2);
		TileWalker tw = new TileWalker();
		tw.setBoard(board);
		assertEquals(t1, tw.getNextTile(0, 1, Direction.UP));
		List<Tile> tiles = tw.collect(0, 1, 1, false, Direction.UP);
		assertEquals(1, tiles.size());
		assertEquals(t1, tiles.get(0));
		tiles = tw.collect(0, 1, 2, true, Direction.UP);
		assertEquals(2, tiles.size());
		assertEquals(t2, tiles.get(0));
		assertEquals(t1, tiles.get(1));
	}
	
	@Test
	public void tileWalkDownTest() {
		Board board = new Board();
		board.init(2, 1);
		Tile t1 = new Tile(Value.NONE);
		Tile t2 = new Tile(Value.NONE);
		board.setTile(0, 0, t1);
		board.setTile(0, 1, t2);
		TileWalker tw = new TileWalker();
		tw.setBoard(board);
		assertEquals(t2, tw.getNextTile(0, 0, Direction.DOWN));
		List<Tile> tiles = tw.collect(0, 0, 1, false, Direction.DOWN);
		assertEquals(1, tiles.size());
		assertEquals(t2, tiles.get(0));
		tiles = tw.collect(0, 0, 2, true, Direction.DOWN);
		assertEquals(2, tiles.size());
		assertEquals(t1, tiles.get(0));
		assertEquals(t2, tiles.get(1));
	}

}
