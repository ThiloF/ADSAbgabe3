/**
 * @author Thilo Falkenstein (877699), Felix König (577751)
 */
import de.medieninf.ads.ADSTool.ServeBytes;

public class aufg3 {

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
		ServeBytes byteServer = new ServeBytes(server);
		byteServer.serve();

	}

}
