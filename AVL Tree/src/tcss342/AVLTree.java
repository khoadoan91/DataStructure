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
				result = true;
			} else
				result = add(n.left, item);
		else if (cmp < 0)
			// Add child to the right.
			if (n.right == null) {
				n.right = new Node<T>(item, 0, n);
				size++;
				result = true;
			} else
				result = add(n.right, item);
		else
			result = false;
		if(result)
			checkBalance(n);
		return result;
	}
	
	private void checkBalance(Node<T> n) {
		if (getNodeHeight(n.left) - getNodeHeight(n.right) == 2) { // left heavy
			if (getNodeHeight(n.left.left) >= getNodeHeight(n.left.right))
				n = rotateRight(n);
			else 
				n = leftRightRotation(n); 
		} else if (getNodeHeight(n.right) - getNodeHeight(n.left) == 2) {  // right heavy
			if (getNodeHeight(n.right.right) >= getNodeHeight(n.right.left))
				n = rotateLeft(n);
			else 
				n = rightLeftRotation(n);
		}
		if (n.parent == null)
			this.root = n;
		n.height = max(getNodeHeight(n.left), getNodeHeight(n.right)) + 1;			
	}
	
	/*		a
	 * 		 \				   b
	 * 		  b	   ---->	  / \
	 * 		   \			 a   c
	 * 			c
	 */
	/** Rotate binary tree node with left child */  
	private Node<T> rotateLeft(Node<T> n) {
		Node<T> temp = n.right;
		temp.parent = n.parent;
		n.right = temp.left;
		if (n.right != null)
			n.right.parent = n;
		temp.left = n;
		n.parent = temp;
		if (temp.parent != null) {
			if (temp.parent.right == n)
				temp.parent.right = temp;
			else if (temp.parent.left == n)
				temp.parent.left = temp;
		}
		temp.height = max(getNodeHeight(temp.left), getNodeHeight(temp.right)) + 1;
    	n.height = max(getNodeHeight(n.left), getNodeHeight(n.right)) + 1;
		return temp;
	}
	
	/* Rotate binary tree node with right child */
    private Node<T> rotateRight(Node<T> n) {
    	Node<T> temp = n.left;
    	temp.parent = n.parent;
    	n.left = temp.right;
    	if (n.left != null)
    		n.left.parent = n;
    	temp.right = n;
    	n.parent = temp;
    	if (temp.parent != null) {
    		if (temp.parent.right == n)
    			temp.parent.right = temp;
    		else if (temp.parent.left == n) 
    			temp.parent.left = temp;
    	}
    	temp.height = max(getNodeHeight(temp.left), getNodeHeight(temp.right)) + 1;
    	n.height = max(getNodeHeight(n.left), getNodeHeight(n.right)) + 1;
    	return temp;
    }
    
    private Node<T> rightLeftRotation(Node<T> n) {
    	n.right = rotateRight(n.right);
    	return rotateLeft(n);
    }
    
    private Node<T> leftRightRotation(Node<T> n) {
    	n.left = rotateLeft(n.left);
    	return rotateRight(n);
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
		if (n != root)
			checkBalance(n.parent);
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
	
	public void preOrder() {
		System.out.print("Preorder : ");
		preOrder(root);
		System.out.print("\n");
	}
	
	private void preOrder(Node<T> n) {
		System.out.print(n.item + " ");
		if (n.left != null)
			preOrder(n.left);
		if (n.right != null)
			preOrder(n.right);
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