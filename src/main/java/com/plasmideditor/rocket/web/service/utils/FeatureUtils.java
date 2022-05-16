package com.plasmideditor.rocket.web.service.utils;

import org.biojava.nbio.core.sequence.features.AbstractFeature;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

public class FeatureUtils {
    public static <S extends AbstractSequence<C>, C extends Compound> int getFeatureStart(AbstractFeature<AbstractSequence<C>, C> f) {
        return f.getLocations().getStart().getPosition();
    }

    public static <S extends AbstractSequence<C>, C extends Compound> int getFeatureEnd(AbstractFeature<AbstractSequence<C>, C> f) {
        return f.getLocations().getEnd().getPosition();
    }
}
