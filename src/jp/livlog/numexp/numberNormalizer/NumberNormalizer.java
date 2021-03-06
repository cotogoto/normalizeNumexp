package jp.livlog.numexp.numberNormalizer;

import java.util.List;

import jp.livlog.numexp.digitUtility.DigitUtility;
import jp.livlog.numexp.digitUtility.impl.DigitUtilityImpl;
import jp.livlog.numexp.numberNormalizer.impl.NumberExtractorImpl;
import jp.livlog.numexp.numberNormalizer.impl.SymbolFixerImpl;
import jp.livlog.numexp.share.NNumber;

public abstract class NumberNormalizer {

    protected DigitUtility               digitUtility = null;

    protected NumberExtractor            NE           = null;

    protected InfNumberConverterTemplate NC           = null;

    protected SymbolFixer                SF           = null;

    public NumberNormalizer(String language) {

        this.language = language;
        this.digitUtility = new DigitUtilityImpl();
        this.digitUtility.initKansuji(language);
        this.NE = new NumberExtractorImpl(this.digitUtility);
        this.SF = new SymbolFixerImpl(this.digitUtility);
    }


    public abstract void process(String input, List <NNumber> output);


    // 絶対時間表現の規格化の際に使用する（絶対時間表現では、前もって記号を処理させないため）
    public abstract void processDontFixBySymbol(String input, List <NNumber> output);

    public String language;
}
