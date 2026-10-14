create table transactions(
  transaction_id uuid primary key,
  source_account_id uuid not null,
  target_account_id uuid not null,
  amount numeric(20, 2) not null check (amount >= 0.50),
  currency varchar(20) not null,
  type varchar(20) not null,
  status varchar(20) not null,
  created_at timestamp not null default now()
);