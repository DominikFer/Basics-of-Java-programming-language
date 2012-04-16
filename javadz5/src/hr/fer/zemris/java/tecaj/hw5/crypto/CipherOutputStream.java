package hr.fer.zemris.java.tecaj.hw5.crypto;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class CipherOutputStream extends OutputStream {

	private Cipher cipher;
	private OutputStream stream;
	
	public CipherOutputStream(OutputStream stream, Cipher cipher) {
		this.cipher = cipher;
		this.stream = stream;
	}
	
	@Override
	public void write(byte[] bytes, int off, int len) throws IOException {
		try {
			byte[] outputBuffer = new byte[4096];
			int outputByteCount = cipher.update(bytes, off, len, outputBuffer);
			stream.write(outputBuffer, 0, outputByteCount);
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(int b) throws IOException {
		try {
			byte[] outputBuffer = new byte[4096];
			int outputByteCount = cipher.update(ByteBuffer.allocate(4).putInt(b).array(), 0, 1, outputBuffer);
			stream.write(outputBuffer, 0, outputByteCount);
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
	}

}
