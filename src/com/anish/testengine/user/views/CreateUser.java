package com.anish.testengine.user.views;

import java.awt.Choice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.anish.testengine.utils.ICommonDAO;
import com.anish.testengine.utils.SQLConstants;

public class CreateUser extends JFrame {

	private JPanel contentPane;
	private JTextField textName;
	Connection connection;
	PreparedStatement preparedStatement;
	PreparedStatement preparedStatement2;
	Choice choice = new Choice();
	
	public CreateUser() {
		connection = null;
		setTitle("Add User");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 369, 213);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(36, 30, 97, 15);
		contentPane.add(lblUsername);
		
		JLabel lblRole = new JLabel("Role");
		lblRole.setBounds(36, 68, 66, 15);
		contentPane.add(lblRole);
		
		textName = new JTextField();
		textName.setBounds(126, 28, 186, 19);
		contentPane.add(textName);
		textName.setColumns(10);
		
		choice.setBounds(126, 68, 186, 21);
		contentPane.add(choice);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener((arg0)->{
			try {
				create();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		btnCreate.setBounds(131, 123, 114, 25);
		contentPane.add(btnCreate);
		choice.add("Admin");
		choice.add("Teacher");
		choice.add("Student");
	}
	
	private void create() throws SQLException, ClassNotFoundException {
		try {
			connection = ICommonDAO.getConnection();
			preparedStatement = connection.prepareStatement(SQLConstants.ADD_TO_USER);
			preparedStatement.setString(1, textName.getText());
			preparedStatement.setString(1, choice.getSelectedItem());
			preparedStatement2 = connection.prepareStatement(SQLConstants.GET_UID);
			preparedStatement.setString(1, textName.getText());
			ResultSet resultSet = preparedStatement2.executeQuery();
			preparedStatement2 = connection.prepareStatement(SQLConstants.GET_RID);
			preparedStatement2.setString(1, choice.getSelectedItem());
			ResultSet resultSet2 = preparedStatement2.executeQuery();
			preparedStatement2 = connection.prepareStatement(SQLConstants.ADD_ROLE);
			preparedStatement2.setInt(1, resultSet.getInt(0));
			preparedStatement2.setInt(2, resultSet2.getInt(0));
			preparedStatement.executeUpdate();
			preparedStatement2.executeUpdate();
			resultSet.close();
			resultSet2.close();
		}
		finally {
			if (preparedStatement!=null) {
				preparedStatement.close();
			}
			if (preparedStatement2!=null) {
				preparedStatement2.close();
			}
			if (connection!=null) {
				connection.close();
			}
		}
	}
}
