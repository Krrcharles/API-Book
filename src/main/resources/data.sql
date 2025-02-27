----------------------------------------------------------------
-- 1) INSERT INTO t_author
----------------------------------------------------------------
INSERT INTO t_author (name) VALUES
  ('Hans Christian Andersen'),
  ('Jane Austen'),
  ('Honoré de Balzac'),
  ('Samuel Beckett'),
  ('Boccace'),
  ('Jorge Luis Borges'),
  ('Emily Brontë'),
  ('Albert Camus'),
  ('Paul Celan'),
  ('Louis-Ferdinand Céline'),
  ('Miguel de Cervantes'),
  ('Geoffrey Chaucer'),
  ('Joseph Conrad'),
  ('Dante Alighieri'),
  ('Charles Dickens'),
  ('Denis Diderot'),
  ('Alfred Döblin'),
  ('Fiodor Dostoïevski'),
  ('George Eliot'),
  ('Ralph Ellison'),
  ('Euripide'),
  ('William Faulkner');

----------------------------------------------------------------
-- 2) INSERT INTO t_genre
----------------------------------------------------------------
INSERT INTO t_genre (label) VALUES
  ('Romanesque (recueil de contes)'),
  ('Romanesque (roman)'),
  ('Romanesque (trilogie romanesque)'),
  ('Romanesque (recueil de nouvelles)'),
  ('Poésie (œuvre poétique)'),
  ('Poésie (épopée)'),
  ('Théâtre (tragédie)');

----------------------------------------------------------------
-- 3) INSERT INTO t_country
----------------------------------------------------------------
INSERT INTO t_country (name) VALUES
  ('Danemark'),
  ('Royaume-Uni'),
  ('France'),
  ('Irlande'),
  ('Italie'),
  ('Argentine'),
  ('Roumanie'),
  ('Espagne'),
  ('Allemagne'),
  ('Empire russe'),
  ('États-Unis'),
  ('Grèce');

----------------------------------------------------------------
-- 4) INSERT INTO t_book
--    (title, publication_year, author_id, genre_id, country_id)
----------------------------------------------------------------
INSERT INTO t_book (title, publication_year, author_id, genre_id, country_id) VALUES
  ('Contes',                                        1837,  1,  1,  1),
  ('Orgueil et Préjugés',                           1813,  2,  2,  2),
  ('Le Père Goriot',                                1835,  3,  2,  3),
  ('Trilogie: Molloy, Malone meurt, L''Innommable', 1953,  4,  3,  4),
  ('Décaméron',                                     1353,  5,  4,  5),
  ('Fictions',                                      1944,  6,  4,  6),
  ('Les Hauts de Hurlevent',                        1847,  7,  2,  2),
  ('L''Étranger',                                   1942,  8,  2,  3),
  ('Les poèmes de...',                              1952,  9,  5,  7),
  ('Voyage au bout de la nuit',                     1932, 10,  2,  3),
  ('Don Quichotte',                                 1615, 11,  2,  8),
  ('Les Contes de Canterbury',                      1300, 12,  4,  2),
  ('Nostromo',                                      1904, 13,  2,  2),
  ('Divine Comédie',                                1300, 14,  6,  5),
  ('Les Grandes Espérances',                        1861, 15,  2,  2),
  ('Jacques le Fataliste et son maître',            1796, 16,  2,  3),
  ('Berlin Alexanderplatz',                         1929, 17,  2,  9),
  ('Crime et Châtiment',                            1866, 18,  2, 10),
  ('L''Idiot',                                      1869, 18,  2, 10),
  ('Les Démons',                                    1872, 18,  2, 10),
  ('Les Frères Karamazov',                          1880, 18,  2, 10),
  ('Middlemarch',                                   1871, 19,  2,  2),
  ('Homme invisible, pour qui chantes-tu ?',        1952, 20,  2, 11),
  ('Médée',                                         -431, 21,  7, 12),
  ('Absalon, Absalon !',                            1936, 22,  2, 11);

----------------------------------------------------------------
-- 5) INSERT INTO t_book_collection
----------------------------------------------------------------
INSERT INTO t_book_collection (name, distance_jaro, distance_jaccard) VALUES
  ('Latest Books', 45, 90),
  ('Fist Books', 45, 90);

----------------------------------------------------------------
-- 6) INSERT INTO t_book_collection_book (link table for N-N relationship)
--    (Assuming the collection inserted above gets ID 1 and books retain IDs 16 to 25)
----------------------------------------------------------------
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
  (1, 25),
  (2, 1),
  (2, 2),
  (2, 3),
  (2, 4),
  (2, 5),
  (2, 6),
  (2, 7),
  (2, 8),
  (2, 9),
  (2, 10);
