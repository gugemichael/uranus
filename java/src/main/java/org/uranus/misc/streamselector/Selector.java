package org.uranus.misc.streamselector;

import java.util.List;

public interface Selector<T>
{
	public void register(T selectable);
	public void unregister(T selectable);
	
	public List<T> select();
}
