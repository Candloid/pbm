package com.concordia.personalBudgetManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
	
	// Create a variable for the connection string.
	private String dbName = "pbm";
	private String serverip = "localhost";
	private String serverport="1433";
    private String connectionUrl = "jdbc:sqlserver://"+serverip+"\\SQLEXPRESS:"+serverport+";databaseName="+dbName+";user=javaUser;password=javaUser;";
    private String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	public DatabaseManager(String connectionUrl) {this.connectionUrl = connectionUrl;}

	public DatabaseManager() {}

	public ResultSet sqlRequest(Connection con, String sqlQuery) {
		ResultSet rs = null;
		try {
			System.out.println("DEBUG: INFO - Invoking SQL query: [" + sqlQuery + "]");
			rs = con.createStatement().executeQuery(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public void sqlOrder(Connection con, String sqlOrder) {
		try {
			System.out.println("DEBUG: INFO - Invoking SQL order: [" + sqlOrder + "]");
			con.createStatement().execute(sqlOrder);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public Connection sqlConnect() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(connectionUrl);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		return con;
	}
}