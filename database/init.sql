CREATE TABLE Administrators (
    ID SERIAL PRIMARY KEY,
    Username VARCHAR UNIQUE NOT NULL,
    PasswordHash VARCHAR NOT NULL
);

INSERT INTO Administrators (Username, PasswordHash) VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3');

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
