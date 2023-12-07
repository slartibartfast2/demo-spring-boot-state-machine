insert
into
    accounts
(balance, created_at, created_by, deleted_at, external_id, name, updated_at, updated_by, version, id)
values
    (75, now(), null, null, '5dc7fe93-bf11-480a-a33e-54eaf49e88cb', 'stanley kubrick', now(), null, 0, default),
    (0, now(), null, null, '5e1b4c42-c24f-47cb-af3f-b8c3cc04a055', 'akira kurosawa', now(), null, 0, default);

insert
into
    cards
(card_limit, created_at, created_by, deleted_at, external_id, account_external_id, card_token, updated_at, updated_by, version, id)
values
    (10000, now(), null, null, '5dc7fe93-bf11-480a-a33e-54eaf49e88cb', '5dc7fe93-bf11-480a-a33e-54eaf49e88cb', 'test-card', now(), null, 0, default);

insert
into
    payments
(account_external_id, amount, created_at, created_by, deleted_at, external_id, state, updated_at, updated_by, version, id)
values
    ('5dc7fe93-bf11-480a-a33e-54eaf49e88cb', 100, DATEADD(DAY, -2, CURRENT_DATE), null, null, '3d1c4db8-6ef4-431f-8006-17599583d427', 'AUTH', now(), null, 0, default),
    ('5dc7fe93-bf11-480a-a33e-54eaf49e88cb', 25, DATEADD(DAY, -1, CURRENT_DATE), null, null, '4e1c4db8-6ef4-431f-8006-17599583d427', 'AUTH', now(), null, 0, default);