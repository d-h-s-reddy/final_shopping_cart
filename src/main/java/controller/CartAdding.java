package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Products;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class CartAdding extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		List<Products> cart = (List<Products>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute("cart", cart);
		}
		Products p = new Products();
		System.out.println(request.getParameter("prod[product_id]"));
		p.setProduct_id(Integer.parseInt(request.getParameter("prod[product_id]")));
		p.setProd_title(request.getParameter("prod[prod_title]"));
		p.setImage(request.getParameter("prod[image]"));
		p.setShipping_charge(Integer.parseInt(request.getParameter("prod[shipping_charge]")));
		p.setProd_price(Integer.parseInt(request.getParameter("prod[prod_price]")));
		p.setProd_mrp(Integer.parseInt(request.getParameter("prod[prod_mrp]")));
		p.setQuantity(1);
		p.setGst(Double.parseDouble(request.getParameter("prod[gst]")));
	    cart.add(p);

		response.setContentType("text/plain");
		response.getWriter().write("Product added to cart successfully");

	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		List<Products> cart = (List<Products>) session.getAttribute("cart");
		Gson gson = new Gson();
		String data = gson.toJson(cart);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(data);
	}


}
