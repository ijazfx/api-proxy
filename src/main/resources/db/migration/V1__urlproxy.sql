CREATE TABLE url (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    alias VARCHAR(50),
    url VARCHAR(500) NOT NULL,
    has_certificate BOOLEAN NOT NULL default false,
    file_path VARCHAR(500),
    passphrase VARCHAR(200),
    is_active BOOLEAN NOT NULL default true
);
