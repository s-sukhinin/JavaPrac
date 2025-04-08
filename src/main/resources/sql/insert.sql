-- === USERS ===
INSERT INTO users (login, password, name, role) VALUES
    ('admin1', 'pass123', 'Alice Moderator', 0),
    ('admin2', 'pass123', 'Bob Moderator', 0),
    ('user1', 'pass123', 'Charlie', 1),
    ('user2', 'pass123', 'Diana', 1),
    ('user3', 'pass123', 'Eve', 1),
    ('user4', 'pass123', 'Frank', 1),
    ('user5', 'pass123', 'Grace', 1),
    ('user6', 'pass123', 'Heidi', 1),
    ('user7', 'pass123', 'Ivan', 1),
    ('user8', 'pass123', 'Judy', 1);

-- === CATEGORIES ===
INSERT INTO categories (title, description, author) VALUES
    ('General Discussion', 'Talk about anything here.', 1),
    ('Tech Talk', 'Discuss technology, programming, etc.', 2),
    ('Empty Category', 'This section is intentionally left empty.', 1);

-- === THREADS ===
-- In "General Discussion" (id = 1)
INSERT INTO threads (title, category, author) VALUES
    ('Welcome to the forum!', 1, 3),
    ('Introduce Yourself', 1, 4);

-- In "Tech Talk" (id = 2)
INSERT INTO threads (title, category, author) VALUES
    ('Best programming language?', 2, 5),
    ('Hardware setups', 2, 6),
    ('Empty Thread', 2, 7);  -- This will remain empty

-- === POSTS ===
-- For thread 1 ("Welcome to the forum!")
INSERT INTO posts (thread, author, content) VALUES
    (1, 3, 'Hello everyone! Welcome aboard.'),
    (1, 4, 'Glad to be here!'),
    (1, 5, 'Nice to meet you all.');

-- For thread 2 ("Introduce Yourself")
INSERT INTO posts (thread, author, content) VALUES
    (2, 6, 'Hi, I am Frank, I love chess and Python.'),
    (2, 7, 'Hey Frank, nice to meet you!');

-- For thread 3 ("Best programming language?")
INSERT INTO posts (thread, author, content) VALUES
    (3, 5, 'I think Rust is the future.'),
    (3, 8, 'Python all the way!'),
    (3, 9, 'JavaScript is more versatile.');

-- For thread 4 ("Hardware setups")
INSERT INTO posts (thread, author, content) VALUES
    (4, 6, 'I run a Ryzen 7 with 32GB RAM.'),
    (4, 10, 'Still using an old MacBook Pro, but it works.');

-- ===BANS===
-- Permanent ban on whole forum
INSERT INTO bans (user_id, thread, category, banned_at, banned_until, ban_reason, moderator) VALUES
    (4, NULL, NULL, NOW(), NULL, 'Spam', 1);

-- 1 week ban in Tech Talk
INSERT INTO bans (user_id, thread, category, banned_at, banned_until, ban_reason, moderator) VALUES
    (5, NULL, 2, NOW(), NOW() + INTERVAL '7 days', 'Off-topic', 1);

-- 3 days ban in Best programming language
INSERT INTO bans (user_id, thread, category, banned_at, banned_until, ban_reason, moderator) VALUES
    (6, 3, NULL, NOW(), NOW() + INTERVAL '3 days', 'Trolling', 2);

-- Expired ban in Hardware setups
INSERT INTO bans (user_id, thread, category, banned_at, banned_until, ban_reason, moderator) VALUES
    (8, 4, NULL, NOW() - INTERVAL '10 days', NOW() - INTERVAL '2 days', 'Insulting other users', 2);