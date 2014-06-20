import java.util.Arrays;
import java.util.Random;

public class moin {

	public static void main(String[] args) {

		byte[] data = new byte[17];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) i;
		}

		System.out.println("Generated data:");
		System.out.println(Arrays.toString(data));

		HashTreeVT ht = new HashTreeVT(data, 3);
		

		System.out.println("HashTreeData: ");
		ht.display();
		
		HashClient client = new HashClient("http://login3.mi.hs-rm.de:8097/");
		client.initConnection();

	}
	
	

	

}
