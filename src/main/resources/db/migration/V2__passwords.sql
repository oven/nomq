create table password
(
    id                 int generated always as identity,
    hashed_password    text,
    cleartext_password text,
    status             varchar(40)
)