package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet implementation class UserServiceController
 */
@WebServlet({ "/user/list", "/user/login", "/user/logout",
			  "/user/register", "/user/update", "/user/delete", "/user/deleteConfirm" })
public class UserController extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		UserDAO dao = new UserDAO();
		HttpSession session = request.getSession();
		session.setAttribute("menu", "user");
		
		response.setContentType("text/html; charset=utf-8");
		String uid = null, pwd = null, pwd2 = null, uname = null, email = null;
		User u = null;
		RequestDispatcher rd = null;
		switch(action) {
		case "list":
			int page = Integer.parseInt(request.getParameter("page"));
			List<User> list = dao.listUsers(page);
			
			session.setAttribute("currentUserPage", page);
			int totalUsers = dao.getUserCount();
			int totalPages = (int) Math.ceil(totalUsers / 10.);
			List<String> pageList = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++)
				pageList.add(String.valueOf(i));
			request.setAttribute("pageList", pageList);
			
			request.setAttribute("userList", list);
			rd = request.getRequestDispatcher("/WEB-INF/view/user/list.jsp");
			rd.forward(request, response);
			break;
		case "login":
			if (request.getMethod().equals("GET")) {
				rd = request.getRequestDispatcher("/WEB-INF/view/user/login.jsp");
				rd.forward(request, response);
			} else {
				uid = request.getParameter("uid");
				pwd = request.getParameter("pwd");
				u = dao.getUserInfo(uid);
				if (u.getUid() != null) {		// uid 가 존재
					if (BCrypt.checkpw(pwd, u.getPwd())) {
						// System.out.println(u.getUid() + ", " + u.getUname());
						session.setAttribute("uid", u.getUid());
						session.setAttribute("uname", u.getUname());
						
						// Welcome message
						request.setAttribute("msg", u.getUname() + "님 환영합니다.");
						request.setAttribute("url", "/bbs/board/list?p=1&f=&q=");
						rd = request.getRequestDispatcher("/WEB-INF/view/user/alertMsg.jsp");
						rd.forward(request, response);
					} else {
						// 재 로그인 페이지
						request.setAttribute("msg", "잘못된 패스워드 입니다. 다시 입력하세요.");
						request.setAttribute("url", "/bbs/user/login");
						rd = request.getRequestDispatcher("/WEB-INF/view/user/alertMsg.jsp");
						rd.forward(request, response);
					}
				} else {				// uid 가 없음
					// 회원 가입 페이지로 안내
					request.setAttribute("msg", "회원 가입 페이지로 이동합니다.");
					request.setAttribute("url", "/bbs/user/register");
					rd = request.getRequestDispatcher("/WEB-INF/view/user/alertMsg.jsp");
					rd.forward(request, response);
				}
			}
			break;
		case "logout":
			session.invalidate();
			response.sendRedirect("/bbs/user/login");
			break;
		case "register":
			if (request.getMethod().equals("GET")) {
				rd = request.getRequestDispatcher("/WEB-INF/view/user/register.jsp");
				rd.forward(request, response);
			} else {
				uid = request.getParameter("uid").strip();
				pwd = request.getParameter("pwd").strip();
				pwd2 = request.getParameter("pwd2").strip();
				uname = request.getParameter("uname").strip();
				email = request.getParameter("email").strip();
				if (pwd.equals(pwd2)) {
					u = new User(uid, pwd, uname, email);
					dao.registerUser(u);
					response.sendRedirect("/bbs/user/login");
				} else {
					request.setAttribute("msg", "패스워드 입력이 잘못되었습니다.");
					request.setAttribute("url", "/bbs/user/register");
					rd = request.getRequestDispatcher("/WEB-INF/view/user/alertMsg.jsp");
					rd.forward(request, response);
				}
			}
			break;
		case "update":
			if (request.getMethod().equals("GET")) {
				uid = request.getParameter("uid");
				u = dao.getUserInfo(uid);
				request.setAttribute("user", u);
				rd = request.getRequestDispatcher("/WEB-INF/view/user/update.jsp");
				rd.forward(request, response);
			} else {								// POST
				uid = request.getParameter("uid");
				pwd = request.getParameter("pwd").strip();
				pwd2 = request.getParameter("pwd2").strip();
				uname = request.getParameter("uname").strip();
				email = request.getParameter("email").strip();
				//패스워드를 입력하지 않은 경우(패스워드 제외하고 업데이트)
				if (pwd == null || pwd.equals("")) {
					u = new User(uid, uname, email);
					dao.updateUser(u);
					session.setAttribute("uname", uname);
					response.sendRedirect("/bbs/user/list?page=" + session.getAttribute("currentUserPage"));				
				//패스워드를 입력한 경우(확인과 일치하는 경우)
				} else if(pwd.equals(pwd2)) {
					u = new User(uid, pwd, uname, email);
					dao.updateUserWithPassword(u);
					session.setAttribute("uname", uname);
					response.sendRedirect("/bbs/user/list?page=" + session.getAttribute("currentUserPage"));	
				} else {//패스워드를 입력한 경우(잘못 입력한 경우)
					request.setAttribute("msg", "패스워드 입력이 잘못되었습니다.");
					request.setAttribute("url", "/bbs/user/update?uid=" + uid);
					rd = request.getRequestDispatcher("/WEB-INF/view/user/alertMsg.jsp");
					rd.forward(request, response);
				}				
			}
			break;
		case "delete":
			uid = request.getParameter("uid");
			rd = request.getRequestDispatcher("/WEB-INF/view/user/delete.jsp?uid=" + uid);
			rd.forward(request, response);
			break;
		case "deleteConfirm":
			uid = request.getParameter("uid");
			dao.deleteUser(uid);
			response.sendRedirect("/bbs/user/list?page=" + session.getAttribute("currentUserPage"));
			break;
		default:
			System.out.println(request.getMethod() + " 잘못된 경로");
		}
	}

}