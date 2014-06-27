/**
 * @author Thilo Falkenstein (877699), Felix König (577751)
 */
public class aufg2 {

	public static void main(String[] args) {

		if (args.length < 22) {
			System.err.println("Verwendung: <Quell-URL> <Ausgabedatei> <Roothash (Byte 1)> <Roothash (Byte 2)> ...  <Roothash (Byte 20)>");
		}

		String src = args[0];
		String dest = args[1];
		String[] hashStr = new String[20];
		byte[] hashBytes = new byte[20];
		System.arraycopy(args, 2, hashStr, 0, 20);

		for (int i = 0; i < hashStr.length; i++) {
			hashBytes[i] = (byte) Integer.parseInt(hashStr[i], 16);
		}

		Hash hash = new Hash(hashBytes);

		HashClient client = new HashClient(src, dest, hash);
		client.query();

	}

}
