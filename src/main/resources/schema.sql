----------------------------------------------------------------
-- 1) DROP TABLES IN THE CORRECT ORDER (to avoid FK conflicts)
----------------------------------------------------------------
DROP TABLE IF EXISTS t_book_collection_book CASCADE;
DROP TABLE IF EXISTS t_book_collection CASCADE;
DROP TABLE IF EXISTS t_book CASCADE;
DROP TABLE IF EXISTS t_author CASCADE;
DROP TABLE IF EXISTS t_genre CASCADE;
DROP TABLE IF EXISTS t_country CASCADE;

----------------------------------------------------------------
-- 2) CREATE TABLES IN THE LOGICAL ORDER
--    a) AUTHOR, GENRE, COUNTRY
--    b) BOOK
--    c) BOOK_COLLECTION
--    d) BOOK_COLLECTION_BOOK (link table for N-N relationship)
----------------------------------------------------------------

-- TABLE: t_author
CREATE TABLE t_author (
    author_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- TABLE: t_genre
CREATE TABLE t_genre (
    genre_id BIGSERIAL PRIMARY KEY,
    label VARCHAR(255) NOT NULL
);

-- TABLE: t_country
CREATE TABLE t_country (
    country_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- TABLE: t_book
CREATE TABLE t_book (
    book_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    publication_year INT NOT NULL,

    author_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,

    CONSTRAINT fk_author
        FOREIGN KEY (author_id)
        REFERENCES t_author (author_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_genre
        FOREIGN KEY (genre_id)
        REFERENCES t_genre (genre_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_country
        FOREIGN KEY (country_id)
        REFERENCES t_country (country_id)
        ON DELETE RESTRICT
);

-- TABLE: t_book_collection
CREATE TABLE t_book_collection (
    collection_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    distance_jaro DOUBLE PRECISION,
    distance_jaccard DOUBLE PRECISION
);

-- TABLE: t_book_collection_book (N-N relationship)
CREATE TABLE t_book_collection_book (
    collection_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,

    PRIMARY KEY (collection_id, book_id),

    CONSTRAINT fk_collection
        FOREIGN KEY (collection_id)
        REFERENCES t_book_collection (collection_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_book
        FOREIGN KEY (book_id)
        REFERENCES t_book (book_id)
        ON DELETE CASCADE
);
