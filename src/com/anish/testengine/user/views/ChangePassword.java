package com.anish.testengine.user.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.ICommonDAO;
import com.anish.testengine.utils.ICommonUtils;
import com.anish.testengine.utils.SQLConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangePassword extends JFrame {

	private JPanel contentPane;
	private UserDTO userDTO;
	private JTextField textPassword;
	private JTextField textRePassword;
	private Connection connection;
	private PreparedStatement preparedStatement;

	public ChangePassword(UserDTO userDTO) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Dashboard dashboard = new Dashboard(userDTO);
				dashboard.setVisible(true);
				dispose();
			}
		});
		setTitle("Change Password");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 186);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewPassword = new JLabel("New Password");
		lblNewPassword.setBounds(27, 31, 117, 15);
		contentPane.add(lblNewPassword);
		
		JLabel lblReenterPassword = new JLabel("Re-enter Password");
		lblReenterPassword.setBounds(27, 71, 152, 15);
		contentPane.add(lblReenterPassword);
		
		JButton btnChange = new JButton("Change");
		btnChange.addActionListener((arg0)->{
			if (textPassword.getText().equals(textRePassword.getText())) {
				try {
					changePassword();
				} catch (ClassNotFoundException | SQLException e) {
					ICommonUtils.getStackTraceString(e);
					JOptionPane.showMessageDialog(this, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnChange.setBounds(160, 118, 114, 25);
		contentPane.add(btnChange);
		
		textPassword = new JTextField();
		textPassword.setBounds(162, 29, 237, 19);
		contentPane.add(textPassword);
		textPassword.setColumns(10);
		
		textRePassword = new JTextField();
		textRePassword.setColumns(10);
		textRePassword.setBounds(162, 69, 237, 19);
		contentPane.add(textRePassword);
		this.userDTO = userDTO;
	}
	
	private void changePassword() throws SQLException, ClassNotFoundException {
		try {
			connection = ICommonDAO.getConnection();
			preparedStatement = connection.prepareStatement(SQLConstants.CHANGE_PASSWORD);
			preparedStatement.setString(1, textPassword.getText());
			preparedStatement.setString(2, userDTO.getUsername());
			preparedStatement.executeUpdate();
		}
		finally {
			if (preparedStatement!=null) {
				preparedStatement.close();
			}
			if (connection!=null) {
				connection.close();
			}
		}
	}
}
