CREATE TABLE users (
	id UUID NOT NULL,
	username VARCHAR(100) NOT NULL,
	password VARCHAR(75) NOT NULL,
	role VARCHAR(10) DEFAULT 'user',
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_ak UNIQUE (username)
);

CREATE TABLE channels (
	id BIGINT NOT NULL,
	name VARCHAR(255) NOT NULL,
	CONSTRAINT channels_pk PRIMARY KEY (id)
);

CREATE TABLE subscribers (
	channel BIGINT NOT NULL,
	user_id UUID NOT NULL,
	CONSTRAINT subscribers_ak UNIQUE (channel, user_id),
	CONSTRAINT subsribers_user_fk FOREIGN KEY (user_id) REFERENCES users(id) 
	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT subscribers_channel_fk FOREIGN KEY (channel) REFERENCES channels(id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE messages (
	id BIGINT NOT NULL,
	user_id UUID NOT NULL,
	channel BIGINT NOT NULL,
	message TEXT NOT NULL,
	sent_at TIMESTAMP DEFAULT NOW(),
	CONSTRAINT messages_pk PRIMARY KEY (id),
	CONSTRAINT messages_user_fk FOREIGN KEY (user_id) REFERENCES users(id)
	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT messages_channel_fk FOREIGN KEY (channel) REFERENCES channels(id)
	ON DELETE CASCADE ON UPDATE CASCADE
);
