package opjj.hw5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CaesarCipherTest {

	CaesarCipher cipher = new CaesarCipher();
	
	@Test
	public void encodeDecodeSimpleMessage() {
		assertEquals(EncodedMessages.MESSAGE_1, cipher.decode(cipher.encode(EncodedMessages.MESSAGE_1, 3), 3));
		assertEquals(EncodedMessages.MESSAGE_2, cipher.decode(cipher.encode(EncodedMessages.MESSAGE_2, 15), 15));
	}
	
	@Test
	public void emptyMessage() {
		assertEquals("", cipher.decode(cipher.encode("", 3), 3));
	}
	
}
