CREATE SCHEMA genbank;

CREATE TABLE genbank.genbanks (
    accession TEXT NOT NULL,
    version TEXT NOT NULL,
    file TEXT NOT NULL,
    PRIMARY KEY(accession, version)
);