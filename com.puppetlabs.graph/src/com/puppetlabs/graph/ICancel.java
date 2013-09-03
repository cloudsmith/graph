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
package com.puppetlabs.graph;

import java.util.concurrent.CancellationException;

/**
 * An indicator passed to long running operations that can be canceled.
 * Operations should periodically check if cancel has been requested and throw {@link CancellationException} if true.
 * 
 * 
 */
public interface ICancel {
	public static class Indicator implements ICancel {
		private boolean cancel = false;

		@Override
		public void assertContinue() throws CancellationException {
			if(isCanceled())
				throw new CancellationException();
		}

		@Override
		public boolean isCanceled() {
			return cancel;
		}

		public void setCanceled(boolean flag) {
			cancel = flag;
		}

	}

	public static class NullIndicator implements ICancel {
		@Override
		public void assertContinue() throws CancellationException {
			/* do nothing */
		}

		@Override
		public boolean isCanceled() {
			return false;
		}
	}

	public static final ICancel NullIndicator = new NullIndicator();

	public void assertContinue() throws CancellationException;

	/**
	 * Returns whether cancelation of current operation has been requested.
	 * Long-running operations should poll to see if cancelation
	 * has been requested.
	 * 
	 * @return <code>true</code> if cancellation has been requested,
	 *         and <code>false</code> otherwise
	 * @see #setCanceled(boolean)
	 */
	public boolean isCanceled();

}
