package jp.livlog.normalizeNumexp.inappropriateExpressionRemover;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.livlog.normalizeNumexp.abstimeExpressionNormalizer.AbstimeExpression;
import jp.livlog.normalizeNumexp.durationExpressionNormalizer.DurationExpression;
import jp.livlog.normalizeNumexp.numericalExpressionNormalizer.NumericalExpression;
import jp.livlog.normalizeNumexp.reltimeExpressionNormalizer.ReltimeExpression;

public abstract class InappropriateExpressionRemover {

    public InappropriateExpressionRemover(String language) {

        this.language = language;
    }


    public abstract void removeInappropriateExtraction(
            String text,
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps);


    protected abstract <AnyTypeExpression> void deleteInappropriateExtractionUsingDictionaryOneType(
            java.util.ArrayList <AnyTypeExpression> any_type_expressions);


    protected abstract <AnyTypeExpression> boolean isUrlStrings(String text, AnyTypeExpression anyTypeExpression);


    protected abstract <AnyTypeExpression> void deleteUrlStrings(String text, java.util.ArrayList <AnyTypeExpression> anyTypeExpressions);


    protected abstract void deleteInappropriateExtractionUsingDictionary(
            String text,
            List <NumericalExpression> numexps,
            List <AbstimeExpression> abstimeexps,
            List <ReltimeExpression> reltimeexps,
            List <DurationExpression> durationexps);


    protected abstract void initInappropriateStringss(String language);


    protected abstract void initUrlStrings();

    protected Map <String, Boolean> inappropriateStringsToBool = new TreeMap <>();

    protected Map <String, Boolean> urlStringsToBool           = new TreeMap <>();

    protected String                   language;
}