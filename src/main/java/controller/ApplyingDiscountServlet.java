package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import dal.Contract;
import dao.DaoBridge;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Products;

public class ApplyingDiscountServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		double dcpn_value = Double.parseDouble(request.getParameter("discount"));
		HttpSession session = request.getSession();
		List<Products> cart = (List<Products>) session.getAttribute("cart");
		Map<String, Double> hm = (Map<String, Double>) session.getAttribute("priceDetails");
		try {
			Contract cn = DaoBridge.getDalObject();
			ArrayList<Object> hm1 = cn.calpriceafterdiscount(cart, hm, dcpn_value);

			session.setAttribute("gstafterdiscount", hm1);
			Gson gson = new Gson();
			String json = gson.toJson(hm1);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			// Write JSON to response output stream
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
