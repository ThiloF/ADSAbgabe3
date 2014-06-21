import java.util.ArrayList;
import java.util.Arrays;

import de.medieninf.ads.ADSTool;

public class HashClient {

	private String url;

	public HashClient(String url) {
		this.url = url;
	}

	public void initConnection() {

		int noBlocks = ADSTool.toInt(ADSTool.getURLBytes(url + "noblocks"));
		String[] path = getPaths(noBlocks);
		System.out.println(Arrays.toString(getPaths(noBlocks)));

		for(int i = 0; i < path.length; i++){
		
		byte[] leaf = ADSTool.getURLBytes(url + path[9]); // Blätter
		byte[] dataBlocks = new byte[leaf.length - (getLevels(noBlocks) * 20)]; // Datenblock
		System.arraycopy(leaf, getLevels(noBlocks) * 20, dataBlocks, 0, dataBlocks.length); // Datenblock
																							// kopieren

		
		ArrayList<byte[]> neighbours = getNeighbours(leaf, getLevels(noBlocks));
		

		

		byte[] hash = getRoot(neighbours, ADSTool.sha1(dataBlocks), path[i]);

		System.out.println("Wurzel: ");
		for (byte b : hash) {
			System.out.print(ADSTool.byteArrayToHexString(new byte[] { b }) + " ");
		}
		
		}

	}
	
	//Wurzel berechnen
	
	private byte[] getRoot(ArrayList<byte[] > neighbours, byte[] dataBlocks, String path){
		byte[] hash = dataBlocks;
		
		
		
		for (int i = 0; i < neighbours.size(); i++) {
			char c = path.charAt(i);
			if(c == 0){
			hash = ADSTool.sha1(chainHashs(hash, neighbours.get(i)));
			}else{
			hash = ADSTool.sha1(chainHashs(neighbours.get(i), hash));	
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

	//Pfad berechenen
	private String[] getPaths(int noBlocks) {
		String[] a = new String[noBlocks];
		int depth = getLevels(noBlocks);
		for (int i = 0; i < noBlocks; i++) {
			String binary = "00000000000000000000000000000000" + Integer.toBinaryString(i);
			a[i] = binary.substring(binary.length() - depth);
		}
		return a;
	}

	
	//Nachbarknoten extrahieren  
	public static ArrayList<byte[]> getNeighbours(byte[] field, int levels) {

		ArrayList<byte[]> array = new ArrayList<>();
		int start = 0;
		for (int i = 0; i < levels; i++) {
			byte[] help = new byte[20];
			System.arraycopy(field, start, help, 0, 20);
			start += 20;
			array.add(help);
		}
		return array;
	}
	
	//Hashes verketten
	
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

}
