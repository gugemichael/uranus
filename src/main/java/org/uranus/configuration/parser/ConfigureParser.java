package org.uranus.configuration.parser;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.Map;

/**
 * Variaty of config string parse into Map key
 * value. only {@link ConfigureParser} implement
 *
 * @author Michael xixuan.lx
 */
public interface ConfigureParser {

    /**
     * parse variety struct config into KeyValue {@link KeyValue} model
     *
     * @param content string based configuration
     * @return key value pair
     */
    Map<String, String> parse(String content);

}
