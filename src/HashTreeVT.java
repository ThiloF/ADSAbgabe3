import java.lang.reflect.Array;
import java.util.ArrayList;
import de.medieninf.ads.*;

import de.medieninf.ads.*;

public class HashTreeVT {

	private Node root;

	public HashTreeVT(byte[] field, int blockSize) {
		root = buildHashTree(field, blockSize);
	}



	private abstract class Node {
		byte[] hash;

		public Node(byte[] hash) {
			this.hash = hash;
		}

		public byte[] getHash() {
			return hash;
		}

	}

	private class InnerNode extends Node {

		Node left;
		Node right;

		public InnerNode(Node leftChild, Node rigthChild) {
			super(ADSTool.sha1(chainHashs(leftChild.getHash(), rigthChild.getHash())));
			this.left = leftChild;
			this.right = rigthChild;
		}

		public Node getLeft() {
			return left;
		}

		public void setLeft(Node left) {
			this.left = left;
		}

		public Node getRight() {
			return right;
		}

		public void setRight(Node right) {
			this.right = right;
		}

	}

	private class LeafNode extends Node {
		byte[] blocks; // array für die Datenblöcke

		public LeafNode(byte[] blocks) {
			super(ADSTool.sha1(blocks));
			this.blocks = blocks;
		}
	}

	public void getPath(int nodeNumer) {
		ArrayList<String> al = new ArrayList<String>();

		buildPath("", al, getLevels(nodeNumer));

		System.out.println(al.size());
		for (String s : al) {
			System.out.println(s);
		}

	}

	private void buildPath(String s, ArrayList<String> path, int lengt) {

		if (s.length() == lengt) {
			path.add(s);
			return;

		}

		path.add(s);

		StringBuilder addZero = new StringBuilder(s);
		StringBuilder addOne = new StringBuilder(s);
		addZero.append('0');
		addOne.append('1');
		buildPath(addZero.toString(), path, lengt);
		buildPath(addOne.toString(), path, lengt);

	}

	// gibt die Höhe des Baumes zurück

	private int getLevels(int numberOfNodes) {
		return (int) (Math.log(numberOfNodes) / Math.log(2));
	}

	//Baut den Baum auf
	
	private Node buildHashTree(byte[] field, int blockSize) {
		
		ArrayList<Node> nodes = getLeafNodes(field, blockSize);

		return null;
	}

	/*
	 * Diese Methode unterteilt das Bytefeld in Blöcke.Diese Blöcke werden dann
	 * in Blattkonten verpackt.Sollte dabei eine ungerade anzahlahl an
	 * Bytblöckenkommen, fügt er einen leeres Blatt hinzu
	 */

	private ArrayList<Node> getLeafNodes(byte[] field, int blockSize) {
		int length = field.length / blockSize;
		ArrayList<Node> leafNodes = new ArrayList<>();

		int position = 0;

		for (int i = 0; i < length; i++) {
			byte[] help = new byte[blockSize];
			System.arraycopy(field, position, help, 0, blockSize);
			leafNodes.add(new LeafNode(help));
			position += blockSize;
		}

		if ((leafNodes.size() % 2) != 0) {
			leafNodes.add(new LeafNode(new byte[blockSize]));
		}

		return leafNodes;
	}

	private byte[] chainHashs(byte[] hash1, byte[] hash2) {
		byte[] hash = new byte[hash1.length + hash2.length];

		System.arraycopy(hash1, 0, hash, 0, hash.length);
		System.arraycopy(hash2, 0, hash, hash1.length - 1, hash2.length);

		return hash;

	}

}
