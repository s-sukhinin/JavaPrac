DROP TABLE IF EXISTS attachments CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS bans CASCADE;
DROP TABLE IF EXISTS threads CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       login TEXT NOT NULL UNIQUE,
                       password TEXT NOT NULL,
                       name TEXT NOT NULL,
                       date_of_registration TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       role INTEGER NOT NULL
);

CREATE TABLE categories (
                            category_id SERIAL PRIMARY KEY,
                            title TEXT NOT NULL,
                            description TEXT,
                            author INT REFERENCES users(user_id) ON DELETE SET NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE threads (
                         thread_id SERIAL PRIMARY KEY,
                         title TEXT NOT NULL,
                         category INT NOT NULL REFERENCES categories(category_id) ON DELETE CASCADE,
                         author INT REFERENCES users(user_id) ON DELETE SET NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE posts (
                       post_id SERIAL PRIMARY KEY,
                       thread INT NOT NULL REFERENCES threads(thread_id) ON DELETE CASCADE,
                       author INT REFERENCES users(user_id) ON DELETE SET NULL,
                       content TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attachments (
                             attach_id SERIAL PRIMARY KEY,
                             post INT NOT NULL REFERENCES posts(post_id) ON DELETE CASCADE,
                             file_name TEXT,
                             path_to_file TEXT,
                             uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bans (
                      ban_id SERIAL PRIMARY KEY,
                      user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
                      thread INT REFERENCES threads(thread_id) ON DELETE CASCADE ,
                      category INT REFERENCES categories(category_id) ON DELETE CASCADE ,
                      banned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      banned_until TIMESTAMP DEFAULT NULL,
                      ban_reason TEXT,
                      moderator INT REFERENCES users(user_id) ON DELETE SET NULL
);
