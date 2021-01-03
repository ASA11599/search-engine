CREATE TYPE UserRole AS ENUM ('admin', 'basic');

CREATE TABLE Users (
    ID SERIAL PRIMARY KEY,
    Username VARCHAR UNIQUE NOT NULL,
    PasswordHash VARCHAR NOT NULL,
    AccountRole UserRole NOT NULL
);

INSERT INTO Users (Username, PasswordHash, AccountRole) VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', 'admin');

CREATE TABLE Index (
    ID SERIAL PRIMARY KEY,
    Title VARCHAR NOT NULL,
    Link VARCHAR NOT NULL
);

INSERT INTO Index (Title, Link)
VALUES
('Google', 'https://www.google.com'),
('Wikipedia', 'https://www.wikipedia.org'),
('Welcome to Python.org', 'https://www.python.org'),
('Facebook - Log In or Sign Up', 'https://www.facebook.com'),
('YouTube', 'https://www.youtube.com/');
