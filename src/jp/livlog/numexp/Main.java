package jp.livlog.numexp;

import java.util.ArrayList;
import java.util.List;

import jp.livlog.numexp.normalizeNumexp.NormalizeNumexp;
import jp.livlog.numexp.normalizeNumexp.impl.NormalizeNumexpImpl;

public class Main {

    public static void main(String[] args) {

        final var language = args[0];
        final var text = args[1];
        final List <String> result = new ArrayList <>();

        final NormalizeNumexp normalizeNumexp = new NormalizeNumexpImpl(language);
        normalizeNumexp.normalize(text, result);

        for (final String line : result) {
            System.out.println(line);
        }
    }

}
