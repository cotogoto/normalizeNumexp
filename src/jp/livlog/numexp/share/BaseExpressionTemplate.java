package jp.livlog.numexp.share;

import lombok.ToString;

@ToString(callSuper=false)
public class BaseExpressionTemplate {

    public String originalExpression;

    public int    positionStart;

    public int    positionEnd;

    public String pattern;

    public NTime  valueLowerbound;

    public NTime  valueUpperbound;
}
