package org.uranus.test;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.uranus.writer.DailyRollFileWriter;
import org.uranus.writer.FileWriter;
import org.uranus.writer.FileWriter.WriteMode;

public class IoWriterTester {

	public static void main(String[] args) throws IOException {

		Random random = new Random();

		StringBuilder sb = new StringBuilder();
		int i = 512;
		while (i-- != 0)
			sb.append("c");

		String s = sb.toString();
		
		char chars[] = new char[] {'a' , 'b', 'c', 'd', 'e' , 'f' , 'g' ,'h'};

		FileWriter writer = new DailyRollFileWriter(true);
		writer.open("/tmp/aaaaaaaaaaaaaaa", WriteMode.TRUNCATE);
		int sum = 0;
		Scanner in = new Scanner(System.in);
		while (true) {
			int times = Math.abs(random.nextInt() % 32);
			StringBuilder b = new StringBuilder();
			String c = String.valueOf(++sum);
			b.append(c);
			
			int n = Math.abs((random.nextInt() % 32));
			
			while(n-- != 0)
				b.append(chars[Math.abs(random.nextInt() % chars.length)]);
			
			writer.writeLine("abcdefghijklmnopqrstuvwxyz | " + b.toString());
			
			System.out.println(b.toString());
		}

		// writer.close();
		//
		// System.out.println("=== IoWriterTester DONE ===");
	}

}
