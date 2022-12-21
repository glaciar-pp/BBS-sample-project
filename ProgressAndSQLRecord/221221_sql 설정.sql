/* =================================
    Maven Project_221221
================================= */

--조회수 증가--
UPDATE board SET viewCount=viewCount+1 WHERE bid=?;

--댓글 확인(단순)--
SELECT * FROM reply WHERE bid=?;

-- 보드 users JOIN해서 댓글 목록 형태 보기 --
SELECT r.rid, r.content, r.regDate, r.isMine, r.uid, r.bid, u.uname  
	FROM reply AS r 
	JOIN users AS u
	ON r.uid=u.uid 
	WHERE bid=?;

-- 댓글 쓰는 기능 --
INSERT INTO reply(content, isMine, uid, bid) VALUES (?, ?, ?, ?);

-- 댓글 수 증가 --
UPDATE board SET replyCount=replyCount+1 WHERE bid=?;

-- 댓글 삭제 --
UPDATE board SET isDeleted=1 WHERE bid=?;

-- 게시글 수정 --
UPDATE board SET title=?, content=?, modTime=NOW(), files=? WHERE bid=?;