import java.util.ArrayList;

import de.medieninf.ads.ADSTool;

public class HashClient {

	private String url;
	private static Hash correctHash = new Hash(new byte[] { (byte) 0x9e, 0x06, (byte) 0xf5, 0x76, 0x53, (byte) 0xd2, 0x7e, (byte) 0xc4, 0x5e, 0x25, (byte) 0xbf, 0x03, (byte) 0xaf, (byte) 0x9d, (byte) 0x82, (byte) 0x86, (byte) 0xd7, 0x0a, (byte) 0xab, (byte) 0xa8 });

	public HashClient(String url) {
		this.url = url;
	}

	public void initConnection() {

		int noBlocks = ADSTool.toInt(ADSTool.getURLBytes(url + "noblocks"));
		System.out.println("Number of blocks: " + noBlocks);
		String[] path = getPaths(noBlocks);
		ArrayList<byte[]> datas = new ArrayList<>();
		int size = 0;

		int errors = 0;
		for (int i = 0; i < path.length; i++) {

			byte[] leaf = ADSTool.getURLBytes(url + path[i]); // Blätter
			int offset = getLevels(noBlocks) * 20;
			byte[] dataBlocks = new byte[leaf.length - offset]; // Datenblock
			System.arraycopy(leaf, offset, dataBlocks, 0, dataBlocks.length); // Datenblock
																				// kopieren

			ArrayList<Hash> neighbours = getNeighbours(leaf, getLevels(noBlocks));

			Hash hash = getRootHash(neighbours, new Hash(ADSTool.sha1(dataBlocks)), path[i]);

			System.out.println("Wurzel " + i + ": " + hash.toString());

			if (!hash.equals(correctHash)) {
				if (errors > 9) {
					System.out.println("Paket über 9 mal falsch angekommen. Abbruch...");
					return;
				}
				i--;
				errors++;
				System.out.println("Falsch. Versuche erneut...");
			} else {

				datas.add(dataBlocks);
				size += dataBlocks.length;
				errors = 0;

			}

		}

		write(datas, size);

	}

	// Wurzel berechnen
	private Hash getRootHash(ArrayList<Hash> neighbours, Hash dataHash, String path) {
		Hash hash = dataHash;

		for (int i = 0; i < neighbours.size(); i++) {
			char c = path.charAt(neighbours.size() - i - 1);
			if (c == '0') {
				hash = hash.chainedTo(neighbours.get(i));
			} else {
				hash = neighbours.get(i).chainedTo(hash);
			}

		}

		return hash;
	}

	// Ebenen berechnen
	private int getLevels(int numberOfNodes) {
		int i = 0;
		for (int j = 1; j < numberOfNodes; i++, j *= 2)
			;
		return i;
	}

	// Pfad berechenen
	private String[] getPaths(int noBlocks) {
		String[] a = new String[noBlocks];
		int depth = getLevels(noBlocks);
		for (int i = 0; i < noBlocks; i++) {
			String binary = "00000000000000000000000000000000" + Integer.toBinaryString(i);
			a[i] = binary.substring(binary.length() - depth);
		}
		return a;
	}

	// Nachbarknoten extrahieren
	public static ArrayList<Hash> getNeighbours(byte[] field, int levels) {

		ArrayList<Hash> array = new ArrayList<>();
		for (int i = 0; i < levels; i++) {
			byte[] help = new byte[20];
			System.arraycopy(field, i * 20, help, 0, 20);
			array.add(new Hash(help));
		}
		return array;
	}

	private void write(ArrayList<byte[]> datas, int size) {
		byte[] stream = new byte[size];

		int offset = 0;
		for (byte[] b : datas) {
			System.arraycopy(b, 0, stream, offset, b.length);
			offset += b.length;
		}

		ADSTool.saveByteArray("wasser3.png", ADSTool.depad(stream));

	}

}
