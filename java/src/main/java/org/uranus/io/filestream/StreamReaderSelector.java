package org.uranus.io.filestream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class StreamReaderSelector<T extends SelectableStreamReader> implements Selector<T>
{
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
	
	public List<T> select() {
		StringBuilder buffer = new StringBuilder();
		List<T> readys = new ArrayList<T>();
		try {
			for (T s : streams) {
				buffer.setLength(0);
				/**
				 *  callback and return the ready selectable , choose THE ONE
				 */
				if (s.getReader().ready())
					readys.add(s);
				while(s.getReader().ready())
					buffer.append(s.getReader().readLine());
				s.readyCallback(buffer.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return streams;
	}

}
