----------------------------------------------------------------
-- 1) INSERT INTO t_author
----------------------------------------------------------------
INSERT INTO t_author (author_id, name) VALUES
  (1,  'Hans Christian Andersen'),
  (2,  'Jane Austen'),
  (3,  'Honoré de Balzac'),
  (4,  'Samuel Beckett'),
  (5,  'Boccace'),
  (6,  'Jorge Luis Borges'),
  (7,  'Emily Brontë'),
  (8,  'Albert Camus'),
  (9,  'Paul Celan'),
  (10, 'Louis-Ferdinand Céline'),
  (11, 'Miguel de Cervantes'),
  (12, 'Geoffrey Chaucer'),
  (13, 'Joseph Conrad'),
  (14, 'Dante Alighieri'),
  (15, 'Charles Dickens'),
  (16, 'Denis Diderot'),
  (17, 'Alfred Döblin'),
  (18, 'Fiodor Dostoïevski'),
  (19, 'George Eliot'),
  (20, 'Ralph Ellison'),
  (21, 'Euripide'),
  (22, 'William Faulkner');

----------------------------------------------------------------
-- 2) INSERT INTO t_genre
----------------------------------------------------------------
INSERT INTO t_genre (genre_id, label) VALUES
  (1, 'Romanesque (recueil de contes)'),
  (2, 'Romanesque (roman)'),
  (3, 'Romanesque (trilogie romanesque)'),
  (4, 'Romanesque (recueil de nouvelles)'),
  (5, 'Poésie (œuvre poétique)'),
  (6, 'Poésie (épopée)'),
  (7, 'Théâtre (tragédie)');

----------------------------------------------------------------
-- 3) INSERT INTO t_country
----------------------------------------------------------------
INSERT INTO t_country (country_id, name) VALUES
  (1,  'Danemark'),
  (2,  'Royaume-Uni'),
  (3,  'France'),
  (4,  'Irlande'),
  (5,  'Italie'),
  (6,  'Argentine'),
  (7,  'Roumanie'),
  (8,  'Espagne'),
  (9,  'Allemagne'),
  (10, 'Empire russe'),
  (11, 'États-Unis'),
  (12, 'Grèce');

----------------------------------------------------------------
-- 4) INSERT INTO t_book
--    (book_id, title, publication_year, author_id, genre_id, country_id)
--    Apostrophes are escaped by doubling ('L'Étranger' etc.)
----------------------------------------------------------------
INSERT INTO t_book (book_id, title, publication_year, author_id, genre_id, country_id) VALUES
  (1,  'Contes',                                       1837,  1,  1,  1),
  (2,  'Orgueil et Préjugés',                          1813,  2,  2,  2),
  (3,  'Le Père Goriot',                               1835,  3,  2,  3),
  (4,  'Trilogie: Molloy, Malone meurt, L''Innommable', 1953,  4,  3,  4),
  (5,  'Décaméron',                                    1353,  5,  4,  5),
  (6,  'Fictions',                                     1944,  6,  4,  6),
  (7,  'Les Hauts de Hurlevent',                       1847,  7,  2,  2),
  (8,  'L''Étranger',                                   1942,  8,  2,  3),
  (9,  'Les poèmes de...',                             1952,  9,  5,  7),
  (10, 'Voyage au bout de la nuit',                    1932, 10,  2,  3),
  (11, 'Don Quichotte',                                1615, 11,  2,  8),
  (12, 'Les Contes de Canterbury',                     1300, 12,  4,  2),
  (13, 'Nostromo',                                     1904, 13,  2,  2),
  (14, 'Divine Comédie',                               1300, 14,  6,  5),
  (15, 'Les Grandes Espérances',                       1861, 15,  2,  2),
  (16, 'Jacques le Fataliste et son maître',           1796, 16,  2,  3),
  (17, 'Berlin Alexanderplatz',                        1929, 17,  2,  9),
  (18, 'Crime et Châtiment',                           1866, 18,  2, 10),
  (19, 'L''Idiot',                                      1869, 18,  2, 10),
  (20, 'Les Démons',                                   1872, 18,  2, 10),
  (21, 'Les Frères Karamazov',                         1880, 18,  2, 10),
  (22, 'Middlemarch',                                  1871, 19,  2,  2),
  (23, 'Homme invisible, pour qui chantes-tu ?',       1952, 20,  2, 11),
  (24, 'Médée',                                        -431, 21,  7, 12),
  (25, 'Absalon, Absalon !',                           1936, 22,  2, 11);

----------------------------------------------------------------
-- 5) CREATE A COLLECTION WITH THE LAST 10 BOOKS (IDs 16 TO 25)
----------------------------------------------------------------
INSERT INTO t_book_collection (collection_id, name, distance_jaro, distance_jaccard)
VALUES (1, 'Latest Books', NULL, NULL);

-- Link the collection_id=1 with books 16 to 25
INSERT INTO t_book_collection_book (collection_id, book_id) VALUES
  (1, 16),
  (1, 17),
  (1, 18),
  (1, 19),
  (1, 20),
  (1, 21),
  (1, 22),
  (1, 23),
  (1, 24),
  (1, 25);
