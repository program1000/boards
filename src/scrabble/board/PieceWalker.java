package scrabble.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PieceWalker {
	
	public enum Direction{LEFT,RIGHT,UP,DOWN}

	public List<Piece> move( Piece piece, Direction direction ) {
		ArrayDeque<Piece> stack = new ArrayDeque<Piece>();
		Piece nextPiece = getNextPiece(piece, direction);
		while( nextPiece!=null ) {
			if ( direction==Direction.RIGHT || direction==Direction.DOWN ) {
				stack.add(nextPiece);
			} else {
			    stack.addFirst(nextPiece);
			}
			nextPiece = getNextPiece(nextPiece, direction);
		}
		List<Piece> result = new ArrayList<Piece>();
		result.addAll(stack);
		return result;
	}
	
	public List<Piece> moveUntil( Piece head, Piece tail, Set<Piece> existPieces, Direction direction ) {
		List<Piece> result = new ArrayList<Piece>();
		Piece current = head;
		while( current!=tail && current!=null ) {
			if ( existPieces.contains(current)==false ) {
			    result.add(current);
			} else {
				result.add(null);
			}
			current = getNextPiece(current, direction);
		}
		if ( current!=null) {
			result.add(current);
		}
		return result;
	}
	
	public Piece getNextPiece(Piece current, Direction direction) {
		switch( direction ){
		case LEFT:
			return current.getLeft();
		case RIGHT:
			return current.getRight();
		case DOWN:
			return current.getDown();
		case UP:
			return current.getUp();
		default:
			throw new RuntimeException("Missing direction");
			
		}
	}
	
}
