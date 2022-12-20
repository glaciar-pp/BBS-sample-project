/* =================================
    Maven Project_221220
================================= */

-- 보드 users JOIN해서 게시글 목록 형태 보기 --
SELECT b.bid, b.uid, b.title, b.content, b.modTime, b.viewCount, 
	b.replyCount, b.files, u.uname  FROM board AS b 
	JOIN users AS u
	ON b.uid=u.uid 
	WHERE b.isDeleted=0 AND b.title LIKE '%%'
	ORDER BY bid DESC
	LIMIT 10 OFFSET 0;

-- 보드 게시글 생성 --
INSERT INTO board(bid, uid, title, content) VALUES
	('admin', '게시판 사이트 오픈', '게시판 사이트를 오늘 오픈하였습니다. 만괂부'),
	('ehkim', '게시판 오픈 축하', '멋진 사이트네요!!');

-- 댓글 생성 --
INSERT INTO reply (content, isMine, uid, bid) VALUES
	('축하합니다😀', 0, 'maria', 1001),
	('댓글입니다👍', 0, 'maria', 1002);

INSERT INTO reply (content, isMine, uid, bid) VALUES
	('감사합니다😊', 0, 'admin', 1003);

-- 보드 댓글 수, 조회 수 세팅 --
UPDATE board SET replyCount=2, viewCount =5 WHERE bid=1001;
UPDATE board SET replyCount=1, viewCount =3 WHERE bid=1002;
