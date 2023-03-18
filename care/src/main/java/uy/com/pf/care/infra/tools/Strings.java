package uy.com.pf.care.infra.tools;

import java.util.Arrays;

public class Strings {

    public static boolean containEqual(String str, String word) {
        return ! Arrays.stream(str.split("\"")).filter(c -> c.equals(word)).toList().isEmpty();
    }
}
