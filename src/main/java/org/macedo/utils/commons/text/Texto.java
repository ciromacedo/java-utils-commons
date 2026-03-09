package org.macedo.utils.commons.text;

import org.apache.commons.lang3.StringUtils;

public class Texto {

    public static final String CRLF = "\r\n";
    public static final String TAB = "\t";
    public static final String EMPTY = "";
    public static final String EMPTY_SPACE = " ";

    public static Boolean isNotBlank(String texto) {
        return StringUtils.isNotBlank(texto);
    }

}
