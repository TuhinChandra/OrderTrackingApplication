package com.order.track.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Util {

	public static ByteArrayOutputStream loadInputStreamAsByteArrayOutputStream(final InputStream pInputStream)
			throws IOException {

		final BufferedInputStream bufferedInputStream = new BufferedInputStream(pInputStream);
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int result = bufferedInputStream.read();
		while(result != -1) {
			byteArrayOutputStream.write((byte)result);
			result = bufferedInputStream.read();
		}
		return byteArrayOutputStream;
	}

}
