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
package org.cloudsmith.graph.utils;

/**
 * @author henrik
 *
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * A circular buffer/queue of bytes.
 */
public final class CircularByteQueue implements Serializable, Cloneable {

	private static final long serialVersionUID = 5001088066123329954L;

	private byte[] buffer;

	private int contentSize = 0;

	/**
	 * The head position (index to remove/return first from the buffer).
	 */
	private int head = 0;

	/**
	 * The tail position (where the next element will be added to the buffer)
	 */
	private int tail = 0;

	/**
	 * The initial capacity of the buffer.
	 * <b>Must be a power of 2.</b>
	 */
	private static final int INITIAL_CAPACITY = 8;

	/**
	 * Creates a buffer of size 16.
	 */
	public CircularByteQueue() {
		buffer = new byte[16];
	}

	/**
	 * Creates an empty buffer capable of holding the given number of bytes.
	 * 
	 * @param expectedSize
	 *            minimum expected capacity
	 */
	public CircularByteQueue(int expectedSize) {
		int initialCapacity = INITIAL_CAPACITY;
		if(expectedSize > initialCapacity) {
			initialCapacity = Integer.highestOneBit(expectedSize);
			// if expected is not ^2, use next ^2
			if(initialCapacity != expectedSize)
				initialCapacity <<= 1;

			if(initialCapacity < 0) {
				throw new IllegalArgumentException("expected size too big");
			}
		}
		buffer = new byte[initialCapacity];
	}

	/**
	 * Puts given byte in the buffer.
	 */
	public void add(byte b) {
		if(head == tail && contentSize != 0)
			doubleCapacity();

		buffer[tail] = b;
		tail = (tail + 1) & (buffer.length - 1);
		contentSize++;
	}

	public void add(byte[] content) {
		add(content, 0, content.length);
	}

	/**
	 * Puts given bytes in the buffer.
	 * 
	 * @param content
	 */
	public void add(byte[] content, int offset, int length) {
		ensureCapacity(size() + length);

		int capacity = buffer.length;
		int firstPart = capacity - tail;
		if(firstPart < length) {
			System.arraycopy(content, offset, buffer, tail, firstPart);
			System.arraycopy(content, offset + firstPart, buffer, 0, length - firstPart);
			tail = (length - firstPart) & (buffer.length - 1);
		}
		else {
			System.arraycopy(content, offset, buffer, tail, length);
			tail = (tail += length) & (buffer.length - 1);
		}
		contentSize += length;
	}

	public void add(char c) {
		add((byte) c);
	}

	public void add(int b) {
		add((byte) b);
	}

	public void clear() {
		head = tail = contentSize = 0;
	}

	@Override
	public CircularByteQueue clone() {
		CircularByteQueue clone;
		try {
			clone = (CircularByteQueue) super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
		clone.buffer = buffer.clone();
		return clone;
	}

	public void delete(int count) {
		if(count > contentSize || count < 0)
			throw new NoSuchElementException();
		head = (head + count) & (buffer.length - 1);
		contentSize -= count;
	}

	/**
	 * Deletes from head of the buffer until the given byte sequence is found. If the inclusive flag
	 * is true, the given sequence is also deleted (when found).
	 * If the given sequence is not found, there will be at most bytes.length - 1 bytes left in the buffer.
	 * 
	 * @param bytes
	 * @param inclusive
	 * @return true if the given sequence was found
	 */
	public boolean deleteUntil(byte[] bytes, boolean inclusive) {
		try {
			while(!startsWith(bytes))
				delete(1);
		}
		catch(NoSuchElementException e) {
			return false; // not matched
		}
		if(inclusive)
			delete(bytes.length);
		return true;

	}

	/**
	 * Double the capacity of this queue.
	 */
	private void doubleCapacity() {
		int oldCapacity = buffer.length;
		int newCapacity = oldCapacity << 1;
		if(newCapacity < 0) {
			throw new IllegalStateException("size too big");
		}
		byte[] newBuffer = new byte[newCapacity];
		System.arraycopy(buffer, head, newBuffer, 0, oldCapacity - head);
		System.arraycopy(buffer, 0, newBuffer, oldCapacity - head, head);
		head = 0;
		tail = oldCapacity;
		buffer = newBuffer;
	}

	private void ensureCapacity(int wantedCapacity) {
		int oldCapacity = buffer.length;
		if(wantedCapacity > oldCapacity) {
			int newCapacity = Integer.highestOneBit(wantedCapacity);
			// if wanted is not ^2, use next ^2
			if(newCapacity != wantedCapacity)
				newCapacity <<= 1;

			if(newCapacity < 0)
				throw new IllegalArgumentException("expected size too big");

			byte[] newBuffer = new byte[newCapacity];
			if(tail > head) {
				System.arraycopy(buffer, head, newBuffer, 0, tail - head);
			}
			else {
				int headToEnd = oldCapacity - head;
				// from head to end
				System.arraycopy(buffer, head, newBuffer, 0, headToEnd);
				// from start to tail
				System.arraycopy(buffer, 0, newBuffer, headToEnd, tail);
			}
			head = 0;
			tail = contentSize;
			buffer = newBuffer;
		}

	}

	public boolean isEmpty() {
		return contentSize == 0;
	}

	public byte peek() {
		if(contentSize == 0) {
			throw new NoSuchElementException();
		}
		return buffer[head];
	}

	public byte[] peek(int count) {
		if(count > contentSize || count < 0)
			throw new NoSuchElementException();
		byte[] result = new byte[count];
		for(int i = 0; i < count; i++)
			result[i] = buffer[(head + i) & (buffer.length - 1)];
		return result;
	}

	/**
	 * Removes (and returns) the head of the buffer
	 * 
	 * @return the head of the buffer
	 * @throws NoSuchElementException
	 *             if this buffer is empty
	 */
	public byte remove() {
		if(contentSize == 0) {
			throw new NoSuchElementException();
		}
		byte result = buffer[head];
		head = (head + 1) & (buffer.length - 1);
		contentSize--;
		return result;
	}

	public byte[] remove(int count) {
		if(count > contentSize || count < 0)
			throw new NoSuchElementException();
		byte[] result = new byte[count];
		for(int i = 0; i < count; i++)
			result[i] = buffer[(head + i) & (buffer.length - 1)];
		head = (head + count) & (buffer.length - 1);
		contentSize -= count;
		return result;
	}

	public int size() {
		return contentSize;
	}

	/**
	 * Returns true if the sequence of bytes at head to head + bytes.length are equals to the given bytes.
	 * 
	 * @param bytes
	 * @return
	 * @throws NoSuchElementException
	 *             if bytes.length > size of buffer
	 */
	public boolean startsWith(byte[] bytes) {
		if(bytes.length > contentSize)
			throw new NoSuchElementException();
		for(int i = 0; i < bytes.length; i++)
			if(buffer[(head + i) & (buffer.length - 1)] != bytes[i])
				return false;
		return true;
	}

	/**
	 * Returns a newly allocated byte array with the content of the circular buffer.
	 * If the size is 0, an empty array is returned.
	 */
	public byte[] toArray() {
		byte[] array = new byte[contentSize];
		for(int i = 0; i < contentSize; i++) {
			array[i] = buffer[(head + i) & (buffer.length - 1)];
		}
		return array;
	}

	/**
	 * Returns a human readable representation of the buffers content - showing each byte as an integer value.
	 */
	@Override
	public String toString() {
		// StringBuilder is a faster StringBuffer
		StringBuilder buf = new StringBuilder();
		buf.append('[');
		for(int i = 0; i < contentSize; i++) {
			buf.append(buffer[(head + i) & (buffer.length - 1)]).append(", ");
		}
		if(contentSize > 0) {
			buf.setLength(buf.length() - 2); // delete trailing comma and space
		}
		buf.append(']');
		return buf.toString();
	}

	/**
	 * Writes the content of the buffer to the given output stream. The buffer's content is unchanged.
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void write(int count, OutputStream out) throws IOException {
		if(count == 0)
			return; // noop

		if(count > size() || count < 0)
			throw new NoSuchElementException("count > size || count < 0");
		// unbroken sequence needs single write
		if(tail > head || head + count < buffer.length)
			out.write(buffer, head, count);
		else {
			// broken sequence needs two writes
			out.write(buffer, head, buffer.length - head);
			count -= buffer.length - head;
			try {
				if(count > 0)
					out.write(buffer, 0, count);
			}
			catch(IndexOutOfBoundsException e) {
				System.out.println("WTF!!");
			}
		}
	}

	/**
	 * Writes count number of bytes from the buffer to out and removes the written content on successful write.
	 * If there is an error while writing, the content is not removed.
	 * 
	 * @param count
	 * @param out
	 * @throws IOException
	 */
	public void writeFlush(int count, OutputStream out) throws IOException {
		if(count == 0)
			return;
		write(count, out);
		delete(count);
	}

	/**
	 * Write the content of the buffer to the given output stream. The buffer's content is cleared on sucessful
	 * write.
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeFlush(OutputStream out) throws IOException {
		write(size(), out);
		clear();
	}
}
