
public class moin {

	public static void main(String[] args) {

		byte[] data = new byte[17];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) i;
		}

		HashTree ht = new HashTree(data, 3);
		byte[] stream = ht.getTransportBlocks("001");

		System.out.println("HashTreeData: ");
		ht.display();

		Hash test = new Hash(stream);
		System.out.println("001 = " + test.toString());

		System.out.println();

		HashClient client = new HashClient("http://localhost:8097/");
		client.initConnection();

	}

}
