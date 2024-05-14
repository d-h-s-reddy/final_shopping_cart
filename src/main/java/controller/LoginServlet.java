package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import dal.Contract;
import dao.DaoBridge;

public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Contract cn=DaoBridge.getDalObject();
			
			String user_name=request.getParameter("username");
			String pswd=request.getParameter("password");
			boolean flag=cn.loginverification(user_name,pswd);
			HttpSession session=request.getSession();
			if(flag==true) {
				session.setAttribute("username", user_name);
				response.sendRedirect("home.jsp");
			}else {
				response.sendRedirect("error.jsp");
			}
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
