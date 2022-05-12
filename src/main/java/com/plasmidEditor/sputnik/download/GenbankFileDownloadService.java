package com.plasmidEditor.sputnik.download;

import com.plasmidEditor.sputnik.GenBankDTO;
import com.plasmidEditor.sputnik.services.GenBankService;
import com.plasmidEditor.sputnik.services.GenBankServiceImpl;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

public interface GenbankFileDownloadService<T extends AbstractSequence<?>> {

    T downloadFileAsSequence(String accession, String version);

    default GenBankDTO downloadFile(String accession, String version) {
        GenBankService service = new GenBankServiceImpl();
        if (version.equals("latest")) {
            return service.getLatestVersion(accession);
        } else {
            return service.get(accession, version);
        }
        //обрабатывать GenBankNotFoundException тут и бросать DownloadGenbankFileException?
    }
}
