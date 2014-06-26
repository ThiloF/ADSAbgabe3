import java.nio.ByteBuffer;
import java.util.Random;

import de.medieninf.ads.ADSTool;
import de.medieninf.ads.ADSTool.OfferBytes;
import de.medieninf.ads.ADSTool.ServeBytes;

public class HashServer implements OfferBytes {

	private HashTree ht;
	private Random random = new Random();
	private int errorquote, blocksize;

	public static void main(String[] args) {

		if (args.length != 3) {
			System.err.println("Verwendung: <Eingabedatei> <Blocklänge> <Fehlerquote in Promille>");
			System.exit(-1);
		}

		String filename = args[0];
		int blocksize = 0, errorquote = 0;
		try {
			blocksize = Integer.parseInt(args[1]);
			errorquote = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.err.println("Blocklänge und Fehlerpromille müssen Ganzzahlwerte enthalten!");
			System.exit(-1);
		}

		System.out.println("Starting server with file " + filename + ", blocksize " + blocksize + " and errorquote " + errorquote + "/1000");
		HashServer server = new HashServer(filename, blocksize, errorquote);
		ServeBytes thingy = new ServeBytes(server);
		thingy.serve();

	}

	public HashServer(String file, int blocksize, int errorquote) {

		byte[] bytes = ADSTool.pad(ADSTool.readByteArray(file), blocksize);

		this.ht = new HashTree(bytes, blocksize);
		// this.ht.display();
		this.errorquote = errorquote;
		this.blocksize = blocksize;

	}

	@Override
	public byte[] get(String path) {
		if (path.equals("noblocks")) {
			return ByteBuffer.allocate(4).putInt(ht.getNoBlocks()).array();
		} else {
			if (random.nextInt(1000) < errorquote) {
				byte[] randoms = new byte[blocksize + 20 * path.length()];
				random.nextBytes(randoms);
				return randoms;
			}
			return ht.getTransportBlocks(path);
		}
	}

}
