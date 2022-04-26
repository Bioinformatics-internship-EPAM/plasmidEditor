CREATE SCHEMA genbank;

CREATE TABLE genbank.genbanks (
    genbank_id BIGSERIAL PRIMARY KEY,
    accession TEXT NOT NULL,
    version TEXT NOT NULL,
    file TEXT NOT NULL,
    UNIQUE(accession, version)
);