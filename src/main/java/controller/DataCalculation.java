package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Products;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import dal.Contract;
import dao.DaoBridge;

public class DataCalculation extends HttpServlet {
	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		List<Products> cart = (List<Products>) session.getAttribute("cart");
		try {
			Contract cn=DaoBridge.getDalObject();
			Map<String,Double> hm=cn.calprice(cart);
			session.setAttribute("priceDetails", hm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
