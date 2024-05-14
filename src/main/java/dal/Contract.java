package dal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Discount;
import models.Products;
import models.User;

public interface Contract {
	boolean loginverification(String user_name, String pswd) throws Exception;

	// void addUser1(User User) throws Exception;

	ArrayList<String> productCategories() throws SQLException;

	ArrayList<Products> getProducts(String selcat, String price, int page) throws Exception;

	ArrayList<String> getNonserviceable(ArrayList<Products> cart, int pincode) throws SQLException;

	List<Products> getupdatecart(List<Products> cart, int p_id, int quantity);

	Map<String, Double> calprice(List<Products> cart);

	ArrayList<Discount> getDiscount() throws SQLException;

	ArrayList<Object> calpriceafterdiscount(List<Products> cart, Map<String, Double> hm, double dcpn_value);

	ArrayList<Object> calshippinggst(List<Products> cart) throws SQLException;

	void addUser(User user) throws Exception;

}
