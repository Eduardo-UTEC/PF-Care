package uy.com.pf.care.infra.tools.strings;

import java.util.Arrays;

public class containEqual {

    // Devuelve true si el str contiene a word como palabra completa, exactamente igual.
    // El formato de str debe ser " "word 1","word 2",..."word n" "
    public static boolean containEqual(String str, String word) {
        return ! Arrays.stream(str.split("\"")).filter(c -> c.equals(word)).toList().isEmpty();
    }
}
