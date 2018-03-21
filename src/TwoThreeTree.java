public class TwoThreeTree {

	private Node root;

	public TwoThreeTree() {
		root = null;
	}

	//Trivial case where tree is empty, then calls the recursive insert
	public boolean insert(int x) {
		
		if(root == null) {
			Node newNode = new Node(x);
			root = newNode;
			root.keys[0] = x;
			return true;
		}

		else {
			return root.insertR(x);
		}
	}

	//Transposes down the tree to find the correct node, if no node is found, it returns the last node that was called.
	public String search(int x) {
		Node n = root.searchFind(x);
		if(n.keys[1] != null) {
			return n.keys[0] + " " + n.keys[1];
		}
		return n.keys[0].toString();
	}

	class Node
	{
		public Integer[] keys;
		public int keySize;
		public Node[] children;

		public Node(int n) {
			keys = new Integer[3];
			children = new Node[4];
			keys[0] = n;
			keySize = 1;
		}

		public Node(int n, Node l, Node r) {
			keys = new Integer[3];
			children = new Node[4];
			keys[0] = n;
			children[0] = l;
			children[1] = r;
			keySize = 1;
		}

		//Recursive insert
		public boolean insertR(int x) {

			//Does nothing if duplicate
			if(contains(x)) {
				return false;
			}
			
			//If node is a leaf, add the element. If it is full after adding the element, split.
			else if(isLeaf()) {
				add(x);
				
				if(is3Node()) {
					split();
					return true;
				}
				return false;
			}
			
			//Recursively finds the correct location for the element.
			else {
				int i = 0;
				while(keys[i] != null && keys[i] < x) {
					i++;
				}
				boolean childSplit = children[i].insertR(x);
				if(childSplit) {
					//Promotes split key into parent node
					add(children[i].keys[0]);
					
					//If parent node is now full
					if(is3Node()) {
						int s = 3;
						while(s - i >= 2) {
							children[s] = children[s - 1];
							s--;
						}
						children[s] = children[i].children[1];
						children[s - 1] = children[i].children[0];
						split();
						return true;
					}
					else {
						if(i == 1) {
							children[2] = children[i].children[1];
							children[1] = children[i].children[0];
						}
						else {
							children[2] = children[1];
							children[1] = children[i].children[1];
							children[0] = children[i].children[0];
						}
						return false;
					}
				}
			}
			return false;
		}


		//Found correct node and adds element into the node
		private void add(int x) {

			//When node has one key
			if(keySize == 1) {

				//If left key is higher, set it to the right key
				if(keys[0] > x) {
					keys[1] = keys [0];
					keys[0] = x;
				}

				//Otherwise, set element to right key
				else {
					keys[1] = x;
				}
				keySize++;
			}

			//Inserts element and sorts keys
			else {
				int i = 2;
				while(i > 0 && x < keys[i - 1]) {
					keys[i] = keys[i - 1];
					i--;
				}
				keys[i] = x;
				keySize++;
			}
		}

		//Recursively searches for the correct node.
		public Node searchFind(int x) {
			if(contains(x)) {
				return this;
			}
			else if(isLeaf()){
				return this;
			}
			else {
				if(x < keys[0]) {
					return children[0].searchFind(x);
				}
				else if(keySize == 2 && x < keys[1]) {
					return children[1].searchFind(x);
				}
				else {
					if(keySize == 2) {
						return children[2].searchFind(x);
					}
					return children[1].searchFind(x);
				}
				
			}
				
		}

		//Returns new split parent node
		private Node split() {
			Node l = new Node(keys[0], children[0], children[1]);
			Node r = new Node(keys[2], children[2], children[3]);
			keys[0] = keys[1];
			keys[1] = null;
			keys[2] = null;
			children[0] = l;
			children[1] = r;
			children[2] = null;
			children[3] = null;
			keySize = 1;
			return this;
		}

		private boolean contains(int x) {
			int i = 0;
			while(i < 3 && keys[i] != null) {
				if(keys[i] == x) {
					return true;	
				}
				i++;
			}
			return false;
		}

		private boolean is3Node() {
			if(keys[2] != null) {
				return true;
			}
			return false;
		}
		
		private boolean isLeaf() {
			if(children[0] == null && children[1] == null && children[2] == null) {
				return true;
			}
			return false;
		}	

	}

}
