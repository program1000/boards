# boards-backend

Implementation of a backend for Scrabble.

# Requirements
* SQLite JDBC

Add the sqlite jdbc jar file in a new lib directory. This can be obtained from the Maven repository, for example: <https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.30.1/sqlite-jdbc-3.30.1.jar>.
Add the native dll file (for Windows), which can be found in the jar file, also in the lib direcory. 
In the source code the location of the native file is set as sysem property. This will prevent auto extraction of the native file for each restart of the application.
See also dictionary/dictionary.java to set library path.

# Java techniques used:
 * Streams, operation on Streams: see util/BoardParser.java
 * JUnit 5 with DynamicTest: see test/boards/scrabble/WalkerTest.java

## Data model

### Board
The board is parsed from file data/scrabbleBoard.txt in util/BoardParser.java. The size of the board can be dynamic.
The Board consists of tiles with a certain value.

### Tile
A Tile has a value (score modifier): DoubleLetter, TrippleLetter, DoubleWord, TrippleWord or None.
A Tile may have a piece and can compute its score depending on the value of the letter on the piece and its score modifier.
Initialy the tiles on the board contains no pieces.

### Piece
A Piece consists of a Letter and a reference to four neighbor pieces (left, right, up and down).
### Letter
A Letter consist of a Character and a value.

### Bag
The bag contains all the letters.
The letters are parsed from data/letters.txt in util/LetterParser.java.
Most letters in the bag are not unique, this is why the file data/letters.txt contains an column amount.
The reading of the data files scrabbleBoard.txt and data/letters.txt is done by one InFileReader (util/InFileReader.java) and shared for both parsers: see game/Config.java

## Game loop
The flow of the game is controlled in game/Control.java.
At the start of the game the bag is shuffled and the current player receives its 7 letters in his 'hand'.
The players turn consists of a sequence of actions.

## Play a word
The input from the player is checked:
* the location on the board to the place the letters are within bounds of the board and placed consecutively on empty spots
* letters from the input matches letters in hand

Then the word is combined from existing letters on the board in board/BoardProcessor.java. 
The word is checked against the dictionary and combined with existing right-angled connected words on the board.
The hand of the player is renewed with letters from the bag.
The score of the collected words are computed.
The turn is repeated for the next player.

## Score
For every letter of the word that is played the score is computed. Only for new letters played on the board the tile score multiplier is applied, for existing letters only the letter value.
Because new words can be formed horizonal and vertical the score is calculated by walking along the pieces in two directions.

## Dictionary
When a player plays a word on the board the word is validated against the words the dictionary.
The valid word are parsed from data/words.txt in util/DictionaryParser.java and stored in a in-memory database in dictionary/DictionaryDb.java with Sqlite.

# TODO
Add end game condition: empty bag.
Save a game state to continue playing in another session.
Implement input from a network session.
Improve structure of score implementation.