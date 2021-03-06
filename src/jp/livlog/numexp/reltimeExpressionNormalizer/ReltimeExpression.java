package jp.livlog.numexp.reltimeExpressionNormalizer;

import jp.livlog.numexp.normalizerUtility.impl.NormalizedExpressionTemplateImpl;
import jp.livlog.numexp.share.NNumber;
import jp.livlog.numexp.share.NTime;
import jp.livlog.numexp.share.NumexpSymbol;

public class ReltimeExpression extends NormalizedExpressionTemplateImpl {

    public ReltimeExpression(NNumber number) {

        super(number.originalExpression, number.positionStart, number.positionEnd);
        this.orgValueLowerbound = number.valueLowerbound;
        this.orgValueUpperbound = number.valueUpperbound;
        this.valueLowerboundAbs = new NTime(NumexpSymbol.INFINITY);
        this.valueUpperboundAbs = new NTime(-NumexpSymbol.INFINITY);
        this.valueLowerboundRel = new NTime(NumexpSymbol.INFINITY);
        this.valueUpperboundRel = new NTime(-NumexpSymbol.INFINITY);
        this.ordinary = false;
    }

    public double  orgValueLowerbound;

    public double  orgValueUpperbound;

    public NTime   valueLowerboundAbs;

    public NTime   valueUpperboundAbs;

    public NTime   valueLowerboundRel;

    public NTime   valueUpperboundRel;

    public boolean ordinary;
}