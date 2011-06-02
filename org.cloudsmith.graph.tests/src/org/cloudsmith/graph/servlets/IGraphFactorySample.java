package org.cloudsmith.graph.servlets;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.security.AccessControlException;

public interface IGraphFactorySample {

	/**
	 * Example method called from servlet to obtain a graph. the factory should write to the ByteArrayOutputStream
	 * and return true if it completed normally. The factory may throw exceptions for graph not being found,
	 * or that user does not have access to the graph referenced by the oid....
	 * 
	 * NOTE: this is just an illustration of a possible API...
	 * 
	 * @param oid
	 * @param result
	 * @return
	 * @throws FileNotFoundException
	 * @throws AccessControlException
	 */
	boolean getGraphSvg(String oid, ByteArrayOutputStream result) throws FileNotFoundException, AccessControlException;

}
