package misc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginCheckFilter
 */
@WebFilter({"/board/*", "/user/list", "/user/logout",
	   "/user/update", "/user/delete", "/user/deleteConfirm" })
public class LoginCheckFilter extends HttpFilter implements Filter {
       

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		request.setCharacterEncoding("utf-8");
		String uid = (String) session.getAttribute("uid");
		if (uid == null || uid.equals("")) {
			httpResponse.sendRedirect("/bbs/user/login");
			return;
		}
		
		chain.doFilter(request, response);
	}

}
//로그인 하지 않으면 들어올 수 없게 만드는 필터