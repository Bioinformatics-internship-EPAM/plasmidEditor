package com.plasmidEditor.sputnik.services;

import com.plasmidEditor.sputnik.GenBankDTO;

import java.util.List;

public interface GenBankService {
    public abstract GenBankDTO save(GenBankDTO t);
    public abstract GenBankDTO get(Long id);
    public abstract GenBankDTO get(String accession, String version);
}
