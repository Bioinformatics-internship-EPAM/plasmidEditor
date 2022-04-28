package com.plasmidEditor.sputnik.services;

import java.util.List;

public interface GenBankService<T> {
    public abstract T save(T t);
    public abstract T get(Long id);
    public abstract T get(String accession, String version);
    public List<T> getAll();
    public abstract void delete(Long id);
    public abstract void delete(String accession, String version);
}
