CREATE SCHEMA IF NOT EXISTS genbank;

CREATE TABLE IF NOT EXISTS genbank.genbanks
(
    genbank_id BIGSERIAL UNIQUE NOT NULL,
    accession TEXT NOT NULL,
    version TEXT NOT NULL,
    file TEXT NOT NULL,
    CONSTRAINT genbank_pkey PRIMARY KEY (genbank_id),
    CONSTRAINT accession_version_unique UNIQUE (accession, version)
);