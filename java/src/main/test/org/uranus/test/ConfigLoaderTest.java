package org.uranus.test;

import java.util.List;
import java.util.Set;
import java.util.UnknownFormatConversionException;

import org.uranus.configuration.ConfigureKey;
import org.uranus.configuration.loader.HumanReadableConfigureLoader;
import org.uranus.configuration.loader.KVConfigureLoader;
import org.uranus.configuration.loader.UserDefinedConfigureLoader;
import org.uranus.util.ClassMemberReflector;

public class ConfigLoaderTest {

	public static enum SomeEnum {
		AA, BB
	}

	public static class Conf {

		@ConfigureKey(key = "a")
		public static int intNumber;

		@ConfigureKey(key = "b")
		public static long longNumber;

		@ConfigureKey(key = "c")
		public static long shortNumber;

		@ConfigureKey(key = "f")
		public static float floatNumber;

		@ConfigureKey(key = "d")
		public static double doubleNumber;

		@ConfigureKey(key = "bool")
		public static boolean bool;

		@ConfigureKey(key = "s")
		public static String string;

		@ConfigureKey(key = "strList")
		public static List<String> strList;

		@ConfigureKey(key = "intList")
		public static List<Integer> intList;

		@ConfigureKey(key = "longList")
		public static List<Long> longList;

		@ConfigureKey(key = "longSet")
		public static Set<Integer> longSet;

		@ConfigureKey(key = "enum")
		public static SomeEnum someEnum;

		@ConfigureKey(key = "not_exist_key", required = false)
		public static String not_exist_key = "set_default_value_here_if_not_exist";
	}

	public static void main(String[] args) {

		try {

			/**
			 * KVConfigureLoader
			 */
			String config = "f=1.13\nd=123.123123123\nlist = 10.15.0.12,23.22.1.1\ns=abcsdef\na=1\nb=2\nc=12\nintList=1,2,3\nstrList=aaaa,cccc,vvvv\nenum=BB\nlongList=1,2,3,4,5\nlongSet=1,1,1,2,2,2\nbool=true";
			new KVConfigureLoader().parse(Conf.class, config);
			System.out.println(new ClassMemberReflector().toString(Conf.class));

			/**
			 * HumanReadableConfigureLoader
			 */
			config = "f=1.13\nd=123.123123123\nlist = 10.15.0.12,23.22.1.1\ns=abcsdef\na=1\nb=2M\nc=12\nintList=1,2,3\nstrList=aaaa,cccc,vvvv\nenum=BB\nlongList=1,2,3,4,5\nlongSet=1,1,1,2,2,2\nbool=on";
			new HumanReadableConfigureLoader().parse(Conf.class, config);
			System.out.println(new ClassMemberReflector().toString(Conf.class));

			/**
			 * UserDefinedConfigureLoader
			 */
			config = "f=1.13\nd=123.123123123\nlist = 10.15.0.12,23.22.1.1\ns=abcsdef\na=1\nb=2\nc=12\nintList=1,2,3\nstrList=aaaa,cccc,vvvv\nenum=BB\nlongList=1,2,3,4,5\nlongSet=1,1,1,2,2,2\nbool=on";
			new UserDefinedConfigureLoader() {

				@Override
				protected String readString(String key, String value) throws NumberFormatException {
					return value;
				}

				@Override
				protected Object readObject(String key, String value) throws NumberFormatException {
					return new Object();
				}

				@Override
				protected Double readNumber(String key, String value) throws NumberFormatException {
					return Double.parseDouble(value);
				}

				@Override
				protected Boolean readBoolean(String key, String value) throws UnknownFormatConversionException {
					return Boolean.parseBoolean(value);
				}
			}.parse(Conf.class, config);

			System.out.println(new ClassMemberReflector().toString(Conf.class));

			// //json format
			// String config2 = "container.ip.list =
			// [{10.15.0.12:asdf,asdf:asdf}],23.22.1.1]\nas=1\nb=2\nc=aaa\nd=[1,2,3]\nenum_e=BB\na=1";
			//
			// new KVConfigureLoader().parse(Conf.class, config2);
			//
			// System.out.println(new
			// ClassMemberReflector().toString(Conf.class));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
