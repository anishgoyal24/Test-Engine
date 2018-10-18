package com.anish.testengine.user.views;

import java.awt.Choice;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.ICommonDAO;
import com.anish.testengine.utils.ICommonUtils;
import com.anish.testengine.utils.SQLConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SelectTest extends JFrame {

	private JPanel contentPane;
	private UserDTO userDTO;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private ResultSet resultSet2;
	Choice choice = new Choice();
	private Logger logger = Logger.getLogger(SelectTest.class);
	
	private void listTest() throws ClassNotFoundException, SQLException {
		connection = null;
		preparedStatement = null;
		resultSet = null;
		try {
			logger.debug("Trying connection");
			connection = ICommonDAO.getConnection();
			preparedStatement = connection.prepareStatement(SQLConstants.GET_TEST_LIST);
			resultSet = preparedStatement.executeQuery();
			preparedStatement = connection.prepareStatement(SQLConstants.GET_COMPLETED_TEST);
			preparedStatement.setString(1, userDTO.getUsername());
			resultSet2 = preparedStatement.executeQuery();
			while(resultSet.next()) {
				boolean flag = false;
				while(resultSet2.next()) {
					if (resultSet.getString("test_code").equals(resultSet2.getString("test_code"))){
						flag = true;
						break;
					}
				}
				if (flag==false) {
					choice.add(resultSet.getString("test_code"));
				}
			}
			logger.debug("success");
		}
		finally {
			if (resultSet!=null) {
				resultSet.close();
			}
			if (resultSet2!=null) {
				resultSet2.close();
			}
			if (preparedStatement!=null) {
				preparedStatement.close();
			}
			if (connection!=null) {
				connection.close();
			}
		}
	}


	public SelectTest(UserDTO userDTO) {
		logger.debug("SelectTest design started");
		setResizable(false);
		setTitle("Select test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 148);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		choice.setBounds(136, 33, 272, 21);
		contentPane.add(choice);
		
		JLabel lblTestList = new JLabel("Test List");
		lblTestList.setFont(new Font("Dialog", Font.BOLD, 14));
		lblTestList.setBounds(27, 39, 91, 15);
		contentPane.add(lblTestList);
		
		JButton btnStartTest = new JButton("Start Test");
		btnStartTest.addActionListener(arg0->{
			try {
				TakeTest takeTest = new TakeTest(choice.getSelectedItem(), userDTO);
				dispose();
			} catch (ClassNotFoundException e) {
				logger.error(ICommonUtils.getStackTraceString(e));
				JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e) {
				logger.error(ICommonUtils.getStackTraceString(e));
				JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnStartTest.setBounds(170, 78, 114, 25);
		contentPane.add(btnStartTest);
		this.userDTO = userDTO;
		try {
			listTest();
		} catch (ClassNotFoundException e) {
			logger.error(ICommonUtils.getStackTraceString(e));
			JOptionPane.showMessageDialog(this, "Error in retrieving test list", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			logger.error(ICommonUtils.getStackTraceString(e));
			JOptionPane.showMessageDialog(this, "Error in retrieving test list", "Error", JOptionPane.ERROR_MESSAGE);
		}
		logger.debug("SelectTest design ended");
	}
}
