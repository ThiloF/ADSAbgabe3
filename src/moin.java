import java.util.Arrays;
import java.util.Random;

public class moin {

	public static void main(String[] args) {

		Random random = new Random();
		byte[] data = new byte[150];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) random.nextInt();
		}

		System.out.println("Generated data:");
		System.out.println(Arrays.toString(data));

		HashTree ht = new HashTree(data, 30);

		System.out.println("HashTreeData: ");
		ht.display();

	}

}
