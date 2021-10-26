package scrabble.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import framework.GameSession;
import scrabble.board.BoardProcessor;
import scrabble.board.model.Bag;
import scrabble.board.model.Board;
import scrabble.board.model.BoardException;
import scrabble.board.model.BoardWord;
import scrabble.board.model.ExtendedWord;
import scrabble.board.model.WordList;
import scrabble.dictionary.DictionaryProcessor;
import scrabble.dictionary.DictonaryDb;
import scrabble.game.ScrabbleSession.Turn;
import scrabble.input.InputProcessor;
import scrabble.input.model.InputException;
import scrabble.input.model.Word;
import scrabble.player.PlayerException;
import scrabble.player.PlayerProcessor;
import scrabble.util.CharToLetterMap;

public class Control extends GameEventProducer implements GameEventListener, ControlActionInterface{
	
	private Bag bag;
	private Board board;
	private InputProcessor inputProcessor;
	private BoardProcessor boardProcessor;
	private PlayerProcessor playerProcessor;
	private DictionaryProcessor dictionaryProcessor;	
	private ScrabbleSession scrabbleSession;
	private boolean isOn;
	private int currentPlayerId=0;
	
	private List<ActionInterface> firstTurnActions;
	private List<ActionInterface> normalTurnActions;
	private List<ActionInterface> turnActions;
	private List<ActionInterface> swapActions;
	private List<ActionInterface> quitActions;
	private List<ActionInterface> currentActions;
	
	public Control() {
		isOn=true;
		firstTurnActions = new ArrayList<ActionInterface>();
		normalTurnActions = new ArrayList<ActionInterface>();
		turnActions = new ArrayList<ActionInterface>();
		swapActions = new ArrayList<ActionInterface>();
		quitActions = new ArrayList<ActionInterface>();
	}
	
	public void init(Config config, GameSession session, ScrabbleSession newScrabbleSession) {
		board = new Board();
		bag = new Bag();
		CharToLetterMap map = new CharToLetterMap();
		DictonaryDb db = new DictonaryDb();
		
		config.init(bag, board, map, db);
		inputProcessor = new InputProcessor();
		
		boardProcessor = new BoardProcessor();
		boardProcessor.setLetterMap(map);
		boardProcessor.setBoard(board);
		
		dictionaryProcessor = new DictionaryProcessor();
		dictionaryProcessor.setDb(db);
		
		playerProcessor = new PlayerProcessor();
		scrabbleSession = newScrabbleSession;
		scrabbleSession.connectToControl(this,this);
		scrabbleSession.connectToPlayerControl(playerProcessor);
		
		session.init();
		
		playerProcessor.setPlayers(session.getPlayers());
		initActions();
	}
	
	private void initActions() {
		InputCheckAction ica = new InputCheckAction(this); 
		PlacementCheckAction pca = new PlacementCheckAction(this);
		StartPositionCheckAction spca = new StartPositionCheckAction(this);
		LettersCheckAction lca = new LettersCheckAction(this);
		PlayWordAction pwa = new PlayWordAction(this);
		ConnectedCheckAction cca = new ConnectedCheckAction(this);
		ChangeLettersAction cla = new ChangeLettersAction(this);
		ScoreAction sa = new ScoreAction(this);
		NextPlayerAction npa = new NextPlayerAction(this);
		ChangeToNormalTurnAction ctnta = new ChangeToNormalTurnAction(this);
		QuitAction qa = new QuitAction(this);
		//normal turn
		//validate input
		//check input with board
		//check input with player's hand
		//collect/play word + check dictionary
		//check input with board if connected
		//renew hand
		//score
		//next player
		normalTurnActions.add(ica);
		normalTurnActions.add(pca);
		normalTurnActions.add(lca);
		normalTurnActions.add(pwa);
		normalTurnActions.add(cca);
		normalTurnActions.add(cla);
		normalTurnActions.add(sa);
		normalTurnActions.add(npa);
		//first turn
		// + check start tile
		// - check connected
		// + change to normal turn actions
		firstTurnActions.add(ica);
		firstTurnActions.add(pca);
		firstTurnActions.add(spca);
		firstTurnActions.add(lca);
		firstTurnActions.add(pwa);
		firstTurnActions.add(cla);
		firstTurnActions.add(sa);
		firstTurnActions.add(ctnta);
		firstTurnActions.add(npa);
		//swap
		//pre: suspend current action
		//check input with player's hand
		//renew hand
		//next player
		//post: resume current action
		swapActions.add(lca);
		swapActions.add(cla);
		swapActions.add(npa);
		//quit
		quitActions.add(qa);
		
		if( board.hasStartTile() ) {
			turnActions = firstTurnActions;
		} else {
			turnActions = normalTurnActions;
		}
	}
	
	public void start() {
		bag.shuffle();
		playerProcessor.fillHand( bag );
		fire(GameEvent.START_GAME);
		while( isOn) {
		    turn();
		    fire(GameEvent.END_TURN);
		}
		scrabbleSession.close();
	}
	
	private void printBoard() {
		board.print();
	}
	
	private void turn() {
		send(GameEvent.START_TURN, new Object[] {currentPlayerId});
		Word word = scrabbleSession.getWord();
		ActionContext context = new ActionContext(word);
		
		Turn turn = scrabbleSession.getTurn();
		if(turn==Turn.PLAY_WORD) {
			currentActions = turnActions;
		}else if(turn==Turn.SWAP) {
			currentActions = swapActions;
		}else if(turn==Turn.QUIT) {
			currentActions = quitActions;
		}
		
		for(int i=0; i< currentActions.size();i++) {
			currentActions.get(i).doAction(context);
			if ( context.isCheckSuccess() == false ) {
				//reset
				break;
			}
		}

	}
	
	public void checkInput(Word word) throws InputException {
		inputProcessor.checkInput(word);
	}
	
	public void checkInputWithBoard(Word word) throws BoardException {
		boardProcessor.checkInput(word);
	}
	
	public void checkStartPositionWithBoard(Word word) throws BoardException {
		boardProcessor.checkStartPosition(word);
	}
	
	public Map<String, Integer> checkLetterWithHand(Word word) throws PlayerException {
		return playerProcessor.checkInput(currentPlayerId, word);
	}
	
	public WordList playWord(Word word) {
		ExtendedWord exWord = boardProcessor.createWord(word);
		// check with dictionary
		if (dictionaryProcessor.checkWord(exWord)==false) {
			fire(GameEvent.SHOW_INVALID_WORD);
			return null;
		}
		return new WordList(exWord, boardProcessor.playWord(word));
	}
	
	public void checkConnected(ExtendedWord exWord, List<BoardWord> allWords) throws BoardException {
		boardProcessor.checkConnected(exWord, allWords);
	}
	
	public void changeLetters(Word word, Map<String, Integer> letterMap) {
		playerProcessor.changeLettersInHand(currentPlayerId, word, letterMap, bag );
	}
	
	public void score(Word word, ExtendedWord exWord, List<BoardWord> allWords) {
		int points = boardProcessor.score(exWord,word, allWords);
		playerProcessor.addPoints(currentPlayerId, points);
	}
	
	public void nextPlayer() {
		currentPlayerId=playerProcessor.getNextPlayerId( currentPlayerId );
	}
	
	public void changeToNormalTurn() {
		turnActions = normalTurnActions;
	}
	
	public void setQuitFlag() {
		isOn = false;
	}

	@Override
	public void receive(GameEvent event) {
		switch(event) {
		case SHOW_BOARD:
			printBoard();
			break;
		default:
			break;
		
		}
		
	}
	
	@Override
	public void receiveData(GameEvent event, Object... obj) {
		// TODO Auto-generated method stub
		
	}
	
	

}
