CREATE TABLE users (
	id UUID NOT NULL,
	username VARCHAR(100) NOT NULL,
	password VARCHAR(75) NOT NULL,
	role VARCHAR(10) DEFAULT 'user',
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_ak UNIQUE (username)
);

CREATE TABLE conversations (
	id SERIAL NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(2),
	CONSTRAINT conversations_pk PRIMARY KEY (id)
);

CREATE TABLE conversation_members (
	conversation_id INT NOT NULL,
	user_id UUID NOT NULL,
	CONSTRAINT conversation_id_fk FOREIGN KEY (conversation_id)
	REFERENCES conversations(id),
	CONSTRAINT user_id_fk FOREIGN KEY (user_id)
	REFERENCES users(id)
);

CREATE TABLE messages (
	id SERIAL NOT NULL,
	conversation_id INT NOT NULL,
	sender_id UUID NOT NULL,
	message TEXT NOT NULL,
	sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(2),
	CONSTRAINT messages_pk PRIMARY KEY(id),
	CONSTRAINT messages_conversation_id_fk FOREIGN KEY (conversation_id)
	REFERENCES conversations(id),
	CONSTRAINT messages_sender_id FOREIGN KEY (sender_id)
	REFERENCES users(id)
);

CREATE TABLE message_requests (
	id SERIAL NOT NULL,
	sender UUID NOT NULL,
	recipient UUID NOT NULL,
	message TEXT NOT NULL,
	sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(2),
	CONSTRAINT message_requests_pk PRIMARY KEY(id),
	CONSTRAINT sender_fk FOREIGN KEY (sender)
		REFERENCES users(id),
	CONSTRAINT recipient_fk FOREIGN KEY (recipient)
		REFERENCES users(id)
);

CREATE TABLE blocks (
	blocked UUID NOT NULL,
	blocker UUID NOT NULL,
	CONSTRAINT blocked_fk FOREIGN KEY (blocked)
		REFERENCES users(id),
	CONSTRAINT blocker_fk FOREIGN KEY (blocker)
		REFERENCES users(id)
);

