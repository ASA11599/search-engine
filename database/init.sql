CREATE TABLE Index (
    id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    link VARCHAR NOT NULL
);

INSERT INTO Index (title, link) VALUES ('Google', 'https://www.google.com'), ('Wikipedia', 'https://www.wikipedia.org');
