
public class HashTree {
	
	private abstract class Node {
		public byte[] hash; // 20 byte hash
	}
	
	private class NodeInner extends Node {
		public Node left, right; // Unterknoten
	}
	
	private class NodeLeaf extends Node {
		public byte[] data; // Daten
	}
	
	private Node root;
	
	public HashTree() {
		root = new NodeInner();
	}
	
}
