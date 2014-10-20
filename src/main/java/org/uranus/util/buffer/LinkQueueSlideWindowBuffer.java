package org.uranus.util.buffer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * Not Thread Safe !
 *
 */
public class LinkQueueSlideWindowBuffer implements SlideWindowBuffer
{
	/**
	 * Max blocks for each SlideWindowBuffer
	 */
	public int max_blocks = 32;

	private Block lastWriteBlock;
	private Queue<Block> blockList = new LinkedList<Block>();
	
	private class Block {
		/**
		 * Each block 128k
		 */
		private static final int BLOCK_SIZE = 16 * 1024;
		public StringBuilder block = new StringBuilder(BLOCK_SIZE);
		public boolean isFull = false;
		public void clear() {
			block.setLength(0);
			isFull = false;
		}
	}
	
	public LinkQueueSlideWindowBuffer(int max_blocks) {
		this.max_blocks = max_blocks;
		addNewBlock();
	}
	
	public LinkQueueSlideWindowBuffer() {
		addNewBlock();
	}
	
	/**
	 * Not Thread Safe
	 */
	@Override
	public void append(String raw) {
		String buf = raw;
		if (raw.length()>MAX_APPEND)
			buf = new String(raw.substring(0,MAX_APPEND));		/* NEW will avoid substring memory leak */
		if (lastWriteBlock.isFull)
			addNewBlock();
		lastWriteBlock.block.append(buf);
		lastWriteBlock.isFull = lastWriteBlock.block.length() >= Block.BLOCK_SIZE;
	}
	
	/**
	 * Not Thread Safe
	 */
	@Override
	public void append(String raw,char segmentation) {
		String buf = raw;
		if (raw.length()>MAX_APPEND)
			buf = new String(raw.substring(0,MAX_APPEND));		/* NEW will avoid substring memory leak */
		if (lastWriteBlock.isFull)
			addNewBlock();
		lastWriteBlock.block.append(buf);
		lastWriteBlock.block.append(segmentation);
		lastWriteBlock.isFull = lastWriteBlock.block.length() >= Block.BLOCK_SIZE;
	}

	/**
	 * Not Thread Safe
	 */
	@Override
	public String getString() {
		StringBuilder buffer = new StringBuilder(Block.BLOCK_SIZE * blockList.size());
		for (Block block : blockList)
			buffer.append(block.block.toString());
		return buffer.toString();
	}

	@Override
	public boolean isEmpty() {
		return blockList.peek().block.length() == 0;
	}

	@Override
	public void clear() {
		while(blockList.size()!=0 && blockList.poll() != null)
			;
		addNewBlock();
	}
	
	private void addNewBlock() {
		// evict (pop) and reuse
		if (blockList.size() >= max_blocks) {
			lastWriteBlock = blockList.poll();
			lastWriteBlock.clear();
		} else 
			lastWriteBlock = new Block();
		blockList.add(lastWriteBlock);
	}

}
