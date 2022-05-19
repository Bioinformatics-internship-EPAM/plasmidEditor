package com.plasmideditor.rocket.web.service.utils;

import com.plasmideditor.rocket.web.exceptions.FactoryUnknownOption;
import com.plasmideditor.rocket.web.service.modifications.AddModification;
import com.plasmideditor.rocket.web.service.modifications.CutModification;
import com.plasmideditor.rocket.web.service.modifications.ModifyModification;
import com.plasmideditor.rocket.web.service.modifications.SequenceModification;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import static com.plasmideditor.rocket.web.service.utils.CompoundNames.DNA_CLASS;
import static com.plasmideditor.rocket.web.service.utils.CompoundNames.PROTEIN_CLASS;

public class ModificationFactory {
    public static <T extends AbstractSequence<C>, C extends Compound> SequenceModification<T, C> getOperation(Operations operation, Class<T> cls) {
        if (operation.equals(Operations.ADD)) {
            if (cls.getSimpleName().equals(DNA_CLASS)) {
                return (SequenceModification<T, C>) new AddModification<DNASequence, NucleotideCompound>();
            } else if (cls.getSimpleName().equals(PROTEIN_CLASS)) {
                return (SequenceModification<T, C>) new AddModification<ProteinSequence, AminoAcidCompound>();
            } else {
                throw new FactoryUnknownOption("Unknown Abstract Sequence type");
            }
        }
        if (operation.equals(Operations.CUT)) {
            if (cls.getSimpleName().equals(DNA_CLASS)) {
                return (SequenceModification<T, C>) new CutModification<DNASequence, NucleotideCompound>();
            } else if (cls.getSimpleName().equals(PROTEIN_CLASS)) {
                return (SequenceModification<T, C>) new CutModification<ProteinSequence, AminoAcidCompound>();
            } else {
                throw new FactoryUnknownOption("Unknown Abstract Sequence type");
            }
        }
        if (operation.equals(Operations.MODIFY)) {
            if (cls.getSimpleName().equals(DNA_CLASS)) {
                return (SequenceModification<T, C>) new ModifyModification<DNASequence, NucleotideCompound>();
            } else if (cls.getSimpleName().equals(PROTEIN_CLASS)) {
                return (SequenceModification<T, C>) new ModifyModification<ProteinSequence, AminoAcidCompound>();
            } else {
                throw new FactoryUnknownOption("Unknown Abstract Sequence type");
            }
        }
        throw new FactoryUnknownOption("Unknown option for ModificationFactory");
    }
}
