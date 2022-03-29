-- !Ups
CREATE TABLE User (
    user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    email VARCHAR(256) NOT NULL,
    UNIQUE KEY user_email (email)
);

CREATE TABLE UserConfirmation (
    user_confirmation_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(256) NOT NULL,
    expiration DATETIME NOT NULL,
    user_id INTEGER NOT NULL,
    UNIQUE KEY confirmation_user (user_id),
    UNIQUE KEY confirmation_token (token)
);

CREATE TABLE Tokens (
    token_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    expires INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    token_value VARCHAR(256) NOT NULL,
    UNIQUE KEY token_value (token_value),
    UNIQUE KEY user_id (user_id)
);

-- !Downs
DROP TABLE User;
DROP TABLE UserConfirmation;
DROP TABLE Tokens;
