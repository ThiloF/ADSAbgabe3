import de.medieninf.ads.ADSTool;

public class Hash {

	private byte[] bytes;

	/**
	 * Der Konstruktor. Er hasht nicht. Er nimmt fertig gehashte bytes
	 * @param bytes
	 */
	public Hash(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int length() {
		return bytes.length;
	}
	
	public Hash chainedTo(Hash hash) {
		if (hash == null) {
			byte[] zeroes = new byte[length()];
			hash = new Hash(zeroes);
		}
		
		byte[] newBytes = new byte[length() + hash.length()];
		
		System.arraycopy(bytes, 0, newBytes, 0, length());
		System.arraycopy(hash.getBytes(), 0, newBytes, length(), hash.length());
		
		return new Hash(ADSTool.sha1(newBytes));
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(ADSTool.byteArrayToHexString(new byte[] { b }) + " ");
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Hash)) return false;
		Hash h = (Hash) o;
		for (int i = 0; i < bytes.length; i++) {
			if (h.getBytes()[i] != bytes[i]) return false;
		}
		return true;
	}

}
