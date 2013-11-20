package org.uranus.io.filesystem.poll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

public class StreamSelector<T extends SelectableStream> implements Selector<T>
{
	private static final Logger logger = Logger.getLogger(StreamSelector.class);

	/**
	 * be checked streamings .
	 * 
	 * TODO : use java.util.concurrent.*
	 * 
	 */
	List<T> streams = new Vector<T>();

	public void register(T inStream) {
		streams.add(inStream);
	}

	public void unregister(T inStream) {
		streams.remove(inStream);
	}
	
	@Override
	public List<T> select() {
		List<T> readys = new ArrayList<T>();
		try {
			for (T s : streams) {
				/**
				 *  callback and return the ready selectable , choose THE ONE
				 */
				if (s.getReader().ready())
					readys.add(s);
				s.readyCallback();
			}
		} catch (IOException e) {
			logger.fatal("[StreamSelector] select error : " + e.getMessage());
		}
		
		return streams;
	}

}
