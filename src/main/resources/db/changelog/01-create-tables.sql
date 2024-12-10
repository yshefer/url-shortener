CREATE TABLE IF NOT EXISTS urls_match_table (
    short_url_id VARCHAR(255) NOT NULL PRIMARY KEY,
    long_url VARCHAR(255)
);

CREATE UNIQUE INDEX IF NOT EXISTS urls_match_table_long_url_uindex
    ON urls_match_table (long_url);
