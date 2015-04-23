package tcss342;

import java.util.Map;

public class Main {
	public static void main(String[] args) {
		Map<Character, Integer> frequencies = Huffman.getCharacterFrequencies("pg1524.txt");
		Map<Character, String> encoding = Huffman.getEncoding(frequencies);
		double cr = Huffman.getCompressionRatio(frequencies, encoding);
		
		System.out.println("Compression ratio: " + cr);
	}
}
