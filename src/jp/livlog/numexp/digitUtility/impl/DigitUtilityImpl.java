package jp.livlog.numexp.digitUtility.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import jp.livlog.numexp.dictionaryDirpath.DictionaryDirpath;
import jp.livlog.numexp.digitUtility.DigitUtility;
import jp.livlog.numexp.share.ENotationType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DigitUtilityImpl extends DigitUtility {

    public TreeMap <String, ENotationType> stringToNotationType     = new TreeMap <>();

    public TreeMap <String, Integer>       kansuji09ToValue         = new TreeMap <>();

    public TreeMap <String, Integer>       kansujiKuraiToPowerValue = new TreeMap <>();

    public class ChineseCharacter {

        public ChineseCharacter(final String character, int value, String notationType) {

            this.character = character;
            this.value = value;
            this.notationType = notationType;
        }

        @SerializedName ("character")
        @Expose
        public final String  character;

        @SerializedName ("value")
        @Expose
        public final Integer value;

        @SerializedName ("NotationType")
        @Expose
        public final String  notationType;
    }

    void loadFromDictionary(final String dictionaryPath, List <ChineseCharacter> loadTarget) {

        loadTarget.clear();

        final var reader = this.fileLoad(dictionaryPath);

        final var gson = new Gson();
        try (var br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                final var chineseCharacter = gson.fromJson(line, ChineseCharacter.class);
                loadTarget.add(chineseCharacter);
            }
        } catch (final IOException e) {
            DigitUtilityImpl.log.error(e.getMessage(), e);
        }
    }


    @Override
    public void initKansuji(String language) {

        final var chineseCharacters = new ArrayList <ChineseCharacter>();
        final var dictionaryPath = new StringBuilder(DictionaryDirpath.DICTIONARY_DIRPATH);
        if (language.equals("ja")) {
            dictionaryPath.append("ja/chinese_character.txt");
        } else if (language.equals("zh")) {
            dictionaryPath.append("zh/chinese_character.txt");
        } else {
            return;
        }

        this.loadFromDictionary(dictionaryPath.toString(), chineseCharacters);
        for (var i = 0; i < chineseCharacters.size(); i++) {
            var notationType = ENotationType.NOT_NUMBER;
            if (chineseCharacters.get(i).notationType.equals("09")) {
                notationType = ENotationType.KANSUJI_09;
            } else if (chineseCharacters.get(i).notationType.equals("sen")) {
                notationType = ENotationType.KANSUJI_KURAI_SEN;
            } else if (chineseCharacters.get(i).notationType.equals("man")) {
                notationType = ENotationType.KANSUJI_KURAI_MAN;
            }

            this.stringToNotationType.put(chineseCharacters.get(i).character, notationType);
            switch (notationType) {
                case KANSUJI_09:
                    this.kansuji09ToValue.put(chineseCharacters.get(i).character, chineseCharacters.get(i).value);
                    break;
                case KANSUJI_KURAI_MAN:
                case KANSUJI_KURAI_SEN:
                    this.kansujiKuraiToPowerValue.put(chineseCharacters.get(i).character, chineseCharacters.get(i).value);
                    break;
                default:
                    break;
            }

        }
        this.kansujiKuraiToPowerValue.put("　", 0);
    }


    @Override
    public boolean isHankakusuji(char uc) {

        final var str = String.valueOf(uc);

        var isResult = false;

        // チェック対象文字列がnullの場合はfalseを返す
        if (str == null) {
            return isResult;
        }

        if (str.matches("[0-9]+")) {
            isResult = true;
        }

        return isResult;
    }


    @Override
    public boolean isZenkakusuji(char uc) {

        final var str = String.valueOf(uc);

        var isResult = false;

        // チェック対象文字列がnullの場合はfalseを返す
        if (str == null) {
            return isResult;
        }

        if (str.matches("[０-９]+")) {
            isResult = true;
        }

        return isResult;
    }


    @Override
    public boolean isArabic(char uc) {

        return (this.isHankakusuji(uc) || this.isZenkakusuji(uc));
    }


    public boolean isNotationType(final char uc, ENotationType notationType) {

        final var str = String.valueOf(uc);

        final var check = this.stringToNotationType.get(str);
        if (check == null) {
            return false;
        }

        return check.getValue() == notationType.getValue();
    }


    @Override
    public boolean isKansuji(char uc) {

        return this.isKansuji09(uc) || this.isKansujiKurai(uc);
    }


    @Override
    public boolean isKansuji09(char uc) {

        return this.isNotationType(uc, ENotationType.KANSUJI_09);
    }


    @Override
    public boolean isKansujiKuraiSen(char uc) {

        return this.isNotationType(uc, ENotationType.KANSUJI_KURAI_SEN);
    }


    @Override
    public boolean isKansujiKuraiMan(char uc) {

        return this.isNotationType(uc, ENotationType.KANSUJI_KURAI_MAN);
    }


    @Override
    public boolean isKansujiKurai(char uc) {

        return this.isKansujiKuraiSen(uc) || this.isKansujiKuraiMan(uc);
    }


    @Override
    public boolean isNumber(char uc) {

        return (this.isHankakusuji(uc) || this.isZenkakusuji(uc) || this.isKansuji(uc) || this.isKansujiKurai(uc));
    }


    @Override
    public boolean isComma(char uc) {

        return (',' == uc || '、' == uc || '，' == uc);
    }


    @Override
    public boolean isDecimalPoint(char uc) {

        return ('.' == uc || '・' == uc || '．' == uc);
    }


    @Override
    public boolean isRangeExpression(String str) {

        return str.startsWith("~") || str.startsWith( "〜") || str.startsWith("～") || str.startsWith("-") || str.startsWith("−") || str.startsWith("ー")
                || str.startsWith("―") || str.startsWith("から");
    }


    @Override
    public int convertKansuji09ToValue(char uc) {

        final var value = this.kansuji09ToValue.get(String.valueOf(uc));
        if (value == null) {
            // 例外処理
            throw new NullPointerException("Exception : is not kansuji09");
        }

        return value;
    }


    @Override
    public int convertKansujiKuraiToPowerValue(char uc) {

        final var powerValue = this.kansujiKuraiToPowerValue.get(String.valueOf(uc));
        if (powerValue == null) {
            // 例外処理
            throw new NullPointerException("Exception : is not kansuji_kurai");
        }

        return powerValue;
    }


    @Override
    public ENotationType convertNotationType(char uc) {

        if (this.isHankakusuji(uc)) {
            return ENotationType.HANKAKU;
        } else if (this.isZenkakusuji(uc)) {
            return ENotationType.ZENKAKU;
        } else if (this.isKansuji(uc)) {
            return ENotationType.KANSUJI;
            // } else if (this.isKansuji09(uc)) {
            // return ENotationType.KANSUJI_09;
        } else if (this.isKansujiKurai(uc)) {
            return ENotationType.KANSUJI;
            // } else if (this.isKansujiKuraiSen(uc)) {
            // return ENotationType.KANSUJI_KURAI_SEN;
            // } else if (this.isKansujiKuraiMan(uc)) {
            // return ENotationType.KANSUJI_KURAI_MAN;
        }

        return ENotationType.NOT_NUMBER;
    }

    // @Override
    // public char getNumberStringCharacter(StringBuilder numberString, int i) {
    //
    // if (numberString.length() > 0) {
    // return numberString.toString().toCharArray()[i];
    // }
    //
    // // 例外処理
    // throw new NullPointerException("Exception : is not number string");
    // }


    private Reader fileLoad(String dictionaryPath) {

        try {
            return new InputStreamReader(
                    DigitUtilityImpl.class.getResourceAsStream(dictionaryPath));
        } catch (final Exception e) {
            dictionaryPath = dictionaryPath.replace("/zh/", "/ja/");
            dictionaryPath = dictionaryPath.replace("/en/", "/ja/");
            return new InputStreamReader(
                    DigitUtilityImpl.class.getResourceAsStream(dictionaryPath));
        }
    }
}
