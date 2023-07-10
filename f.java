package com.wancms.sdk.util;

import org.apache.commons.codec.binary.Base64;

public class f {
    public static char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '*', '!'};

    public static String a(String str) {
        if (str == null) {
            return null;
        }
        if (!str.startsWith("x") || !str.endsWith("y")) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.deleteCharAt(0);
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        String[] split = stringBuffer.toString().split("_");
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            int parseInt = Integer.parseInt(split[i]);
            char[] cArr = a;
            stringBuffer2.append((char) (parseInt - cArr[i % cArr.length]));
        }
        return b(stringBuffer2.toString());
    }

    public static String b(String str) {
        return new String(Base64.decodeBase64(new String(str).getBytes()));
    }

    public static String c(String str) {
        if (str == null) {
            return "";
        }
        String d = d(str);
        StringBuffer stringBuffer = new StringBuffer();
        char[] charArray = d.toCharArray();
        stringBuffer.append("x");
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            char[] cArr = a;
            stringBuffer.append(c + cArr[i % cArr.length]);
            stringBuffer.append("_");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append("y");
        return stringBuffer.toString();
    }

    public static String d(String str) {
        return new String(Base64.encodeBase64Chunked(str.getBytes()));
    }
}