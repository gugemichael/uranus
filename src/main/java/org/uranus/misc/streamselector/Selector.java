package org.uranus.misc.streamselector;

import java.util.List;

public interface Selector<T>
{
	void register(T selectable);
	void unregister(T selectable);
	
	List<T> select();
}
