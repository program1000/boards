package scrabble.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InFileReader {
	
	public long read( String path, String fileName, ParseInterface parser) {
		File file = new File(path+File.separator+fileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file)) ){
			return parser.parse(br.lines());
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}

}
