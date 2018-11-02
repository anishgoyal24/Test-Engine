package com.anish.testengine.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.anish.testengine.user.dto.RightDTO;
import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.*;
import org.apache.log4j.Logger;


public class UserDAO {
	private Logger logger = Logger.getLogger(UserDAO.class);
	
	public UserDTO doLogin(String username, String password) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		UserDTO userDTO = null;
		ArrayList<RightDTO> rights = new ArrayList<>();
		try {
			logger.debug("Trying to establish connection and get user data");
			connection = ICommonDAO.getConnection();
			pStatement = connection.prepareStatement(SQLConstants.LOGIN_SQL);
			pStatement.setString(1, username);
			pStatement.setString(2, password);
			resultSet = pStatement.executeQuery();
			while(resultSet.next()) {
				if (userDTO==null) {
					logger.debug("Creating userDTO object");
					userDTO = new UserDTO();
					userDTO.setUsername(resultSet.getString("userid"));
					userDTO.setPassword(resultSet.getString("password"));
					userDTO.setRole(resultSet.getString("rolename"));
					userDTO.setRights(rights);
				}
				RightDTO rightDTO = new RightDTO(resultSet.getString("rightname"), resultSet.getString("screen"));
				rights.add(rightDTO);
			}
		}
		finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (pStatement != null) {
				pStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return userDTO;
		
	}
}
