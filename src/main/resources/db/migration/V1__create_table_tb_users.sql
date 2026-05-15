CREATE TABLE tb_users (
    id       UUID         PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL,

    CONSTRAINT tb_users_email_format CHECK (email LIKE '%@%'),
    CONSTRAINT tb_users_role_check   CHECK (role IN ('ROLE_ADMIN', 'ROLE_DIRETOR', 'ROLE_USER'))
);
