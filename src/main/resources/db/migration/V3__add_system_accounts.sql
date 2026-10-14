--SYSTEM
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('11111111-1111-1111-1111-111111111111', 0, 0.00, 'USD', 'ACTIVE', 0);

--CASH
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('22222222-2222-2222-2222-222222222222', -1, 0.00, 'USD', 'ACTIVE', 0);

--FEES
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('33333333-3333-3333-3333-333333333333', -2, 0.00, 'USD', 'ACTIVE', 0);

--INTEREST
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('44444444-4444-4444-4444-444444444444',-3, 0.00, 'USD', 'ACTIVE', 0);

--TAX
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('55555555-5555-5555-5555-555555555555',-4, 0.00, 'USD', 'ACTIVE', 0);

--REVERSAL
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('66666666-6666-6666-6666-666666666666', -5, 0.00, 'USD', 'ACTIVE', 0);

--CHARGEBACK
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('77777777-7777-7777-7777-777777777777',-6, 0.00, 'USD', 'ACTIVE', 0);

--CLEARING
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('88888888-8888-8888-8888-888888888888',-7, 0.00, 'USD', 'ACTIVE', 0);

--BONUS
insert into accounts (account_id, owner_id, balance, currency, status, version)
values ('99999999-9999-9999-9999-999999999999',-8, 0.00, 'USD', 'ACTIVE', 0);