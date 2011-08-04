package org.cloudsmith.graph.utils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A RuleFilteredOutputStream uses replace, remove and similar rules to trigger modifications
 * of writes to a wrapped OutputStream.
 * 
 */
public class RuleFilteredOutputStream extends FilterOutputStream implements IOutputStreamFilterFactory.IFinishable {

	public abstract class PatternRule {
		protected byte[] pattern;

		boolean active;

		protected PatternRule(byte[] pattern) {
			this.pattern = pattern;
			active = true;
		}

		public boolean isActive() {
			return active;
		}

		protected abstract void performAction() throws IOException;

		public void setActive(boolean flag) {
			active = flag;
		}
	}

	public class RemoveRule extends PatternRule {
		byte[] replacement;

		public RemoveRule(byte[] pattern) {
			super(pattern);
		}

		@Override
		protected void performAction() throws IOException {
			buffer.delete(pattern.length);
		}
	}

	public class ReplaceRule extends PatternRule {
		byte[] replacement;

		public ReplaceRule(byte[] pattern, byte[] replacement) {
			super(pattern);
			this.replacement = replacement;
		}

		@Override
		protected void performAction() throws IOException {
			buffer.delete(pattern.length);
			out.write(replacement);
		}
	}

	/**
	 * The internal buffer where data is stored.
	 */
	protected CircularByteQueue buffer;

	private PatternRule[] rules;

	private int longestPatternSize;

	private boolean deleteMode;

	private PatternRule deleteUntilRule;

	private PatternRule pendingRule;

	private boolean inclusiveDelete;

	/**
	 * Creates a new buffered output stream to write data to the
	 * specified underlying output stream.
	 * 
	 * @param out
	 *            the underlying output stream.
	 */
	public RuleFilteredOutputStream(OutputStream out) {
		this(out, 8192);
	}

	/**
	 * Creates a new buffered output stream to write data to the
	 * specified underlying output stream with the specified buffer
	 * size.
	 * 
	 * @param out
	 *            the underlying output stream.
	 * @param size
	 *            the buffer size.
	 * @exception IllegalArgumentException
	 *                if size &lt;= 0.
	 */
	public RuleFilteredOutputStream(OutputStream out, int size) {
		super(out);
		if(size <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		buffer = new CircularByteQueue(size);
		rules = new PatternRule[0];
		longestPatternSize = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		finish();
		super.close();
	}

	/**
	 * Finishes the filtering. Any remaining bytes (too short to be matched by any rules) is written
	 * to the output. Only call this method when there is a need to "restart" the filtering.
	 * If in delete mode, the remaining bytes are deleted.
	 */
	public void finish() throws IOException {
		if(isDeleteMode()) {
			buffer.delete(buffer.size());
		}
		else if(pendingRule != null) {
			// give a pending rule a last chance to do something
			// before flushing the output
			pendingRule.performAction();
		}
		while(buffer.size() > 0) {
			for(PatternRule r : rules) {
				if(!r.isActive())
					continue;
				// skip patterns that are longer than what remains
				if(r.pattern.length > buffer.size())
					continue;
				if(buffer.startsWith(r.pattern)) {
					r.performAction();
				}
			}
			// At this point, none of the patterns wanted the content (if some processing
			// took place, the scanning has been redone). Write one byte, and retest patterns.
			if(buffer.size() > 0)
				buffer.writeFlush(1, out);
		}
	}

	public boolean isDeleteMode() {
		return deleteMode;
	}

	private void onWrite() throws IOException {
		// a pending rule is in effect until it's action sets pending null
		if(pendingRule != null) {
			pendingRule.performAction();
			return;
		}
		boolean keepScanning = true;
		RESCAN: while(keepScanning) {

			// process deletes
			if(isDeleteMode()) {
				if(buffer.deleteUntil(deleteUntilRule.pattern, inclusiveDelete)) {
					setDeleteModeOff();
					deleteUntilRule.performAction();
					continue RESCAN; // action may have turned on delete of something else
				}
				return; // still in delete mode, 'until pattern' not found, and buffer need more data
			}
			keepScanning = false;
			while(buffer.size() >= longestPatternSize) {
				for(PatternRule r : rules) {
					if(!r.isActive())
						continue;
					if(buffer.startsWith(r.pattern)) {
						r.performAction();
						keepScanning = true;
						continue RESCAN;
					}
				}
				// At this point, none of the patterns wanted the content (if some processing
				// took place, the scanning has been redone). Write one byte, and retest patterns.
				if(buffer.size() > 0)
					buffer.writeFlush(1, out);
				if(buffer.isEmpty())
					break;
			}
		}
	}

	public void setDeleteModeOff() {
		deleteMode = false;
	}

	public void setDeleteModeOn(PatternRule r, boolean inclusive) {
		this.deleteUntilRule = r;
		inclusiveDelete = inclusive;
		deleteMode = true;
	}

	/**
	 * Sets a rule as pending - it will be activated on each write, until it
	 * setPendingRule is called with null.
	 * 
	 * @param rule
	 */
	public void setPendingRule(PatternRule rule) {
		pendingRule = rule;
	}

	public void setRules(PatternRule[] rules) {
		if(rules == null) {
			rules = new PatternRule[0];
			longestPatternSize = 0;
			return;
		}
		Arrays.sort(rules, new Comparator<PatternRule>() {

			@Override
			public int compare(PatternRule o1, PatternRule o2) {
				int length1 = o1.pattern.length;
				int length2 = o2.pattern.length;
				if(length1 == length2)
					return 0;
				// longest first
				return length1 < length2
						? 1
						: -1;
			}
		});
		this.rules = rules;
		longestPatternSize = rules.length > 0
				? rules[0].pattern.length
				: 0;
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array
	 * starting at offset <code>off</code> to this buffered output stream.
	 * 
	 * <p>
	 * Ordinarily this method stores bytes from the given array into this stream's buffer, flushing the buffer to the underlying output stream as
	 * needed. If the requested length is at least as large as this stream's buffer, however, then this method will flush the buffer and write the
	 * bytes directly to the underlying output stream. Thus redundant <code>BufferedOutputStream</code>s will not copy data unnecessarily.
	 * 
	 * @param b
	 *            the data.
	 * @param off
	 *            the start offset in the data.
	 * @param len
	 *            the number of bytes to write.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public synchronized void write(byte b[], int off, int len) throws IOException {
		buffer.add(b, off, len);
		onWrite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	/**
	 * Writes the specified byte to this buffered output stream.
	 * 
	 * @param b
	 *            the byte to be written.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public synchronized void write(int b) throws IOException {
		buffer.add((byte) b);
		onWrite();
	}

}
