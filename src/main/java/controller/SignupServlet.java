package controller;

import java.io.IOException;

import dal.Contract;
import dao.DaoBridge;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Contract cn = DaoBridge.getDalObject();

			String user_name = request.getParameter("username");
			String pswd = request.getParameter("password");
			// Create a new user object
			User newUser = new User();
			newUser.setUsername(user_name);
			newUser.setPassword(pswd);
			cn.addUser(newUser);
			HttpSession session = request.getSession();
			response.sendRedirect("Login.jsp");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.sendRedirect("error.jsp");
			e.printStackTrace();
		}
	}
}