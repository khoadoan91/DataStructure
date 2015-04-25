package tcss342;

import java.util.Map;

public class Main {
	public static void main(String[] args) {
		Map<Character, Integer> frequencies = Huffman.getCharacterFrequencies("pg1524.txt");
		for (char letter : frequencies.keySet()) {
			System.out.println(letter + " = " + frequencies.get(letter));
		}
		Map<Character, String> encoding = Huffman.getEncoding(frequencies);
		for (char letter : encoding.keySet()) {
			System.out.println(letter + " = " + encoding.get(letter));
		}	
		double cr = Huffman.getCompressionRatio(frequencies, encoding);
		System.out.println("Compression ratio: " + cr);
	}
}
