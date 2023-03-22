package uy.com.pf.care.infra.tools;

import java.util.Arrays;

public class Strings {

    // Devuelve true si el str contiene a word como palabra completa, exactamente igual.
    // El formato de str debe ser " "word 1","word 2",..."word n" "
    public static boolean containEqual(String str, String word) {
        return ! Arrays.stream(str.split("\"")).filter(c -> c.equals(word)).toList().isEmpty();
    }
}
