package dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
public class DatabaseConnection {
	
	public Connection getConnection() throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("Db");
		String driver = bundle.getString("driver");
		String url = bundle.getString("url");
		String username = bundle.getString("username");
		String password = bundle.getString("password");

		Class.forName(driver);
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}
}
