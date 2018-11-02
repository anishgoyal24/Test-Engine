package com.anish.testengine.utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public interface ICommonDAO {
	Logger logger = Logger.getLogger(ICommonDAO.class);
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
	    logger.debug("Trying connection");
		Connection connection = null;
		ResourceBundle rBundle = ResourceBundle.getBundle("config");
		Class.forName(rBundle.getString("driver"));
		connection = DriverManager.getConnection(rBundle.getString("url"), rBundle.getString("userid"), rBundle.getString("password"));
		return connection;
	}
}
