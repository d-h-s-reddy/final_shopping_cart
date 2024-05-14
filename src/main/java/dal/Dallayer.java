package dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dao.DatabaseConnection;
import models.Discount;
import models.Products;

public class Dallayer implements Contract {
	DatabaseConnection c = new DatabaseConnection();
	Connection con = c.getConnection();

	public Dallayer() throws Exception {
	}

	@Override
	public boolean loginverification(String user_name, String pswd) throws Exception {
		String str = "select * from j_user_credentials where username=? and password=?";
		PreparedStatement ps = con.prepareStatement(str);
		ps.setString(1, user_name);
		ps.setString(2, pswd);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ArrayList<String> productCategories() throws SQLException {
		String str = "Select * from j_productcategories";
		PreparedStatement ps = con.prepareStatement(str);
		ResultSet rs = ps.executeQuery();
		ArrayList<String> al = new ArrayList<>();
		while (rs.next()) {
			al.add(rs.getString("prct_title"));
		}
		return al;
	}

	@Override
	public ArrayList<Products> getProducts(String selcat, String price, int page) throws Exception {
		ArrayList<Products> al = null;
		String str = null;
		if (selcat.equals("All")) {
			str = "select * from j_products p inner join j_productstock ps on p.prod_id=ps.prod_id inner join j_hsncodes h on h.hsnc_id=p.prod_hsnc_id "
					+ "order by p.prod_id limit ? offset ?";
			if (!price.equals("All")) {
				str += "where true " + addstring(price);
			}
		} else {
			str = "select * from j_products p inner join j_productstock ps on p.prod_id=ps.prod_id inner join j_hsncodes h on h.hsnc_id=p.prod_hsnc_id "
					+ "where p.prod_prct_id in (\r\n"
					+ "	select prct_id from j_productcategories where prct_title=?)  ";

		}
		PreparedStatement pp;
		if (!selcat.equals("All")) {
			if (!price.equals("All")) {

				str += addstring(price);

			}
			str += " limit ? offset ?";
			pp = con.prepareStatement(str);
			pp.setString(1, selcat);
			pp.setInt(2, 6);
			pp.setInt(3, page * 6);
		} else {

			pp = con.prepareStatement(str);
			pp.setInt(1, 6);
			pp.setInt(2, page * 6);
		}

		ResultSet rs = pp.executeQuery();
		al = new ArrayList<>();
		while (rs.next()) {
			Products p = new Products();
			p.setProduct_id(rs.getInt("prod_id"));
			p.setProd_title(rs.getString("prod_title"));
			p.setImage(rs.getString("prod_image"));
			p.setShipping_charge(rs.getInt("prod_shipping_charge"));
			p.setProd_price(rs.getInt("prod_price"));
			p.setProd_mrp(rs.getInt("prod_mrp"));
			p.setGst(rs.getDouble("hsnc_gstc_percentage"));
			al.add(p);
		}
		return al;
	}

	private String addstring(String price) {
		String str = "";
		int pr = Integer.parseInt(price);
		if (pr == 0) {
			str += "and prod_price>=0 and prod_price<=500";
		} else if (pr == 500) {
			str += "and prod_price>=500 and prod_price<=1000";
		} else if (pr == 1000) {
			str += "and prod_price>=1000 and prod_price<=10000";
		} else {
			str += "and prod_price>=10000 and prod_price<=100000";
		}

		return str;

	}

	@Override
	public ArrayList<String> getNonserviceable(ArrayList<Products> cart, int pincode) throws SQLException {
		ArrayList<Integer> ids = new ArrayList();
		for (Products p : cart) {
			int id = p.getProduct_id();
			ids.add(id);
		}
		ArrayList<String> name = new ArrayList();
		CallableStatement stmt = con.prepareCall("select * from j_get_service(?,?)");
		String prodids = (String) ids.stream().map(Object::toString).collect(Collectors.joining(","));
		stmt.setString(1, prodids);
		stmt.setInt(2, pincode);
		ResultSet rs = stmt.executeQuery();
		ids.clear();
		if (!rs.next()) {
			name = null;
		} else {
			String productname = rs.getString(1);
			name.add(productname);
			while (rs.next()) {
				productname = rs.getString(1);// Assuming the result set contains a single column of integers
				name.add(productname);
			}
		}
		return name;
	}

	@Override
	public List<Products> getupdatecart(List<Products> cart, int p_id, int quantity) {
		for (Products product : cart) {
			if (product.getProduct_id() == p_id) {
				if (quantity == 0) {
					cart.remove(product);
				} else {
					product.setQuantity(quantity);

				}
				break;
			}

		}
		return cart;
	}

	@Override
	public Map<String, Double> calprice(List<Products> cart) {
		double totalamount = 0;
		double t;
		double gst = 0;
		double shippingcharge = 0;
		for (Products p : cart) {
			t = (p.getProd_price() * (double) (100 / (double) (100 + p.getGst()))) * p.getQuantity();
			totalamount += t;
			gst += (p.getProd_price() - t);
			System.out.println("gst" + gst);
			shippingcharge += p.getShipping_charge();
		}
		Map<String, Double> result = new HashMap<>();
		result.put("totalAmount", totalamount);
		result.put("gst", gst);
		result.put("shipping_charge", shippingcharge);
		return result;
	}

	@Override
	public ArrayList<Discount> getDiscount() throws SQLException {
		String str = "select * from j_discountcoupon";
		PreparedStatement ps = con.prepareStatement(str);
		ArrayList<Discount> dis = new ArrayList<Discount>();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Discount d = new Discount();
			d.setDcpn_id(rs.getInt("dcpn_id"));
			d.setDcpn_title(rs.getString("dcpn_code"));
			d.setDcpn_value(rs.getInt("dcpn_value"));
			dis.add(d);
		}
		return dis;
	}

	@Override
	public ArrayList<Object> calpriceafterdiscount(List<Products> cart, Map<String, Double> hm, double dcpn_value) {
		System.out.println(dcpn_value);
		double totalamount = 0;
		double t = 0;
		double gst = 0;
		double pg = 0;
		double total_price = caltotalamount(cart);
		double total_price_afdis = total_price - (double) (total_price * (double) (dcpn_value / 100));
		System.out.println(total_price_afdis);
		double total_discount_amount = total_price * (double) (dcpn_value / 100);
		System.out.println((double) (dcpn_value / 100));

		ArrayList<Double> productDetails;
		Map<Integer, ArrayList<Double>> datapricegst = new HashMap<>();

		for (Products p : cart) {
			double proprotionalamount = (double) p.getProd_price() / total_price;
			double discount_price_inclusive = proprotionalamount * total_price_afdis;
			double productprice = (double) (discount_price_inclusive / (1 + (double) (p.getGst() / 100)));
			double p_gst = discount_price_inclusive - productprice;

			t = productprice * p.getQuantity();
			pg = p_gst * p.getQuantity();

			productDetails = new ArrayList<>();
			productDetails.add(t);
			productDetails.add(pg);
			datapricegst.put(p.getProduct_id(), productDetails);

			totalamount += t;
			gst += pg;
		}
		hm.put("totalAmount", totalamount);
		hm.put("gst", gst);
		ArrayList<Object> al = new ArrayList<>();
		al.add(datapricegst);
		al.add(hm);

		return al;
	}

	private double caltotalamount(List<Products> cart) {
		double t = 0;
		for (Products p : cart) {
			t += p.getProd_price() * p.getQuantity();
		}
		return t;
	}

	@Override
	public ArrayList<Object> calshippinggst(List<Products> cart) throws SQLException {
		double shippingcharge = calshippingcharge(cart);
		double total_price = caltotalamount(cart);
		double shipdiscount = 0;

		String str = "select * from j_shippingdiscount where ? BETWEEN total_price_from and total_price_to";
		PreparedStatement ps = con.prepareStatement(str);
		ps.setDouble(1, total_price);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			shipdiscount = (double) rs.getInt("discount");
		}

		double shippingchargeafdiscount = shippingcharge - (shippingcharge * (double) (shipdiscount / 100));

		System.out.println("after discount" + shippingchargeafdiscount);

		double t = 0;
		double pg = 0;
		double totalshippingamount = 0;
		double totalshippinggst = 0;

		ArrayList<Double> productshippingdetails;
		Map<Integer, ArrayList<Double>> shippingpricegst = new HashMap<>();
		for (Products p : cart) {
			double proprotionalamount = (double) p.getShipping_charge() / shippingcharge;
			double discount_price_inclusive = proprotionalamount * shippingchargeafdiscount;
			double p_gst = (double) (discount_price_inclusive * (double) (p.getGst() / 100));

			t = discount_price_inclusive * p.getQuantity();
			pg = p_gst * p.getQuantity();

			productshippingdetails = new ArrayList<>();
			productshippingdetails.add(t);
			productshippingdetails.add(pg);
			shippingpricegst.put(p.getProduct_id(), productshippingdetails);

			totalshippingamount += t;
			totalshippinggst += pg;
		}
		Map<String, Double> results = new HashMap<>();
		results.put("TotalShippingCharge", totalshippingamount);
		results.put("ShippingGst", totalshippinggst);
		ArrayList<Object> al = new ArrayList<>();
		al.add(shippingpricegst);
		al.add(results);
		return al;
	}

	private double calshippingcharge(List<Products> cart) {
		double s = 0;
		for (Products p : cart) {
			s += p.getShipping_charge() * p.getQuantity();
		}
		return s;
	}

}