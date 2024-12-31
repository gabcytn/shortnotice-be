CREATE TABLE users (
	id UUID NOT NULL,
	username VARCHAR(100) NOT NULL,
	password VARCHAR(75) NOT NULL,
	profile_pic VARCHAR (255) NOT NULL 
		DEFAULT 'https://res.cloudinary.com/dfvwoewft/image/upload/v1733883089/default-profile_r4f6xf.jpg',
	role VARCHAR(10) NOT NULL DEFAULT 'user',
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
		REFERENCES users(id),
	CHECK (sender <> recipient)
);

CREATE TABLE blocks (
	blocked UUID NOT NULL,
	blocker UUID NOT NULL,
	CONSTRAINT blocked_fk FOREIGN KEY (blocked)
		REFERENCES users(id),
	CONSTRAINT blocker_fk FOREIGN KEY (blocker)
		REFERENCES users(id)
);

CREATE OR REPLACE FUNCTION one_directional_message_request() 
RETURNS TRIGGER AS $$
BEGIN
	IF EXISTS (
		SELECT 1 FROM message_requests
		WHERE (NEW.sender = recipient
		AND NEW.recipient = sender)
	) THEN
		RAISE EXCEPTION 'Message request should be one-directional';
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER message_requests_tr
BEFORE INSERT OR UPDATE ON message_requests
FOR EACH ROW
EXECUTE FUNCTION one_directional_message_request();
