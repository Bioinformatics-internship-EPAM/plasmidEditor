package com.plasmidEditor.sputnik.download;

import org.biojava.nbio.core.sequence.template.AbstractSequence;

public interface GenbankFileDownloadService<T extends AbstractSequence<?>> {

    T downloadFile(String accession, String version);

}
