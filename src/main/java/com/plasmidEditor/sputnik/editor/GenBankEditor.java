package com.plasmidEditor.sputnik.editor;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

public class GenBankEditor {
    public <S extends AbstractSequence<C>, C extends Compound> S add(String subsequence, int position, String file) {
        return new AddMethod().edit(new EditorParameters(subsequence, position, file, 0));
    }

    public <S extends AbstractSequence<C>, C extends Compound> S modify(String subsequence, int position, String file) {
        return new ModifyMethod().edit(new EditorParameters(subsequence, position, file, 0));
    }

    public <S extends AbstractSequence<C>, C extends Compound> S cut(int position, int size, String file) {
        return new CutMethod().edit(new EditorParameters(null, position, file, size));
    }


}
