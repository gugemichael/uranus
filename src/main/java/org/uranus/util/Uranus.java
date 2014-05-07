package org.uranus.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;


public class Uranus
{
	private static Logger logger = Logger.getLogger(Uranus.class);

	public static void main(String[] args ) throws FileNotFoundException, InterruptedException , IOException
	{
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.println("Complete !!!");
		while(true) {
			int i = 100000;
			long t = System.currentTimeMillis();
			while(i-- != 0)
				logger.debug("[asdfasdf]asdfasdfasdfasdfasdfasdfasd asdsf asd ");
			System.out.println("use " + (System.currentTimeMillis() - t));
		}
		
	}

}
