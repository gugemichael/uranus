package org.uranus.io.filesystem.stream;

import java.util.List;

public interface Selector<T>
{
	public void register(T selectable);
	public void unregister(T selectable);
	
	public List<T> select();
}
