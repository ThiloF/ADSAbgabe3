import java.util.Arrays;

import de.medieninf.ads.ADSTool;

public class HashClient {

	private String url;

	public HashClient(String url) {
		this.url = url;
	}

	public void initConnection() {

		int noBlocks = ADSTool.toInt(ADSTool.getURLBytes(url + "noblocks"));
		System.out.println(Arrays.toString(getPaths(noBlocks)));
	}

	private int getLevels(int numberOfNodes) {
		int i = 0;
		for (int j = 1; j < numberOfNodes; i++, j *= 2)
			;
		return i;
	}

	private String[] getPaths(int noBlocks) {
		String[] a = new String[noBlocks];
		int depth = getLevels(noBlocks);
		for (int i = 0; i < noBlocks; i++) {
			String binary = "00000000000000000000000000000000" + Integer.toBinaryString(i);
			a[i] = binary.substring(binary.length() - depth);
		}
		return a;
	}

}
