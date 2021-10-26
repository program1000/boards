package boards.scrabble;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import scrabble.util.InFileReader;
import scrabble.util.ParseInterface;

public class InFileReaderTest {

	@Test
	public void readTest() {
		InFileReader ir = new InFileReader();
		TestParser parser = new TestParser();
		assertEquals(3, ir.read("test/data", "test.txt", parser));
		List<String> lines = parser.getLines();
		assertEquals(3, lines.size());
		assertEquals("1", lines.get(0));
		assertEquals(" 2", lines.get(1));
		assertEquals("  3", lines.get(2));
	}
	
	class TestParser implements ParseInterface {
		
		private List<String> lines;

		@Override
		public long parse(Stream<String> input) {
			lines = input.collect(Collectors.toList());
			return lines.size();
		}
		
		protected List<String> getLines() {
			return lines;
		}
		
	}
}
