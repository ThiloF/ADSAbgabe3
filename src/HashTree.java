/**
 * @author Thilo Falkenstein (877699), Felix König (577751)
 */
import java.util.ArrayList;

import de.medieninf.ads.ADSTool;

public class HashTree {

	private Node root;
	private int blocksize;
	private int fieldlength;
	private int noblocks;

	public HashTree(byte[] field, int blockSize) {
		this.noblocks = (int) Math.ceil((double) field.length / blockSize);
		byte[] field2 = new byte[noblocks * blockSize];
		System.arraycopy(field, 0, field2, 0, field.length);
		root = buildHashTree(getLeafNodes(field2, blockSize));
		this.blocksize = blockSize;
		this.fieldlength = field2.length;
	}

	public Node getRoot() {
		return root;
	}

	private abstract class Node {
		Hash hash;

		public Node(Hash hash) {
			this.hash = hash;
		}
	}

	private class InnerNode extends Node {

		Node left;
		Node right;

		public InnerNode(Node leftChild, Node rightChild) {
			super(leftChild.hash.chainedTo((rightChild == null) ? null : rightChild.hash));
			this.left = leftChild;
			this.right = rightChild;
		}
	}

	private class LeafNode extends Node {
		byte[] data; // array für die Datenblöcke

		public LeafNode(byte[] blocks) {
			super(new Hash(ADSTool.sha1(blocks)));
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

	// Diese Methode unterteilt das Bytefeld in Blöcke.
	// Diese Blöcke werden dann in Blattknoten verpackt.
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

	public void display() {
		System.out.print("bytes=" + fieldlength);
		System.out.print(", blocksize=" + blocksize);
		System.out.print(", noblocks=" + noblocks);
		System.out.println(", height=" + getLevels(noblocks));
		System.out.println(" i  Pfad    SHA-1 Hash");
		System.out.println();
		display(root, "");
	}

	private void display(Node node, String s) {
		String path = "";
		if (node instanceof LeafNode) {
			path = String.valueOf(Integer.parseInt(s, 2));
		}
		System.out.format("%3s ", path);
		System.out.format("%-6s  ", s);
		System.out.println(node.hash.toString());
		// Geht durch alle Knoten rekursiv
		if (node instanceof InnerNode) {
			InnerNode nodeInner = (InnerNode) node;
			if (nodeInner.left != null)
				display(nodeInner.left, s + 0);
			if (nodeInner.right != null)
				display(nodeInner.right, s + 1);
		}
	}

	public byte[] getTransportBlocks(String path) {

		InnerNode walk = (InnerNode) root;
		ArrayList<Hash> neighbours = new ArrayList<>();
		LeafNode leaf = null;

		// Iteriert von der Wurzel bis zum Blatt
		// Merkt sich alle Schwesterhashes auf dem Weg
		for (int i = 0; i < path.length(); i++) {
			Node needed = null;
			if (path.charAt(i) == '0') {
				Hash hash = walk.right == null ? new Hash(new byte[walk.left.hash.length()]) : walk.right.hash;
				neighbours.add(hash);
				needed = walk.left;
			} else {
				neighbours.add(walk.left.hash);
				needed = walk.right;
			}
			if (needed instanceof InnerNode) {
				walk = (InnerNode) needed;
			} else {
				leaf = (LeafNode) needed;
			}

		}

		byte[] stream = new byte[neighbours.size() * 20 + blocksize];
		int offset = 0;

		for (int i = neighbours.size() - 1; i >= 0; i--) {
			Hash h = neighbours.get(i);
			System.arraycopy(h.getBytes(), 0, stream, offset, h.length());
			offset += h.length();
		}

		if (leaf == null) {
			leaf = new LeafNode(new byte[blocksize]);
		}

		System.arraycopy(leaf.data, 0, stream, offset, leaf.data.length);

		return stream;

	}

	public int getNoBlocks() {
		return noblocks;
	}

}
