INSERT INTO user_account (username, firstname, lastname) VALUES
    ('jdoe1', 'John', 'Doe'),
    ('asmith', 'Alice', 'Smith'),
    ('bwayne', 'Bruce', 'Wayne'),
    ('stx85', 'Bruce', 'Wayne');

INSERT INTO book (isbn, title, authors, publish_date, publishers, cover_link, cover_image, language) VALUES
    ('9783161484100', 'Clean Code', 'Robert C. Martin', '2008', 'Prentice Hall', 'https://example.com/clean-code.jpg', NULL, 'en'),
    ('9780134685991', 'Effective Java', 'Joshua Bloch', '2018', 'Addison-Wesley', 'https://example.com/effective-java.jpg', NULL, 'en'),
    ('9780201633610', 'Design Patterns', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', '1994', 'Addison-Wesley', 'https://example.com/design-patterns.jpg', NULL, 'en');

INSERT INTO user_book (user_account_id, book_id, rating, comment) VALUES
    (1, 1, 5, 'Tolles Buch für Clean-Code-Prinzipien'),
    (1, 2, 4, 'Sehr hilfreich, aber etwas fortgeschritten'),
    (2, 3, 5, 'Ein Klassiker für Softwaredesign'),
    (3, 1, 3, 'Guter Einstieg, aber etwas trocken'),
    (4, 1, 3, 'Guter Einstieg, aber etwas trocken'),
    (4, 2, 3, 'Guter Einstieg, aber etwas trocken');