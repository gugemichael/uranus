package org.uranus.configuration.loader;

import org.uranus.configuration.ConfigLoadException;
import org.uranus.configuration.ConfigureKey;
import org.uranus.configuration.ConfigureOption;
import org.uranus.configuration.GenericStruct;
import org.uranus.configuration.parser.ConfigureParser;
import org.uranus.configuration.parser.KeyValueParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Common process on config pair with Map, String or File
 *
 * supplied diffrent {@link ConfigureParser} and {@link ConfigureOption}
 * implemention class will concrete a few of read* method, read diffrent
 * type of config key as follows :
 *
 * String, Number, Boolean, Object
 *
 * @author Michael xixuan.lx
 */
public abstract class AbstractConfigureLoader implements ConfigureLoader {

    /**
     * operation on unknown type occurred or parse value failed
     */
    private final ConfigureOption option;

    /**
     * analytic key with parser's parse()
     */
    private ConfigureParser PARSER = new KeyValueParser();

    public AbstractConfigureLoader() {
        this(new ConfigureOption().setParsePolicy(ConfigureOption.ConfigureParsePolicy.EXCEPTION));
    }

    public AbstractConfigureLoader(ConfigureOption policy) {
        if (policy == null)
            throw new NullPointerException("");

        this.option = policy;
    }

    public AbstractConfigureLoader setConfigureParser(ConfigureParser parser) {
        if (parser != null)
            this.PARSER = parser;
        return this;
    }

    /**
     * concrete type key resolv code
     *
     * @param member, to be analyzed Class's member field
     * @param type,   self member field class type
     * @param key,    annotated key name
     * @param value,  value in config string
     * @throws IllegalAccessException, inject or access member exception
     * @throws ConfigLoadException,    parsing exception
     */
    @SuppressWarnings("unchecked")
    private void resolv(Field member, Class<?> type, final String key, final String value) throws IllegalAccessException, ConfigLoadException {

        if (type == int.class || type == short.class || type == long.class || type == float.class || type == double.class) {			 /* number */
            Double number = readNumber(key, value);
            if (type == int.class)
                member.setInt(null, number.intValue());
            else if (type == short.class)
                member.setShort(null, number.shortValue());
            else if (type == long.class)
                member.setLong(null, number.longValue());
            else if (type == float.class)
                member.setFloat(null, number.floatValue());
            else if (type == double.class)
                member.setDouble(null, number);

        } else if (type == Integer.class || type == Short.class || type == Long.class || type == Float.class || type == Double.class) {     /* number boxing */
            Double number = readNumber(key, value);
            if (type == Integer.class)
                member.set(null, number.intValue());
            if (type == Short.class)
                member.set(null, number.shortValue());
            if (type == Long.class)
                member.set(null, number.longValue());
            if (type == Float.class)
                member.set(null, number.floatValue());
            if (type == Double.class)
                member.set(null, number);

        } else if (type == boolean.class) { 							/* boolean */
            member.setBoolean(null, readBoolean(key, value));

        } else if (type == Boolean.class) { 							/* boolean boxing */
            member.set(null, readBoolean(key, value));

        } else if (type == String.class) {								/* string */
            member.set(null, readString(key, value));

        } else if (type == List.class || type == Set.class) { 		/* List and Set */

            String[] values = value.split(",");

            // generic type
            Type generic = ((ParameterizedType) member.getGenericType()).getActualTypeArguments()[0];

            @SuppressWarnings("rawtypes")
            List list = GenericStruct.makeGenericList(generic);

            if (list == null)
                throw new IllegalAccessException("generic list resolve failed");

            // TODO : merge with *GenericStruct*
            for (String v : values) {
                if (generic.equals(String.class))
                    list.add(readString(key, v));
                else if (generic.equals(Short.class))
                    list.add(readNumber(key, v).shortValue());
                else if (generic.equals(Integer.class))
                    list.add(readNumber(key, v).intValue());
                else if (generic.equals(Long.class))
                    list.add(readNumber(key, v).longValue());
                else if (generic.equals(Float.class))
                    list.add(readNumber(key, v).floatValue());
                else if (generic.equals(Double.class))
                    list.add(readNumber(key, v));
                else if (generic.equals(Byte.class))
                    list.add(readBoolean(key, v));
                else
                    list.add(readObject(key, v));
            }

            if (type == Set.class)
                member.set(null, new HashSet<Object>(list));
            else
                member.set(null, list);

        } else if (type.isEnum()) { 										/* enum */

            for (Object item : type.getEnumConstants())
                if (item.toString().equals(value)) {
                    member.set(null, item);
                    break;
                }

            // enum not matched
            if (member.get(null) == null)
                throw new ConfigLoadException(ConfigLoadException.ExceptionCode.VALUE_PARSE_FAILED, String.format("enum type %s parse key %s failed", type.getSimpleName(), key));

        } else {																	/* Object */

            member.set(null, readObject(key, value));
        }
    }

    /**
     * Parse properties under key-value Map
     *
     * @param clazz, to be analyzed target Class
     * @param kv,    key value pairs
     * @throws ConfigLoadException
     */
    public void parse(Class<?> clazz, Map<String, String> kv) throws ConfigLoadException {
        if (clazz == null || kv == null || kv.isEmpty())
            throw new ConfigLoadException(ConfigLoadException.ExceptionCode.ARGUMENT_INVALID, "key value map or clazz is invalid");

        String value = null;

        /**
         * iterate every member field in clazz , search for someont which is
         * matched for our Modifier, and get its ConfigureKey annotation 's key
         * name, finally get the value from the Property. if not found we will
         * take action with *policy*
         *
         */
        for (Field field : clazz.getDeclaredFields()) {

            // must PUBLIC STATIC
            if ((field.getModifiers() & Modifier.STATIC) == 0 || (field.getModifiers() & Modifier.PUBLIC) == 0)
                continue;

            // only annotated with ConfigureKey
            ConfigureKey annotation = field.getAnnotation(ConfigureKey.class);
            if (annotation == null)
                continue;

            final String annotationKey = annotation.key();

            // only concerned with {@Link ConfigureKey}
            if (annotationKey == null)
                continue;

            value = kv.get(annotationKey);

            if (value == null) {

                // skip only when not required and in kvMap, with nullValue
                if (!annotation.required())
                    continue;

                // TODO : not beautiful !
                switch (option.getParsePolicy()) {
                case EXCEPTION:
                    throw new ConfigLoadException(ConfigLoadException.ExceptionCode.KEY_NOT_FOUND, String.format("config key not found %s, %s", annotationKey, field.getName()));
                case DISCARD:
                    continue;
                default:
                    break;
                }
            }

            // TODO : suppress *Findbug* nullptr hint info
            value = value.trim();

            Class<?> type = field.getType();

            try {
                resolv(field, type, annotationKey, value);
            } catch (NumberFormatException e) {
                throw new ConfigLoadException(ConfigLoadException.ExceptionCode.VALUE_PARSE_FAILED, String.format("convert annotation key %s failed", annotationKey));
            } catch (IllegalArgumentException e) {
                throw new ConfigLoadException(ConfigLoadException.ExceptionCode.VALUE_PARSE_FAILED, String.format("set annotation key %s failed", annotationKey));
            } catch (IllegalAccessException e) {
                throw new ConfigLoadException(ConfigLoadException.ExceptionCode.KEY_ACCESS_INVALID, String.format("access annotation key %s failed", annotationKey));
            }
        }
    }

    /**
     * Parse properties under key-value string
     *
     * @param clazz, to be analyzed target Class
     * @param conf,  key value string
     */
    @Override
    public void parse(Class<?> clazz, String conf) throws ConfigLoadException {

        if (clazz == null || conf == null || conf.isEmpty())
            throw new ConfigLoadException(ConfigLoadException.ExceptionCode.ARGUMENT_INVALID, "config string or clazz is invalid");

        // read properties
        Map<String, String> kv = PARSER.parse(conf);

        parse(clazz, kv);
    }

    /**
     * Parse properties under File content string line
     *
     * @param clazz, to be analyzed target Class
     * @param conf,  config file
     */
    @Override
    public void parse(Class<?> clazz, File conf) throws ConfigLoadException, IOException {

        if (clazz == null || conf == null || !conf.exists() || !conf.canRead())
            throw new ConfigLoadException(ConfigLoadException.ExceptionCode.ARGUMENT_INVALID, "config file or clazz is invalid");

        char[] buffer = new char[4096];
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(conf));
            // read file content
            while (true) {
                int len = reader.read(buffer);
                if (len == -1)
                    break;
                content.append(buffer, 0, len);
            }

            if (content.length() == 0)
                throw new IOException("config file content empty");

            parse(clazz, content.toString());

        } finally {
            if (reader != null)
                reader.close();
        }
    }

    /**
     * read boolean value
     */
    protected abstract Boolean readBoolean(String key, String value) throws UnknownFormatConversionException;

    /**
     * read numberic value
     */
    protected abstract Double readNumber(String key, String value) throws NumberFormatException;

    /**
     * read plain string
     */
    protected abstract String readString(String key, String value) throws NumberFormatException;

    /**
     * read serialized or binary class object
     */
    protected abstract Object readObject(String key, String value) throws NumberFormatException;
}
