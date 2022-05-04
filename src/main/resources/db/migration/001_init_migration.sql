-- +goose Up
-- +goose StatementBegin
SELECT 'up SQL query';

CREATE SCHEMA IF NOT EXISTS genbank;
CREATE TABLE genbank.genbanks(genbank_id serial primary key NOT NULL, accession text NOT NULL, version text NOT NULL, file text NOT NULL);
ALTER TABLE genbank.genbanks ADD CONSTRAINT accession_to_version UNIQUE(accession, version);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
SELECT 'down SQL query';
ALTER TABLE genbank.genbanks DROP CONSTRAINT accession_to_version;
DROP TABLE genbank.genbanks;
DROP SCHEMA genbank;
-- +goose StatementEnd
