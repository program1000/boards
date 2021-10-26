package scrabble.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import framework.GameSession;
import framework.Player;
import scrabble.input.model.InputLetter;
import scrabble.input.model.Word;
import scrabble.player.model.ScrabblePlayer;

public class ConsoleSession extends GameSession implements ScrabbleSession, GameEventListener{
   // implements ScrabbleSession
   //  get players, score, save event
	
	public static final String TOKEN_BLANCO="#";
	public static final String TOKEN_PRE_IJ="[";
	public static final String TOKEN_EXIST=".";
	public static final String TOKEN_COMMAND="!";
	
	
	private Scanner in;
	private ConsoleGameEventProducer eventProducer;
	private List<ScrabblePlayer> players;
	private Turn turn;
	
	public ConsoleSession() {
		eventProducer = new ConsoleGameEventProducer();
	}
	
	
	public void init() {
		in = new Scanner(System.in);
		System.out.println("Amount of players?");
		int amount = in.nextInt();
		
		for(int i=0;i<amount;i++) {
		    System.out.println("Name? ");
		  
	        String name = in.next();
	        Player p = new Player(name);
	        addPlayer(p);
		}
		players = new ArrayList<ScrabblePlayer>();
	}
	
	public void close() {
		in.close();
	}
	
	public void connectToControl( GameEventProducer producer, GameEventListener listener ) {
		producer.addListener(this);
		eventProducer.addListener(listener);
	}
	
	public void connectToPlayerControl( GameEventProducer producer ) {
		producer.addListener(this);
	}
	
	public Word getWord() {
		String inWord = in.next();

		// preproces
		String[] chars = inWord.split("");
		String chr = null;
		List<ChrData> chList = new ArrayList<ChrData>();
		turn = Turn.PLAY_WORD;
		for (int i = 0; i < chars.length; i++) {
			chr = chars[i];
			switch (chr) {
			case TOKEN_BLANCO:
				chList.add(new ChrBlanco(chars[i + 1].toUpperCase()));
				i++;
				break;
			case TOKEN_EXIST:
				chList.add(new ChrExist( chr ));
				break;
			case TOKEN_PRE_IJ:
				chList.add(new ChrData(chars[i + 1].toUpperCase() + chars[i + 2].toUpperCase()));
				i += 2;
				break;
			case TOKEN_COMMAND:
				if (turn == Turn.SWAP) {
					turn = Turn.QUIT;
				} else {
					turn = Turn.SWAP;
				}
				break;
			default:
				chList.add(new ChrData(chr.toUpperCase()));
			}
			/*
			 * if (chr.equals(TOKEN_BLANCO)) { chList.add(new
			 * ChrData(chars[i+1].toUpperCase(),true)); i++; } else if(
			 * chr.equals(TOKEN_PRE_IJ) ) { chList.add(new
			 * ChrData(chars[i+1].toUpperCase()+chars[i+2].toUpperCase(),false)); i+=2; }
			 * else if( chr.equals(TOKEN_COMMAND) ) {
			 * 
			 * } else { chList.add(new ChrData(chr.toUpperCase(),false)); }
			 */
		}

		Word word = null;
		int x = 0 ;
		int y = 0 ;
		if (turn == Turn.PLAY_WORD) {
			System.out.println("Loc(x,y): ");
			String loc = in.next();
			String[] xyloc = loc.split(",");
			System.out.println("Horizonral (h/z): ");
			String isHor = in.next();
			word = new Word(isHor.equalsIgnoreCase("h"));
			x = Integer.valueOf(xyloc[0]);
			y = Integer.valueOf(xyloc[1]);
		} else {
			word = new Word(true);
		}
		
		//create word
		for(ChrData chrData: chList) {
			InputLetter il = new InputLetter(x, y, chrData.chr);
			if(chrData instanceof ChrBlanco) {
				il.setBlanco();
			} else if(chrData instanceof ChrExist) {
				il.setExists();
			}
			word.addLetter(il);
			if(word.isHorizontal()) {
			    x++;
			} else {
				y++;
			}
		}
		return word;
	}
	
	public Turn getTurn() {
		return turn;
	}

	@Override
	public void receive(GameEvent event) {
		switch(event) {
		case SHOW_INVALID_WORD:
			printInvalidWord();
			break;
		case START_GAME:
			eventProducer.fire(GameEvent.SHOW_BOARD);
			break;
		case END_TURN:
			eventProducer.fire(GameEvent.SHOW_BOARD);
			break;
		default:
			break;
		
		}
		
	}
	
	@Override
	public void receiveData(GameEvent event, Object... obj) {
		switch(event) {
		case FILL_HAND:
			players.add((ScrabblePlayer) obj[0]);
			break;
		case START_TURN:
			showHand((int) obj[0]);
			break;
		case ADD_POINTS:
			showScore((int)obj[0], (int)obj[1]);
		default:
			break;
		}
	}
	
	private void showHand( int id ) {
		ScrabblePlayer player = players.get(id);	
		System.out.println( player.getName() + ", ["+player.getHandStr()+"] input word (#G for Blanco, [IJ for IJ, W.RD for existing tile not required for beginning or end), !SW for swap):");
	}
	
	private void showScore( int points, int score ) {
		System.out.println("Points: "+points+ " Score: "+score);
	}
	
	private void printInvalidWord() {
		System.out.println("Word not in DB");
	}
	
	private class ConsoleGameEventProducer extends GameEventProducer {
		
	}


	private class ChrData{
	    String chr;
	    
	    private ChrData( String newChr ) {
	    	chr = newChr;
	    }
	}
	
	private class ChrBlanco extends ChrData{
		private ChrBlanco( String newChr ) {
			super( newChr );
		}
	}
	
	private class ChrExist extends ChrData{
		private ChrExist( String newChr ) {
			super( newChr );
		}
	}
}
