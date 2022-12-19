/* Drop Tables */

DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS users;

/* Create Tables */

CREATE TABLE board
(
	bid int NOT NULL AUTO_INCREMENT,
	uid varchar(20) NOT NULL,
	title varchar(128) NOT NULL,
	content varchar(4096),
	modTime datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
	viewCount int DEFAULT 0 NOT NULL,
	replyCount int DEFAULT 0 NOT NULL,
	isDeleted int DEFAULT 0 NOT NULL,
	files varchar(400),
	PRIMARY KEY (bid)
);


CREATE TABLE reply
(
	rid int NOT NULL AUTO_INCREMENT,
	content varchar(128) NOT NULL,
	regDate datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
	isMine int DEFAULT 0 NOT NULL,
	uid varchar(20) NOT NULL,
	bid int NOT NULL,
	PRIMARY KEY (rid)
);


CREATE TABLE users
(
	uid varchar(20) NOT NULL,
	pwd char(60) NOT NULL,
	uname varchar(20) NOT NULL,
	email varchar(32),
	regDate date DEFAULT (CURRENT_DATE),
	isDeleted int DEFAULT 0 NOT NULL,
	PRIMARY KEY (uid)
);



/* Create Foreign Keys */

ALTER TABLE reply
	ADD FOREIGN KEY (bid)
	REFERENCES board (bid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE board
	ADD FOREIGN KEY (uid)
	REFERENCES users (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE reply
	ADD FOREIGN KEY (uid)
	REFERENCES users (uid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;