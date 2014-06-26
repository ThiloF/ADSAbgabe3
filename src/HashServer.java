import java.util.Random;

import de.medieninf.ads.ADSTool;
import de.medieninf.ads.ADSTool.OfferBytes;

public class HashServer implements OfferBytes {

	private HashTree ht;
	private Random random = new Random();
	private int errorquote, blocksize;

	public static void main(String[] args) {

		// TODO Einlesen: Dateiname, Blöcklänge, Fehlerrate in Promille
		// TODO Instanz von HashServer erstellen

	}

	public HashServer(String file, int blocksize, int errorquote) {

		byte[] bytes = ADSTool.readByteArray(file);

		this.ht = new HashTree(bytes, blocksize);
		this.errorquote = errorquote;
		this.blocksize = blocksize;

	}

	@Override
	public byte[] get(String path) {
		if (random.nextInt(1000) < errorquote) {
			byte[] randoms = new byte[blocksize + 20 * path.length()];
			random.nextBytes(randoms);
			return randoms;
		}
		return ht.getTransportBlocks(path);
	}

}
