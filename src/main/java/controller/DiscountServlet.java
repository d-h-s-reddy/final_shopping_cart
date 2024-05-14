package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Discount;

import java.io.IOException;
import java.util.ArrayList;

import dal.Contract;
import dao.DaoBridge;

public class DiscountServlet extends HttpServlet {
	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Contract cn=DaoBridge.getDalObject();
			ArrayList<Discount> dis=cn.getDiscount();
			request.setAttribute("discount", dis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
