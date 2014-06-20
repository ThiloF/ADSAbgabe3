import java.util.ArrayList;

import de.medieninf.ads.ADSTool;

public class HashTreeVT {

	private Node root;
	private int blocksize;
	private int fieldlength;
	private int noblocks;

	public HashTreeVT(byte[] field, int blockSize) {
		this.noblocks = (int) Math.ceil((double) field.length / blockSize);
		byte[] field2 = new byte[noblocks * blockSize];
		System.arraycopy(field, 0, field2, 0, field.length);
		root = buildHashTree(getLeafNodes(field2, blockSize));
		this.blocksize = blockSize;
		this.fieldlength = field.length;
	}

	public Node getRoot() {
		return root;
	}

	private abstract class Node {
		byte[] hash;

		public Node(byte[] hash) {
			this.hash = hash;
		}
	}

	private class InnerNode extends Node {

		Node left;
		Node right;

		public InnerNode(Node leftChild, Node rightChild) {
			super(ADSTool.sha1(chainHashs(leftChild.hash, (rightChild == null) ? null : rightChild.hash)));
			this.left = leftChild;
			this.right = rightChild;
		}
	}

	private class LeafNode extends Node {
		byte[] data; // array für die Datenblöcke

		public LeafNode(byte[] blocks) {
			super(ADSTool.sha1(blocks));
			this.data = blocks;
		}
	}

	// gibt die Höhe des Baumes zurück

	private int getLevels(int numberOfNodes) {
		int i = 0;
		for (int j = 1; j < numberOfNodes; i++, j *= 2)
			;
		return i;
	}

	// Baut den Baum auf

	private Node buildHashTree(ArrayList<Node> nodes) {

		if (nodes.size() == 1) {
			return nodes.get(0);
		}

		ArrayList<Node> parentNodes = new ArrayList<>();

		if ((nodes.size() % 2) != 0) {
			nodes.add(null);
		}

		for (int i = 1; i < nodes.size(); i += 2) {

			parentNodes.add(new InnerNode(nodes.get(i - 1), nodes.get(i)));

		}

		return buildHashTree(parentNodes);
	}

	/*
	 * Diese Methode unterteilt das Bytefeld in Blöcke.Diese Blöcke werden dann
	 * in Blattkonten verpackt.Sollte dabei eine ungerade anzahlahl an
	 * Bytblöckenkommen, fügt er einen leeres Blatt hinzu
	 */

	private ArrayList<Node> getLeafNodes(byte[] field, int blockSize) {
		ArrayList<Node> leafNodes = new ArrayList<>();

		int position = 0;

		for (int i = 0; i < noblocks; i++) {
			byte[] help = new byte[blockSize];
			System.arraycopy(field, position, help, 0, blockSize);
			leafNodes.add(new LeafNode(help));
			position += blockSize;
		}

		return leafNodes;
	}

	private byte[] chainHashs(byte[] hash1, byte[] hash2) {

		if (hash2 == null) {
			byte[] zeros = new byte[hash1.length];
			hash2 = zeros;
		}

		byte[] hash = new byte[hash1.length + hash2.length];

		System.arraycopy(hash1, 0, hash, 0, hash1.length);
		System.arraycopy(hash2, 0, hash, hash1.length, hash2.length);

		return hash;

	}

	public void display() {
		System.out.print("bytes=" + fieldlength);
		System.out.print(", blocksize=" + blocksize);
		System.out.print(", noblocks=" + noblocks);
		System.out.println(", height=" + getLevels(noblocks));
		System.out.println(" i Pfad SHA-1 Hash");
		System.out.println();
		display(root, "");
	}

	private void display(Node node, String s) {
		String path = "";
		if (node instanceof LeafNode) {
			path = String.valueOf(Integer.parseInt(s, 2));
		}
		System.out.format("%2s ", path);
		System.out.format("%-3s  ", s);
		// System.out.println(ADSTool.byteArrayToHexString(node.hash));
		for (byte b : node.hash) {
			System.out.print(ADSTool.byteArrayToHexString(new byte[] { b }) + " ");
		}
		System.out.println();
		if (node instanceof InnerNode) {

			InnerNode nodeInner = (InnerNode) node;
			if (nodeInner.left != null)
				display(nodeInner.left, s + 0);
			if (nodeInner.right != null)
				display(nodeInner.right, s + 1);
		}
	}

}
