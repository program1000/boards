package scrabble.board.model;

public class Board {

	private Tile[][] tiles;
	private int maxX=0;
	private int maxY=0;
	private boolean hasStartTile=false;
	private int startX=0;
	private int startY=0;

	
	public void init(int rows, int columns) {
		tiles = new Tile[columns][rows];
		maxX=columns;
		maxY=rows;
	}
	
	public Tile getTile(int x, int y) {
		if( isInBounds(x, y) ) {
		    return tiles[x][y];
		}
		return null;
	}
	
	public void setTile(int x, int y, Tile tile) {
		if( isInBounds(x, y) ) {
		    tiles[x][y]=tile;
		}
	}
	
	public boolean isInBounds(int x, int y) {
		return x>=0&&x<maxX && y>=0&&y<maxY;
	}
	
	public boolean hasStartTile() {
		return hasStartTile;
	}
	
	public void setStartTile(int x, int y) {
		startX=x;
		startY=y;
		hasStartTile = true;
	}
	
	public int getStartTileX() {
		return startX;
	}
	
	public int getStartTileY() {
		return startY;
	}
	
	public boolean isValid() {
		if ( maxX==0 ) {
			return false;
		}
		if ( maxY==0 ) {
			return false;
		}
		for( int y=0;y<maxY;y++) {
		    for( int x=0;x<maxX;x++) {
		    	if( getTile(x,y)==null ) {
		    		return false;
		    	}
		    }
		}
		return true;
	}
	
	public void print() {
		for( int y=0;y<maxY;y++) {
		    for( int x=0;x<maxX;x++) {
		    	System.out.print(getTile(x,y));
		    }
		    System.out.println();
		}
	}
	
}
