-- Keyspace creation
CREATE KEYSPACE IF NOT EXISTS posts_keyspace
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

-- Table creation
CREATE TABLE IF NOT EXISTS posts_keyspace.posts (
    user_id BIGINT,
    post_id UUID,
    content TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY ((user_id), created_at, post_id)
) WITH CLUSTERING ORDER BY (created_at DESC);
