create table accounts(
    account_id uuid primary key,
    owner_id bigint  not null unique,
    balance numeric(20, 2) not null,
    currency varchar(20) not null,
    status varchar(20) not null,
    version bigint not null default 0
);