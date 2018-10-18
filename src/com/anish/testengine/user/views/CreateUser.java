package com.anish.testengine.user.views;

import java.awt.Choice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.ICommonDAO;
import com.anish.testengine.utils.ICommonUtils;
import com.anish.testengine.utils.SQLConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateUser extends JFrame {

	private JPanel contentPane;
	private JTextField textName;
	Connection connection;
	PreparedStatement preparedStatement;
	PreparedStatement preparedStatement2;
	Choice choice = new Choice();
	Logger logger = Logger.getLogger(CreateUser.class);
	int uid = 0;
	int rid = 0;
	
	public CreateUser(UserDTO userDTO) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Dashboard dashboard = new Dashboard(userDTO);
				dashboard.setVisible(true);
				dispose();
			}
		});
		logger.debug("CreateUser design started");
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
				ICommonUtils.getStackTraceString(e);
			}
		});
		btnCreate.setBounds(131, 123, 114, 25);
		contentPane.add(btnCreate);
		choice.add("Admin");
		choice.add("Teacher");
		choice.add("Student");
		logger.debug("CreateUser design ended");
	}
	
	private void create() throws SQLException, ClassNotFoundException {
		try {
			logger.debug("Creating user");
			connection = ICommonDAO.getConnection();
			preparedStatement = connection.prepareStatement(SQLConstants.ADD_TO_USER);
			preparedStatement.setString(1, textName.getText());
			preparedStatement.setString(2, textName.getText() + "@" + choice.getSelectedItem().toLowerCase());
			preparedStatement.executeUpdate();
			preparedStatement2 = connection.prepareStatement(SQLConstants.GET_UID);
			preparedStatement2.setString(1, textName.getText());
			ResultSet resultSet = preparedStatement2.executeQuery();
			preparedStatement2 = connection.prepareStatement(SQLConstants.ADD_ROLE);
			while(resultSet.next()) {
				uid = resultSet.getInt("uid");
			}
			if (choice.getSelectedItem().toLowerCase().equals("admin")) {
				rid = 1;
			}
			else if(choice.getSelectedItem().toLowerCase().equals("teacher")) {
				rid = 2;				
			}
			else {
				rid = 3;
			}
			if (uid!=0 && rid!=0) {
				preparedStatement2.setInt(1, uid);
				preparedStatement2.setInt(2, rid);
				preparedStatement2.executeUpdate();
				JOptionPane.showMessageDialog(this, "User added sucessfully", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
			
			resultSet.close();
			logger.debug("User created");
		}
		finally {
			if (uid==0 || rid==0){
				preparedStatement = connection.prepareStatement("delete from user_mst where userid = ?");
				preparedStatement.setString(1, textName.getText());
				preparedStatement.executeUpdate();
			}
			if (preparedStatement!=null) {
				preparedStatement.close();
			}
			if (preparedStatement2!=null) {
				preparedStatement2.close();
			}
			if (connection!=null) {
				connection.close();
			}
			logger.debug("connections closed");
		}
	}
}
