package tcss342;

public class BSTvsAVL {
	// Must be one less than a power of 2.
	public static final int TREE_SIZE = (1 << 12) - 1;  // 4095 items
	public static final int REPETITIONS = 200;

	public static void main(String[] args) {
		timeBalancedTrees(true);
		timeBalancedTrees(false);
		timeLightlySkewedTrees(false);
		timeHeavilySkewedTrees(false);
		timeDegenerateTrees(false);
	}

	public static void timeBalancedTrees(boolean silent) {
		if (!silent)
			System.out.println("Balanced tree comparison...");
		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		long start = System.nanoTime();
		for (int i = 0; i < REPETITIONS; i++) {
			// Add all items to the tree.
			for (int gap = TREE_SIZE + 1; gap > 1; gap /= 2)
				for (int item = gap / 2 - 1; item < TREE_SIZE; item += gap)
					bst.add(2 * item);
			// Search for every item that is in the tree.
			for (int item = 0; item < 2 * TREE_SIZE; item += 2)
				if (!bst.contains(item))
					System.out.println("There's a problem with the BST!\n");
			// Search for an equal number of items that aren't in the tree.
			for (int item = 1; item < 2 * TREE_SIZE; item += 2)
				if (bst.contains(item))
					System.out.println("There's a problem with the BST!\n");
			bst.clear();
		}
		long stop = System.nanoTime();
		if (!silent)
			System.out.println("BST time: " + (stop - start) / 1000000 + " ms");

		AVLTree<Integer> avl = new AVLTree<>();
		start = System.nanoTime();
		for (int i = 0; i < REPETITIONS; i++) {
			// Add all items to the tree.
			for (int gap = TREE_SIZE + 1; gap > 1; gap /= 2)
				for (int item = gap / 2 - 1; item < TREE_SIZE; item += gap)
					avl.add(2 * item);
			// Search for every item that is in the tree.
			for (int item = 0; item < 2 * TREE_SIZE; item += 2)
				if (!avl.contains(item))
					System.out.println("There's a problem with the AVL tree!\n");
			// Search for an equal number of items that aren't in the tree.
			for (int item = 1; item < 2 * TREE_SIZE; item += 2)
				if (avl.contains(item))
					System.out.println("There's a problem with the AVL tree!\n");
			avl.clear();
		}
		stop = System.nanoTime();
		if (!silent)
			System.out.println("AVL time: " + (stop - start) / 1000000 + " ms");
	}

	public static void timeLightlySkewedTrees(boolean silent) {
		if (!silent)
			System.out.println("Lightly skewed tree comparison...");

		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		long start = System.nanoTime();
		for (int i = 0; i < REPETITIONS; i++) {
			createLightlySkewed(bst, 0, TREE_SIZE);
			for (int item = 0; item < 2 * TREE_SIZE; item += 2)
				if (!bst.contains(item))
					System.out.println("There's a problem with the BST!\n");
			for (int item = 1; item < 2 * TREE_SIZE; item += 2)
				if (bst.contains(item))
					System.out.println("There's a problem with the BST!\n");
			bst.clear();
		}
		long stop = System.nanoTime();
		if (!silent)
			System.out.println("BST time: " + (stop - start) / 1000000 + " ms");

		AVLTree<Integer> avl = new AVLTree<>();
		start = System.nanoTime();
		for (int i = 0; i < REPETITIONS; i++) {
			createLightlySkewed(avl, 0, TREE_SIZE);
			for (int item = 0; item < 2 * TREE_SIZE; item += 2)
				if (!avl.contains(item))
					System.out.println("There's a problem with the AVL tree!\n");
			for (int item = 1; item < 2 * TREE_SIZE; item += 2)
				if (avl.contains(item))
					System.out.println("There's a problem with the AVL tree!\n");
			avl.clear();
		}
		stop = System.nanoTime();
		if (!silent)
			System.out.println("AVL time: " + (stop - start) / 1000000 + " ms");
	}
	private static void createLightlySkewed(BinarySearchTree<Integer> avl, int low, int high) {
		if (low < high) {
			double f = Math.pow(TREE_SIZE + 1, 1 / Math.sqrt(TREE_SIZE + 1));
			f = (f - 1) / f;
			int pivot = (int)(low + (high - low + 1) * f);
			avl.add(2 * pivot);
			createLightlySkewed(avl, low, pivot);
			createLightlySkewed(avl, pivot + 1, high);
		}
	}

	private static void createLightlySkewed(AVLTree<Integer> avl, int low, int high) {
		if (low < high) {
			double f = Math.pow(TREE_SIZE + 1, 1 / Math.sqrt(TREE_SIZE + 1));
			f = (f - 1) / f;
			int pivot = (int)(low + (high - low + 1) * f);
			avl.add(2 * pivot);
			createLightlySkewed(avl, low, pivot);
			createLightlySkewed(avl, pivot + 1, high);
		}
	}

	public static void timeHeavilySkewedTrees(boolean silent) {
		if (!silent)
			System.out.println("Heavily skewed tree comparison...");

		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		long start = System.nanoTime();
		for (int i = 0; i < REPETITIONS; i++) {
			for (int span = (TREE_SIZE + 1) / 2; span > 0; span /= 2)
				for (int item = span - 1; item < 2 * span - 1; item++)
					bst.add(2 * item);
			for (int item = 0; item < 2 * TREE_SIZE; item += 2)
				if (!bst.contains(item))
					System.out.println("There's a problem with the BST!\n");
			for (int item = 1; item < 2 * TREE_SIZE; item += 2)
				if (bst.contains(item))
					System.out.println("There's a problem with the BST!\n");
			bst.clear();
		}
		long stop = System.nanoTime();
		if (!silent)
			System.out.println("BST time: " + (stop - start) / 1000000 + " ms");

		AVLTree<Integer> avl = new AVLTree<>();
		start = System.nanoTime();
		for (int i = 0; i < REPETITIONS; i++) {
			for (int span = (TREE_SIZE + 1) / 2; span > 0; span /= 2)
				for (int item = span - 1; item < 2 * span - 1; item++)
					avl.add(2 * item);
			for (int item = 0; item < 2 * TREE_SIZE; item += 2)
				if (!avl.contains(item))
					System.out.println("There's a problem with the AVL tree!\n");
			for (int item = 1; item < 2 * TREE_SIZE; item += 2)
				if (avl.contains(item))
					System.out.println("There's a problem with the AVL tree!\n");
			avl.clear();
		}
		stop = System.nanoTime();
		if (!silent)
			System.out.println("AVL time: " + (stop - start) / 1000000 + " ms");
	}

	public static void timeDegenerateTrees(boolean silent) {
		if (!silent)
			System.out.println("Degenerate tree comparison...");
		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		long start = System.nanoTime();
		for (int i = 0; i < REPETITIONS; i++) {
			for (int item = 0; item < TREE_SIZE; item++)
				bst.add(2 * item);
			for (int item = 0; item < 2 * TREE_SIZE; item += 2)
				if (!bst.contains(item))
					System.out.println("There's a problem with the BST!\n");
			for (int item = 1; item < 2 * TREE_SIZE; item += 2)
				if (bst.contains(item))
					System.out.println("There's a problem with the BST!\n");
			bst.clear();
		}
		long stop = System.nanoTime();
		if (!silent)
			System.out.println("BST time: " + (stop - start) / 1000000 + " ms");

		AVLTree<Integer> avl = new AVLTree<>();
		start = System.nanoTime();
		for (int i = 0; i < REPETITIONS; i++) {
			for (int item = 0; item < TREE_SIZE; item++)
				avl.add(2 * item);
			for (int item = 0; item < 2 * TREE_SIZE; item += 2)
				if (!avl.contains(item))
					System.out.println("There's a problem with the AVL tree!\n");
			for (int item = 1; item < 2 * TREE_SIZE; item += 2)
				if (avl.contains(item))
					System.out.println("There's a problem with the AVL tree!\n");
			avl.clear();
		}
		stop = System.nanoTime();
		if (!silent)
			System.out.println("AVL time: " + (stop - start) / 1000000 + " ms");
	}
}