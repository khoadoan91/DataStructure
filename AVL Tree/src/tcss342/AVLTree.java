/*
 * Kyle Doan
 * August 25th, 2015
 * Homework 3
 * AVL Tree - Self-balance Tree 
 */
package tcss342;

public class AVLTree<T extends Comparable<T>> {
	private static class Node<T> {
		public T item;
		public int height;
		public Node<T> parent;
		public Node<T> left;
		public Node<T> right;
		
		public Node(T item) {
			this(item, 0, null);
		}
		
		public Node(T item, int height, Node<T> parent) {
			this.item = item;
			this.height = height;
			this.parent = parent;
		}

		// find next bigger number of current node.
		public Node<T> getSuccessor() {
			if (right != null) {
				Node<T> successor = right;
				while (successor.left != null)
					successor = successor.left;
				return successor;
			} else {
				Node<T> successor = this;
				while (successor.parent != null && successor.parent.right == successor)
					successor = successor.parent;
				successor = successor.parent;
				return successor;
			}
		}
	}

	private int size;
	private Node<T> root;
	
	private int getNodeHeight (final Node<T> node) {
		if (node == null) 
			return -1;
		return node.height;
	}
	
	private int max(int a, int b) {
		return a > b ? a : b;
	}
	
	public boolean add(T item) {
		if (item == null)
			throw new IllegalArgumentException();
		
		if (root == null) {
			root = new Node<T>(item);
			size++;
			return true;
		} else
			return add(root, item);
	}
	
	private boolean add(Node<T> n, T item) {
		boolean result = false;
		int cmp = n.item.compareTo(item);
		if (cmp > 0)
			// Add child to the left.
			if (n.left == null) {
				n.left = new Node<T>(item, 0, n);
				size++;
				// the if condition below is wrong in zigzag scenario
				if (getNodeHeight(n) - getNodeHeight(n.parent.right) == 2) 
					if (item.compareTo(n.parent.item) < 0)
						rotateRight(n.parent);
					else // n.left.item > n.parent.item
						leftRightRotation(n.parent);
				result = true;
			} else
				result = add(n.left, item);
		else if (cmp < 0)
			// Add child to the right.
			if (n.right == null) {
				n.right = new Node<T>(item, 0, n);
				size++;
				// the if condition below is wrong in zigzag scenario
				if (getNodeHeight(n) - getNodeHeight(n.parent.left) == 2)
					if (item.compareTo(n.parent.item) > 0)
						rotateLeft(n.parent);
					else	
						rightLeftRotation(n.parent);
				result = true;
			} else
				result = add(n.right, item);
		else
			result = false;
		if (result) 
			n.parent.height = max(getNodeHeight(n.parent.left), getNodeHeight(n.parent.right)) + 1;
		
		return result;
	}
	
	/*		a
	 * 		 \				   b
	 * 		  b	   ---->	  / \
	 * 		   \			 a   c
	 * 			c
	 */
	/** Rotate binary tree node with left child */  
	private void rotateLeft(Node<T> n) {
		n.right.parent = n.parent;
		n.parent = n.right;
		n.right = n.parent.left;
		if (n.right != null)
			n.right.parent = n;
		n.parent.left = n;
	}
	
	/* Rotate binary tree node with right child */
    private void rotateRight(Node<T> n) {
        n.left.parent = n.parent;
        n.parent = n.left;
        n.left = n.parent.right;
        if (n.left != null)
        	n.left.parent = n;
        n.parent.right = n;
    }
    
    private void rightLeftRotation(Node<T> n) {
    	rotateRight(n.left);
    	rotateLeft(n);
    }
    
    private void leftRightRotation(Node<T> n) {
    	rotateLeft(n.right);
    	rotateRight(n);
    }

	public boolean remove(T item) {
		if (item == null)
			throw new IllegalArgumentException();

		return remove(root, item);
	}
	
	private boolean remove(Node<T> n, T item) {
		int cmp = n.item.compareTo(item);
		if (cmp > 0)
			// Search to the left.
			if (n.left == null)
				return false;
			else
				return remove(n.left, item);
		else if (cmp < 0)
			// Search to the right.
			if (n.right == null)
				return false;
			else
				return remove(n.right, item);
		else {
			remove(n);
			size--;
			return true;
		}
	}

	private void remove(Node<T> n) {
		if (n.right == null) {
			// No right subtree, move left subtree (if any) up to replace n.
			if (n == root) {
				root = n.left;
				if (root != null)
					root.parent = null;
			} else {
				if (n.parent.left == n)
					n.parent.left = n.left;
				else
					n.parent.right = n.left;
				if (n.left != null)
					n.left.parent = n.parent;
			}
		} else if (n.left == null) {
			// No left subtree, move right subtree up to replace n.
			if (n == root) {
				root = n.right;
				root.parent = null;
			} else {
				if (n.parent.left == n)
					n.parent.left = n.right;
				else
					n.parent.right = n.right;
				n.right.parent = n.parent;
			}
		} else {
			// Both children are present, replace n with its successor and
			// remove the successor.
			Node<T> successor = n.getSuccessor();
			n.item = successor.item;
			remove(successor);
		}
	}

	public boolean contains(T item) {
		if (item == null)
			throw new IllegalArgumentException();
		
		if (root == null)
			return false;
		else
			return contains(root, item);
	}
	
	private boolean contains(Node<T> n, T item) {
		int cmp = n.item.compareTo(item);
		if (cmp > 0)
			// Search to the left
			if (n.left == null)
				return false;
			else
				return contains(n.left, item);
		else if (cmp < 0)
			// Search to the right
			if (n.right == null)
				return false;
			else
				return contains(n.right, item);
		else
			return true;
	}

	public int size() {
		return size;
	}

	public void clear() {
		size = 0;
		root = null;
	}

	public void hasValidStructure() {
		if (root != null) {
			assert root.parent == null;
			hasValidStructure(root);
		}
	}

	private void hasValidStructure(Node<T> n) {
		if (n.left != null) {
			assert n.left.item.compareTo(n.item) < 0;
			hasValidStructure(n.left);
		}
		if (n.right != null) {
			assert n.right.item.compareTo(n.item) > 0;
			hasValidStructure(n.right);
		}
	}
}