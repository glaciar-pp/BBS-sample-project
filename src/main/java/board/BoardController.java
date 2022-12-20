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


/**
 * Servlet implementation class BoardController
 */
@WebServlet({"/board/list", "/board/search", "/board/write", "/board/update",
			 "/board/detail", "/board/delete", "/board/deleteConfirm"})

public class BoardController extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		BoardDAO dao = new BoardDAO();
		//ReplyDAO rdao = new ReplyDAO();
		HttpSession session = request.getSession();
		String uid = (String) session.getAttribute("uid");
		session.setAttribute("menu", "board");
		
		response.setContentType("text/html; charset=utf-8");
		
		String title = null, content = null, files = null;
		Board board = null;
		RequestDispatcher rd = null;
		
		switch(action) {
		case "list":
			int page = Integer.parseInt(request.getParameter("page"));
			List<Board> list = dao.listUsers("title", "", page);
			
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
			int bid = Integer.parseInt(request.getParameter("bid"));
			board = dao.getBoardDetail(bid);
			request.setAttribute("board", board);
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
				
				board = new Board(uid, title, content, files);
				dao.insert(board);
				response.sendRedirect("/bbs/board/list?page=1");
			}
			break;
			
			
		default: 
			System.out.println(request.getMethod() + " 잘못된 경로");
		}
	}
}
