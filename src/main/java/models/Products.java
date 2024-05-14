package models;

public class Products {
	int product_id;
	String prod_title;
	int prod_hsn;
	String image;
	double shipping_charge;
	int prod_price;
	int prod_mrp;
	int quantity;
	double gst;

	public double getShipping_charge() {
		return shipping_charge;
	}

	public void setShipping_charge(double shipping_charge) {
		this.shipping_charge = shipping_charge;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getProd_price() {
		return prod_price;
	}

	public void setProd_price(int prod_price) {
		this.prod_price = prod_price;
	}

	public int getProd_mrp() {
		return prod_mrp;
	}

	public void setProd_mrp(int prod_mrp) {
		this.prod_mrp = prod_mrp;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getProd_title() {
		return prod_title;
	}

	public void setProd_title(String prod_title) {
		this.prod_title = prod_title;
	}

	public int getProd_hsn() {
		return prod_hsn;
	}

	public void setProd_hsn(int prod_hsn) {
		this.prod_hsn = prod_hsn;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
