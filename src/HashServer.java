/**
 * @author Thilo Falkenstein (877699), Felix König (577751)
 */
import java.nio.ByteBuffer;
import java.util.Random;

import de.medieninf.ads.ADSTool;
import de.medieninf.ads.ADSTool.OfferBytes;

public class HashServer implements OfferBytes {

	private HashTree ht;
	private Random random = new Random();
	private int errorquote, blocksize;

	public HashServer(String file, int blocksize, int errorquote) {

		byte[] bytes = ADSTool.readByteArray(file);

		bytes = ADSTool.pad(bytes, blocksize);
		this.ht = new HashTree(bytes, blocksize);
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
