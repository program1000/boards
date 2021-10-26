package scrabble.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scrabble.board.PieceWalker.Direction;
import scrabble.board.model.Board;
import scrabble.board.model.BoardException;
import scrabble.board.model.BoardWord;
import scrabble.board.model.ExtendedWord;
import scrabble.board.model.Tile;
import scrabble.input.model.InputLetter;
import scrabble.input.model.Word;
import scrabble.util.CharToLetterMap;

public class BoardProcessor {
	private PieceWalker pieceWalker;
	private TileWalker tileWalker;
	private CharToLetterMap letterMap;
	private Board board;
	private Map<Direction, Direction> horizontalToVerticalMap;
	
	public static final String ERR_EMPTY_WORD = "Empty word";
	public static final String ERR_LETTER = "Letter ";
	public static final String ERR_LETTER_LOCATION_OUT_OF_BOUNDS = " outside board bounds";
	public static final String ERR_LETTER_NOT_VALID = " is not valid";
	public static final String ERR_LETTER_ON_EMPTY_TILE = " on empty spot";
	public static final String ERR_LETTER_ON_OCCUPIED_TILE = " on occupied spot";
	public static final String ERR_LETTER_DIFFERENT_ROW = " in different row";
	public static final String ERR_LETTER_DIFFERENT_COLUMN = " in different column";
	public static final String ERR_WORD_NOT_ON_START_POSITION = "Word not on start position";
	public static final String ERR_WORD_NOT_CONNECTED_ON_BOARD = "Word not connected on board";
	
	public BoardProcessor() {
		pieceWalker = new PieceWalker();
		tileWalker = new TileWalker(); 
		horizontalToVerticalMap = new HashMap<Direction, Direction>();
		horizontalToVerticalMap.put(Direction.LEFT, Direction.UP);
		horizontalToVerticalMap.put(Direction.RIGHT, Direction.DOWN);
		horizontalToVerticalMap.put(Direction.UP, Direction.LEFT);
		horizontalToVerticalMap.put(Direction.DOWN, Direction.RIGHT);
	}
	
	public void setLetterMap( CharToLetterMap map ) {
		letterMap = map;
	}
	
	public void setBoard( Board newBoard ) {
		board = newBoard;
		tileWalker.setBoard(board);
	}
	

	public void checkInput( Word newWord ) throws BoardException {
		//Empty
		if( newWord.getLetters().isEmpty()) {
			throw new BoardException(ERR_EMPTY_WORD);
		}
		
		int sx = newWord.getHead().getX();
		int sy = newWord.getHead().getY();
		boolean isHorizontal = newWord.isHorizontal();
		int cx=0;
		int cy=0;
		for(InputLetter l : newWord.getLetters()) {
			cx = l.getX();
			cy = l.getY();
			//check valid letter
			if ( l.isExists()==false&&letterMap.hasLetter(l.getChar())==false) {
				throw new BoardException(ERR_LETTER + l.getChar() + ERR_LETTER_NOT_VALID);
			}
			//check location in bounds and tile is empty
			Tile tile = board.getTile(cx, cy);
			if(tile==null) {
				throw new BoardException(ERR_LETTER + l.getChar() + ERR_LETTER_LOCATION_OUT_OF_BOUNDS);
			} else if( l.isExists() && tile.isEmpty() ) {
				throw new BoardException(ERR_LETTER + l.getChar() + ERR_LETTER_ON_EMPTY_TILE);
			} else if (tile.isEmpty() == false && l.isExists()==false ) {
				throw new BoardException(ERR_LETTER + l.getChar() + ERR_LETTER_ON_OCCUPIED_TILE);
			}
			//check direction and if pieces are consecutive
			if(sx!=cx) {
				throw new BoardException(ERR_LETTER + l.getChar() + ERR_LETTER_DIFFERENT_COLUMN);
			}
			if(sy!=cy) {
				throw new BoardException(ERR_LETTER + l.getChar() + ERR_LETTER_DIFFERENT_ROW);
			}
			sx=tileWalker.getNextX(sx,convertDirection(Direction.RIGHT, isHorizontal));
			sy=tileWalker.getNextY(sy,convertDirection(Direction.RIGHT, isHorizontal));
		}
	}
	
	public void checkStartPosition( Word newWord ) throws BoardException {
		int sx = board.getStartTileX();
		int sy = board.getStartTileY();
		for(InputLetter l : newWord.getLetters()) {
			if( l.getX()==sx && l.getY()==sy  ) {
				return;
			}
		}
		throw new BoardException(ERR_WORD_NOT_ON_START_POSITION);
	}
	

	public void checkConnected( ExtendedWord exWord, List<BoardWord> allWords) throws BoardException {
		boolean isInline = exWord.getPreLen() >0 || exWord.getPostLen()>0;
		if ( isInline ) {
			return;
		}
		if( allWords.isEmpty() && exWord.hasExist()==false) {
			throw new BoardException(ERR_WORD_NOT_CONNECTED_ON_BOARD);
		}
		
	}
	
	public ExtendedWord createWord( Word newWord ) {
		List<InputLetter> letters = newWord.getLetters();
		InputLetter head = newWord.getHead();
		Piece ltemp = createPiece( head );
		String result="";
		boolean isHorizontal=newWord.isHorizontal();
		String pre = mixWithBoard(ltemp, head.getX(), head.getY(), convertDirection(Direction.LEFT, isHorizontal) );
		int min = pre.length();
		result+=pre;
		boolean hasExist = false;
		for(InputLetter l : letters ) {
			if (l.isExists()) {
				result+=board.getTile(l.getX(), l.getY()).getPiece().getChar();
				hasExist=true;
			} else {
			result+=l.getChar();
			}
			
		}
		InputLetter tail = newWord.getTail();
		Piece rtemp = createPiece( tail );
		String post = mixWithBoard(rtemp, tail.getX(), tail.getY(), convertDirection(Direction.RIGHT, isHorizontal) );
		int max = post.length();
		result+=post;

		return new ExtendedWord(min, max, result,hasExist);
	}
	
	private Direction convertDirection( Direction direction, boolean isHorizontal ) {
        if (isHorizontal==false) {
			return horizontalToVerticalMap.get(direction);
		}
        return direction;
	}
	
	private String mixWithBoard(Piece piece, int x, int y, Direction direction) {
		String result = "";
		if ( attachPiece( piece, x, y, direction ) ) {
			for(Piece p : pieceWalker.move(piece, direction) ) {
				result+=p.getChar();
			}
			piece.remove(direction);
		}
		return result;
	}
	
	public List<BoardWord> playWord( Word newWord ) {
	
		Set<Piece> existPieces = new HashSet<Piece>();
		Piece[] pieces = attachWord( newWord, existPieces );

		return collectWords( newWord, pieces[0], pieces[1], existPieces );
	}
	
	private Piece[] attachWord( Word newWord, Set<Piece> existPieces ) {
		//Place pieces on tiles
		Piece newPiece = null;
		InputLetter head = newWord.getHead();
		InputLetter tail = newWord.getTail();
		Piece previous = null;
		boolean isHorizontal = newWord.isHorizontal();
		Piece[] result = new Piece[2];

		for(InputLetter l : newWord.getLetters() ) {
			if( l.isExists() ) {
				newPiece = board.getTile(l.getX(), l.getY()).getPiece();
			    existPieces.add(newPiece);
			} else {
			    newPiece = createPiece(l);
			}
			if( previous!=null ) {
    		    previous.attach(newPiece, convertDirection(Direction.RIGHT, isHorizontal) );
			}
			if( l.isExists() == false ) { 
			     board.getTile(l.getX(),l.getY()).addPiece(newPiece);
     	         attachPiece( newPiece, l.getX(), l.getY(), convertDirection(Direction.UP, isHorizontal) );
		         attachPiece( newPiece, l.getX(), l.getY(), convertDirection(Direction.DOWN, isHorizontal) );
			}

			if(  head==l) {
				attachPiece( newPiece, l.getX(), l.getY(), convertDirection(Direction.LEFT, isHorizontal) );
				result[0]=newPiece;
			} 
			if ( tail==l) {
				attachPiece( newPiece, l.getX(), l.getY(), convertDirection(Direction.RIGHT, isHorizontal) );
				result[1]=newPiece;
			}
			previous = newPiece;
		}
		return result;
	}
	
	private boolean attachPiece( Piece newPiece, int x, int y, Direction direction ) {
		Tile tile = tileWalker.getNextTile(x, y, direction);
		if( tile!=null && tile.isEmpty()==false ) {
			newPiece.attach(tile.getPiece(), direction);
			return true;
		}
		return false;
	}
	
	private List<BoardWord> collectWords( Word newWord, Piece head, Piece tail, Set<Piece> existPieces ) {
		int headX=newWord.getHead().getX();
		int headY=newWord.getHead().getY();
		boolean isHorizontal = newWord.isHorizontal();
		
		List<BoardWord> words = new ArrayList<BoardWord>();

		int currentPos=0;
		if( isHorizontal ) {
			currentPos = headX;
		} else {
			currentPos = headY;
		}
		for( Piece current : pieceWalker.moveUntil(head, tail, existPieces, convertDirection(Direction.RIGHT, isHorizontal) ) ){
			if( current!=null ) {
			    String word = "";
			    List<Piece> tmp = pieceWalker.move(current, convertDirection(Direction.UP, isHorizontal) );
			    for(Piece t : tmp ) {
			        word+=t.getChar();
			    }
			    int preLen = tmp.size();
			    word+=current.getChar();
			    tmp = pieceWalker.move(current, convertDirection(Direction.DOWN, isHorizontal));
			    for(Piece t : tmp ) {
			        word+=t.getChar();
			    }
			    int postLen = tmp.size();
			    if ( word.length()>1 ) {
			         if (isHorizontal) {
			       	     words.add(new BoardWord(currentPos,headY-preLen,currentPos,headY,preLen+postLen+1,word));
			         } else {
			             words.add(new BoardWord(headX-preLen,currentPos,headX,currentPos,preLen+postLen+1,word));
			         }
			    }
			}
			currentPos++;
		}

		return words;
	}
	
	public int score( ExtendedWord exWord, Word newWord, List<BoardWord> otherWords ) {
		InputLetter head = newWord.getHead();
		InputLetter tail = newWord.getTail();
		Points total = new Points();
		Points mainPoints = new Points();
		boolean isHorizontal = newWord.isHorizontal();
		// scan inputletter property 'exist' to split tiles into score existing and new letters
		List<Tile> newTiles = new ArrayList<Tile>();
		List<Tile> existTiles = new ArrayList<Tile>();
		for( InputLetter l: newWord.getLetters() ) {
			if ( l.isExists()==false ) {
				newTiles.add(board.getTile(l.getX(),l.getY()));
			} else {
				existTiles.add(board.getTile(l.getX(),l.getY()));
			}
		}
		Points newWordPoints = scoreInDirection( newTiles, true );
		mainPoints.addPoints(newWordPoints.getPoints());
		mainPoints.addMultiplier(newWordPoints.getMultiplier());
		Points existWordPoints = scoreInDirection( existTiles, false );
		mainPoints.addPoints(existWordPoints.getPoints());

		if ( exWord.getPreLen() > 0 ) {
		     Points extPrePoints = scoreInDirection( collectTiles(exWord.getPreLen(), head.getX(), head.getY(), false, convertDirection(Direction.LEFT, isHorizontal) ), false );
		     mainPoints.addPoints(extPrePoints.getPoints());
		}
		if ( exWord.getPostLen() > 0 ) {
		     Points extPostPoints = scoreInDirection( collectTiles(exWord.getPostLen(), tail.getX(), tail.getY(), false, convertDirection(Direction.RIGHT, isHorizontal) ), false );
		     mainPoints.addPoints(extPostPoints.getPoints());
		}
		total.addPoints(mainPoints.getTotal());
		for(BoardWord word : otherWords) {
			Points boardWordPoints = scoreInDirection( collectTiles(word.getLen(), word.getHeadX(), word.getHeadY(), true, convertDirection(Direction.DOWN, isHorizontal) ), false );
			Tile mainTile = board.getTile(word.getMainX(), word.getMainY());
			boardWordPoints.addPoints(-mainTile.score(false));
			boardWordPoints.addPoints(mainTile.score(true));
			boardWordPoints.addMultiplier(mainTile.multiplier());
			total.addPoints(boardWordPoints.getTotal());
		}
		if (newWord.size()==7) {
			total.addPoints(50);
		}
		return total.getPoints();
	}
	
	private List<Tile> collectTiles( int len, int x, int y, boolean addHead, Direction direction) {
		return tileWalker.collect(x, y, len, addHead, direction);
	}
	
	private Points scoreInDirection( List<Tile> tiles, boolean isNew ) {
		Points result= new Points();
		for(Tile tile : tiles ) {
			result.addPoints(tile.score(isNew));
			if( isNew ) {
			    result.addMultiplier(tile.multiplier());
			}
		}
		return result;
	}
	
	public Piece createPiece(InputLetter inputLetter) {
		if ( inputLetter.isBlanco() ) {
			return new Piece(letterMap.getBlanco(inputLetter.getChar()));
		}
		return new Piece(letterMap.get(inputLetter.getChar()));
	}

}
