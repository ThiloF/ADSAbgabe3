/**
 * @author Thilo Falkenstein (877699), Felix König (577751)
 */
import de.medieninf.ads.ADSTool;

public class aufg1 {

	public static void main(String[] args) {

		if (args.length < 2) {
			System.err.println("Verwendung: <blocksize> <Generierungsgröße>/<Eingabedatei>");
		}

		int blocksize = 0;
		try {
			blocksize = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.err.println("Blockgröße muss eine Zahl sein!");
			System.exit(-1);
		}

		boolean generate = false;

		String inputfile = args[1];
		byte[] input = null;
		try {
			input = ADSTool.readByteArray(inputfile);
		} catch (Exception e) {
			System.out.println("\nDatei konnte nicht gelesen werden. Versuche, als Generierungsgröße zu interpretieren...");
		}

		if (input == null) {
			generate = true;
		}

		if (generate) {
			int gensize = 0;
			try {
				gensize = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Generatorgröße muss eine Zahl sein!");
				System.exit(-1);
			}
			input = new byte[gensize];
			for (int i = 0; i < input.length; i++) {
				input[i] = (byte) i;
			}
		}

		HashTree ht = new HashTree(input, blocksize);

		System.out.println("HashTreeData: ");
		ht.display();

	}
}
