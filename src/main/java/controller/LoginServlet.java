package controller;

import java.io.IOException;
import java.io.PrintWriter;

import dal.Contract;
import dao.DaoBridge;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Contract cn = DaoBridge.getDalObject();

			String user_name = request.getParameter("username");
			String pswd = request.getParameter("password");
			boolean flag = cn.loginverification(user_name, pswd);
			HttpSession session = request.getSession();
			if (flag == true) {
				session.setAttribute("username", user_name);

				response.sendRedirect("home.jsp");
			} else {
				response.sendRedirect("error.jsp");
			}

			if (true) {
				request.getSession().setAttribute("LOGGEDIN", "yes");
				request.getSession().setAttribute("USERNAME", user_name);
				PrintWriter out = response.getWriter();
				out.print("{\"username\": \"" + user_name + "\"}");
				out.flush();

				// response.getWriter().write("{ \"valid\": true,\"url\":\"" + previousUrl + "\"}");
			} else {
				request.getSession().setAttribute("LOGGEDIN", "no");
				response.getWriter().write("{ \"valid\": false}");
			}

			// Respond with JSON containing username
			// response.setContentType("application/json");
			// response.setCharacterEncoding("UTF-8");
			// PrintWriter out = response.getWriter();
			// out.print("{\"username\": \"" + user_name + "\"}");
			// out.flush();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
