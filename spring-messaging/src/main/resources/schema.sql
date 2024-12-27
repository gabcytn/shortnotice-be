CREATE TABLE users (
	id UUID NOT NULL,
	username VARCHAR(100) NOT NULL,
	password VARCHAR(75) NOT NULL,
	role VARCHAR(10) DEFAULT 'user',
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_ak UNIQUE (username)
);

CREATE TABLE groups (
	id SERIAL NOT NULL,
	CONSTRAINT groups_pk PRIMARY KEY (id)
);

CREATE TABLE group_members (
	id INT NOT NULL,
	user_id UUID NOT NULL,
	CONSTRAINT group_members_pk PRIMARY KEY (id, user_id),
	CONSTRAINT group_members_id_fk FOREIGN KEY(id) REFERENCES groups(id)
	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT group_members_user_id_fk FOREIGN KEY(user_id) REFERENCES users(id)
	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE messages (
	id SERIAL NOT NULL,
	sender_id UUID NOT NULL,
	receiver_id UUID,
	group_id INT,
	message TEXT NOT NULL,
	sent_at TIMESTAMP DEFAULT NOW(),
	CONSTRAINT messages_pk PRIMARY KEY (id),
	CONSTRAINT messages_sender_fk FOREIGN KEY (sender_id) REFERENCES users(id)
	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT messages_receiver_fk FOREIGN KEY (receiver_id) REFERENCES users(id)
  	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT messages_group_fk FOREIGN KEY(group_id) REFERENCES groups(id)
	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT message_destination_chk CHECK (
		(receiver_id IS NULL AND group_id IS NOT NULL) OR
		(receiver_id IS NOT NULL AND group_id IS NULL)
	)
);
