/*
 * Kyle Doan
 * August 20th, 2015
 * Homework 2
 * Huffman compression 
 */

package tcss342;

import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Huffman {

	public final static int ONE_BYTE = 8;

	public static Map<Character, Integer> getCharacterFrequencies(String path) {
		try {
			FileReader input = new FileReader(path);
			try {
				int n = input.read();
				Map<Character, Integer> result = new HashMap<>();
				while(n != -1) {
					char c = (char) n;
					if (result.containsKey(c)) 
						result.put(c, result.get(c) + 1);
					else
						result.put((char) n, 1);
					n = input.read();
				}
				input.close();
				return result;
			} catch (IOException e) {
				System.out.println(e.getMessage());
				return null;
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static Map<Character, String> getEncoding(Map<Character, Integer> frequencies) {
		Map<Character, String> result = new HashMap<>();
		HuffmanNode root = buildTree(frequencies);
		encode(root, result, "");
		return result;
	}

	private static HuffmanNode buildTree(Map<Character, Integer> freq) {
		if (freq == null) {
			throw new NullPointerException("");
		}
		Queue<HuffmanNode> queue = new PriorityQueue<>();
		for (char letter : freq.keySet()) {
			queue.add(new HuffmanNode(freq.get(letter), letter));
		}
      	while (queue.size() > 1) {
         	HuffmanNode temp1 = queue.remove();
         	HuffmanNode temp2 = queue.remove();
         	HuffmanNode sum = new HuffmanNode(temp1.weight + temp2.weight, temp1, temp2);
         	queue.add(sum);
      	}
      	return queue.peek();
	}

	private static void encode(HuffmanNode root, Map<Character, String> map, String code) {
		if (root.left == null && root.right == null) {
			map.put(root.letter, code);
      	} else {
         	encode(root.left, map, code + "0");
         	encode(root.right, map, code + "1");
      	}
	}
	
	public static double getCompressionRatio(Map<Character, Integer> frequencies, Map<Character, String> encoding) {
		double result;
		int origin = 0, compressed = 0;
		for (char letter : frequencies.keySet()) {
			origin += ONE_BYTE * frequencies.get(letter);	
		}
		for (char letter : encoding.keySet()) {
			compressed += encoding.get(letter).length() * frequencies.get(letter);
		}
		result = ((double) origin) / ((double) compressed);
		return result;
	}

	private static class HuffmanNode implements Comparable<HuffmanNode> {
		private int weight;
		private char letter;		
		private HuffmanNode left;
		private HuffmanNode right; 

		// Construct a HuffmanNode with a letter and its frequency
      	public HuffmanNode(int weight, char letter) {
         	this(weight, null, null);
         	this.letter = letter;
      	}
      
      	// Construct a HuffmanNode with a letter's frequency and left, right reference
      	public HuffmanNode(int weight, HuffmanNode left, HuffmanNode right) {
        	this.weight = weight;
         	this.left = left;
         	this.right = right;
      	}
      
	    // Compare HuffmanNode by the occurrences of the letter
	    public int compareTo(HuffmanNode other) {
	       	return weight - other.weight;
	    }
	}
}
