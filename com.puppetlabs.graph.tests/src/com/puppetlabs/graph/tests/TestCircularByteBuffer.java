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
package com.puppetlabs.graph.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.NoSuchElementException;

import com.puppetlabs.graph.utils.CircularByteQueue;
import org.junit.Test;

/**
 * tests the CircularByteBuffer
 * 
 */
public class TestCircularByteBuffer {

	@Test
	public void test_growWithTailGtHead() throws Exception {
		CircularByteQueue buf = new CircularByteQueue();
		buf.add('a');
		buf.add("12345678901234567890".getBytes("UTF8"));
		assertEquals("Should have an 'a'", 'a', buf.peek());
		assertEquals("size", 21, buf.size());
		assertEquals("Should start with 'a123'", "a123", new String(buf.peek(4)));
	}

	@Test
	public void test_peek() throws Exception {
		CircularByteQueue buf = new CircularByteQueue();
		boolean caught = false;
		try {
			buf.peek();
		}
		catch(NoSuchElementException e) {
			caught = true;
		}
		assertTrue("Should have thrown NoSuchElementException", caught);

		caught = false;
		try {
			buf.peek(2);
		}
		catch(NoSuchElementException e) {
			caught = true;
		}
		assertTrue("Should have thrown NoSuchElementException", caught);

		buf.add("123456789".getBytes());
		assertEquals("peek one", '1', buf.peek());
		assertEquals("peek two", "12", new String(buf.peek(2)));
		assertEquals("peek all(9)", "123456789", new String(buf.peek(9)));

		buf.add("01234567".getBytes());
		assertEquals("peek all(17)", "12345678901234567", new String(buf.peek(17)));

	}

	@Test
	public void test_remove() throws Exception {
		CircularByteQueue buf = new CircularByteQueue();

		boolean caught = false;
		try {
			buf.remove();
		}
		catch(NoSuchElementException e) {
			caught = true;
		}
		assertTrue("Should have caught exception for remove empty", caught);

		caught = false;
		try {
			buf.remove(4);
		}
		catch(NoSuchElementException e) {
			caught = true;
		}
		assertTrue("Should have caught exception for remove empty", caught);

		buf.add("123".getBytes());
		assertEquals("Remove of first", '1', buf.remove());
		assertEquals("Remove of second", '2', buf.remove());
		assertEquals("Remove of third (last)", '3', buf.remove());
		assertTrue("Should now be empty", buf.isEmpty());

		buf.add("abcdefghijklmnopqrstuvxyz".getBytes());
		assertEquals("Remove of first sequence", "abc", new String(buf.remove(3)));
		assertEquals("Remove of first sequence", "def", new String(buf.remove(3)));
		assertEquals("Remove of first sequence", "ghi", new String(buf.remove(3)));
		assertEquals("Remove of first sequence", "jkl", new String(buf.remove(3)));
		assertEquals("Remove of first sequence", "mno", new String(buf.remove(3)));
		assertEquals("Remove of first sequence", "pqr", new String(buf.remove(3)));
		assertEquals("Remove of first sequence", "stu", new String(buf.remove(3)));
		assertEquals("Remove of first sequence", "vxy", new String(buf.remove(3)));
		assertEquals("Remove of first sequence", "z", new String(buf.remove(1)));

	}

	@Test
	public void test_size() throws Exception {
		CircularByteQueue buf = new CircularByteQueue();
		assertEquals("Should have zero size", 0, buf.size());
		assertTrue("Should be empty", buf.isEmpty());

		buf.add('a');
		assertEquals("Should have 1 size", 1, buf.size());
		assertFalse("Should not be empty", buf.isEmpty());

		buf.peek();
		assertEquals("Should have 1 size", 1, buf.size());
		assertEquals("Should remove an 'a'", 'a', buf.remove());
		assertEquals("Should have zero size", 0, buf.size());

		buf.add("12345678901234567890".getBytes("UTF8"));
		assertEquals("Should have 20 size", 20, buf.size());
		assertEquals("correct after grow", "12345678901234567890", new String(buf.peek(20)));

		assertEquals("removes 10", 10, buf.remove(10).length);
		assertEquals("Should have 10 size", 10, buf.size());

		buf.add("xxxxxxxxxxyyyyyyyyyy".getBytes("UTF8"));
		assertEquals("no overrun on grow", "1234567890", new String(buf.peek(10)));
		assertEquals("Should have 30 size", 30, buf.size());
	}

	@Test
	public void test_startsWith() throws Exception {
		CircularByteQueue buf = new CircularByteQueue();
		buf.add("abc".getBytes());
		assertFalse("does not start with axy", buf.startsWith("axy".getBytes()));
		assertFalse("does not start with xy", buf.startsWith("xy".getBytes()));
		assertFalse("does not start with x", buf.startsWith("x".getBytes()));

		boolean caught = false;
		try {
			buf.startsWith("too long".getBytes());
		}
		catch(NoSuchElementException e) {
			caught = true;
		}
		assertTrue("Should have thrown NoSuchElementException", caught);
	}

	@Test
	public void test_write() throws Exception {
		CircularByteQueue buf = new CircularByteQueue(16);
		buf.add("1234567890".getBytes());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		buf.write(1, stream);
		assertEquals("wrote one char - should read the same", '1', stream.toByteArray()[0]);
		assertEquals("size is still 10", 10, buf.size());

		buf.add("abcdefgh".getBytes()); // push it over the 16 byte initial size (now 18)
		buf.remove(); // remove one to move head

		stream = new ByteArrayOutputStream();
		buf.write(17, stream);
		assertEquals("read what was written after overflow", "234567890abcdefgh", new String(stream.toByteArray()));

		// write should not change anything in the buf
		stream = new ByteArrayOutputStream();
		buf.write(17, stream);
		assertEquals("read what was written after overflow", "234567890abcdefgh", new String(stream.toByteArray()));

	}

	@Test
	public void test_writeFlush() throws Exception {
		CircularByteQueue buf = new CircularByteQueue(16);
		buf.add("1234567890".getBytes());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		buf.writeFlush(1, stream);
		assertEquals("wrote one char - should read the same", '1', stream.toByteArray()[0]);
		assertEquals("size is now 9", 9, buf.size());

		buf.add("abcdefghi".getBytes()); // push it over the 16 byte initial size (now 18)
		buf.remove(); // remove one to move head

		stream = new ByteArrayOutputStream();
		buf.writeFlush(8, stream);
		assertEquals("read what was written", "34567890", new String(stream.toByteArray()));

		// write what remains
		stream = new ByteArrayOutputStream();
		buf.writeFlush(9, stream);
		assertEquals("read what was written ", "abcdefghi", new String(stream.toByteArray()));
		assertTrue("buffer is now empty", buf.isEmpty());
	}

}
