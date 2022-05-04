package com.plasmidEditor.sputnik.services;

import com.plasmidEditor.sputnik.GenBankDTO;

public interface GenBankService {
    GenBankDTO save(GenBankDTO t);
    GenBankDTO get(Long id);
    GenBankDTO get(String accession, String version);
}
