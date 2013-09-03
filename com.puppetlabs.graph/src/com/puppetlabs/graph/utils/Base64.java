/**
 * Copyright (c) 2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package com.puppetlabs.graph.utils;

/**
 * Simple Base64 encoder/decoder as specified by RFC2045 - using standard base64 encoding (letters, digits
 * and '+' and '/').
 * 
 */

public class Base64 {
	/**
	 * Lookup table from 6-bit positive integer to base64 alphabet as specified by RFC2045.
	 */
	private static final char intToBase64[] = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+',
			'/' };

	/**
	 * Lookup table from Unicode to base64 alphabet (6 bit integer) as specified by RFC2045.
	 * Negative positions represents characters not in the alphabet.
	 */
	private static final byte base64ToInt[] = {
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
			58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
			18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
			39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };

	public static byte[] base64ToByteArray(String s) {
		int length = s.length();
		int groupCount = length / 4;
		if(4 * groupCount != length)
			throw new IllegalArgumentException("Length must be multiple of 4");
		int missingBytesCount = 0;
		int fullGroupCount = groupCount;
		if(length != 0) {
			if(s.charAt(length - 1) == '=') {
				missingBytesCount++;
				fullGroupCount--;
			}
			if(s.charAt(length - 2) == '=')
				missingBytesCount++;
		}
		byte[] result = new byte[3 * groupCount - missingBytesCount];

		// Full groups
		int inIdx = 0, outIdx = 0;
		for(int i = 0; i < fullGroupCount; i++) {
			int c0 = base64toInt(s.charAt(inIdx++));
			int c1 = base64toInt(s.charAt(inIdx++));
			int c2 = base64toInt(s.charAt(inIdx++));
			int c3 = base64toInt(s.charAt(inIdx++));
			result[outIdx++] = (byte) ((c0 << 2) | (c1 >> 4));
			result[outIdx++] = (byte) ((c1 << 4) | (c2 >> 2));
			result[outIdx++] = (byte) ((c2 << 6) | c3);
		}

		// Partial
		if(missingBytesCount != 0) {
			int c0 = base64toInt(s.charAt(inIdx++));
			int c1 = base64toInt(s.charAt(inIdx++));
			result[outIdx++] = (byte) ((c0 << 2) | (c1 >> 4));

			if(missingBytesCount == 1) {
				int ch2 = base64toInt(s.charAt(inIdx++));
				result[outIdx++] = (byte) ((c1 << 4) | (ch2 >> 2));
			}
		}
		return result;
	}

	/**
	 * Translate given char to base64 6bit.
	 * 
	 * @throw IllegalArgumentException if c is 8 bit and not not in alphabet, or ArrayOutOfBoundsException if
	 *        c is more than 8 bit.
	 */
	private static int base64toInt(char c) {
		int result = base64ToInt[c];
		if(result < 0)
			throw new IllegalArgumentException("Illegal character " + c);
		return result;
	}

	/**
	 * Translates the specified byte array into a Base64 string.
	 */
	public static String byteArrayToBase64(byte[] a) {
		int length = a.length;
		int fullGroupCount = length / 3;
		int partialCount = length - 3 * fullGroupCount;
		int resultSize = 4 * ((length + 2) / 3);
		StringBuilder result = new StringBuilder(resultSize);

		// Full groups
		int inIdx = 0;
		for(int i = 0; i < fullGroupCount; i++) {
			int b0 = a[inIdx++] & 0xff;
			int b1 = a[inIdx++] & 0xff;
			int b2 = a[inIdx++] & 0xff;
			result.append(intToBase64[b0 >> 2]);
			result.append(intToBase64[(b0 << 4) & 0x3f | (b1 >> 4)]);
			result.append(intToBase64[(b1 << 2) & 0x3f | (b2 >> 6)]);
			result.append(intToBase64[b2 & 0x3f]);
		}

		// Partial
		if(partialCount != 0) {
			int b0 = a[inIdx++] & 0xff;
			result.append(intToBase64[b0 >> 2]);
			if(partialCount == 1) {
				result.append(intToBase64[(b0 << 4) & 0x3f]);
				result.append("==");
			}
			else {
				int byte1 = a[inIdx++] & 0xff;
				result.append(intToBase64[(b0 << 4) & 0x3f | (byte1 >> 4)]);
				result.append(intToBase64[(byte1 << 2) & 0x3f]);
				result.append('=');
			}
		}
		return result.toString();
	}

}
