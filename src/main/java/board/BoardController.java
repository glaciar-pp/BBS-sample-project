package board;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.BoardDAO;
import db.ReplyDAO;


/**
 * Servlet implementation class BoardController
 */
@WebServlet({"/board/list", "/board/search", "/board/write", "/board/update",
			 "/board/detail", "/board/delete", "/board/deleteConfirm",
			 "/board/reply"})

public class BoardController extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		BoardDAO dao = new BoardDAO();
		ReplyDAO replyDao = new ReplyDAO();
		HttpSession session = request.getSession();
		String sessionUid = (String) session.getAttribute("uid");
		session.setAttribute("menu", "board");
		
		response.setContentType("text/html; charset=utf-8");
		//중복적으로 나오는 항목은 상위에서 설정
		String title = null, content = null, files = null, uid = null;
		int bid = 0;
		Board board = null;
		RequestDispatcher rd = null;
		
		switch(action) {
		case "list":
			int page = Integer.parseInt(request.getParameter("page"));
			List<Board> list = dao.listBoard("title", "", page);
			
			session.setAttribute("currentBoardPage", page);
			int totalBoardNo = dao.getBoardCount();
			int totalPages = (int) Math.ceil(totalBoardNo / 10.);
			List<String> pageList = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++)
				pageList.add(String.valueOf(i));
			request.setAttribute("pageList", pageList);
			
			String today = LocalDate.now().toString();		// 2022-12-20
			request.setAttribute("today", today);
			request.setAttribute("boardList", list);
			rd = request.getRequestDispatcher("/board/list.jsp");
			rd.forward(request, response);
			break;
			
		case "detail":
			bid = Integer.parseInt(request.getParameter("bid"));
			uid = request.getParameter("uid");
			String option = request.getParameter("option");
			//조회수 증가 (본인이 본인 게시글을 읽거나 댓글 작성 후 조회수 증가는 제외)
			if (option == null && (!uid.equals(sessionUid))) {
				dao.increaseViewCount(bid);
			}
			board = dao.getBoardDetail(bid);
			request.setAttribute("board", board);
			List<Reply> replyList = replyDao.getReplies(bid); //이제 댓글을 뷰잉 해줄수 있게 됨
			request.setAttribute("replyList", replyList);
			
			rd = request.getRequestDispatcher("/board/detail.jsp");
			rd.forward(request, response);
			break;
			
		case "write":	
			if (request.getMethod().equals("GET")) {
				response.sendRedirect("/bbs/board/write.jsp");
			} else {
				title = request.getParameter("title");
				content = request.getParameter("content");
				files = request.getParameter("files");
				
				board = new Board(sessionUid, title, content, files);
				dao.insertBoard(board);
				response.sendRedirect("/bbs/board/list?page=1");
			}
			break;
		
		case "reply":
			content = request.getParameter("content");
			bid = Integer.parseInt(request.getParameter("bid"));
			uid = request.getParameter("uid");					//게시글 작성자, 즉 게시글의 uid
			int isMine = (uid.equals(sessionUid)) ? 1 : 0;		
			//게시글  uid와 댓글을 쓰려고 하는 사람의 uid가 같으면 isMine이 1, 그렇지 않으면 0 
			Reply reply = new Reply(content, isMine, sessionUid, bid);
			replyDao.insert(reply);
			dao.increaseReplyCount(bid);
			response.sendRedirect("/bbs/board/detail?bid=" + bid + "&uid=" + uid + "&option=DNI");
			break;
			
		case "delete":	
			bid = Integer.parseInt(request.getParameter("bid"));
			response.sendRedirect("/bbs/board/delete.jsp?bid=" + bid);
			break;
			
		case "deleteConfirm":
			bid = Integer.parseInt(request.getParameter("bid"));
			dao.deleteBoard(bid);
			response.sendRedirect("/bbs/board/list?page=" + session.getAttribute("currentBoardPage"));
			break;
			/*delete와 Confirm도 GET & POST 방식으로 하는게 정석이나 
			yes, no 정보의 정보가 끝이다보니 워낙 양이 적어 이렇게 편법(?)으로 처리가 가능함*/
			
		case "update":	//업데이트와 같이 GET & POST 방식이 정석이긴 함
			if (request.getMethod().equals("GET")) {
				bid = Integer.parseInt(request.getParameter("bid"));
				board = dao.getBoardDetail(bid);
				request.setAttribute("board", board);
				rd = request.getRequestDispatcher("/board/update.jsp"); //정보를 가져와서 view 계열에 뿌림
				rd.forward(request, response);
			} else {				// content의 양이 4000자가 넘으니 용량이 커서 POST방식을 써야함
				bid = Integer.parseInt(request.getParameter("bid"));
				uid = request.getParameter("uid");					//게시글 작성자, 즉 게시글의 uid
				title = request.getParameter("title");
				content = request.getParameter("content");
				files = request.getParameter("files");
				
				board = new Board(bid, title, content, files);
				dao.updateBoard(board);
				response.sendRedirect("/bbs/board/detail?bid=" + bid + "&uid=" + uid);
				//response.sendRedirect("/bbs/board/detail?bid=" + bid + "&uid=" + uid + "&option=DNI");
				//정보 새로 받은 후 원래 글 자리대로 감. uid 부분은 수정권한을 본인에게만 줬기 때문에 꼭 필수는 아님
			}
			break;	
			
		default: 
			System.out.println(request.getMethod() + " 잘못된 경로");
		}
	}
}
