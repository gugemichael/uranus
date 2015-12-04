package org.uranus.test;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.uranus.io.writer.DailyRollFileWriter;
import org.uranus.io.writer.FileWriter;
import org.uranus.io.writer.FileWriter.WriteMode;

public class IoWriterTest {

	public static void main(String[] args) throws IOException, InterruptedException {

		IoWriterTest tester = new IoWriterTest();
		Random random = new Random();

		StringBuilder sb = new StringBuilder();
		int i = 512;
		while (i-- != 0)
			sb.append("c");

		FileWriter writer = new DailyRollFileWriter(false);
		writer.open("/tmp/aaaaaaaaaaaaaaa", WriteMode.TRUNCATE);
		while (true) {
			i = Math.abs(random.nextInt() % 16);
			System.out.println(i);
			while (i-- > 0) {
				String tmp = new Date().toString();
				System.out.println(tmp);
				tester.aaa(writer, tmp);
			}

			System.out.println("ONE");
		}

		// writer.close();
		//
		// System.out.println("=== IoWriterTester DONE ===");
	}

	public void aaa(FileWriter writer, String buffer) {
		writer.writeLine("abcdefghijklmnopqrstuvwxyz | " + buffer);
	}

}
