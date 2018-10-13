package com.anish.testengine.user.views;

import java.awt.*;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.anish.testengine.utils.ICommonUtils;

import org.apache.log4j.Logger;

import com.anish.testengine.user.dao.UserDAO;
import com.anish.testengine.user.dto.UserDTO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class LoginView {

	protected JFrame frame;
	private JLabel lblPassword;
	private JPasswordField userpwd;
	private JTextField usrtext;
	private Logger logger = Logger.getLogger(LoginView.class);

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				LoginView window = new LoginView();
				window.frame.setVisible(true);
			} catch (Exception e) {
			    ICommonUtils.getStackTraceString(e);
			}
		});
	}

	public void doLogin() {
		logger.debug("Started checking login");
		String username = usrtext.getText();
		String password = new String(userpwd.getPassword());
		UserDAO userDAO = new UserDAO();
		try {
			UserDTO userDTO = userDAO.doLogin(username, password);
			if (userDTO != null) {
				logger.debug("Login successful");
				Dashboard dashboard = new Dashboard(userDTO);
				dashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);
				dashboard.setVisible(true);
				frame.dispose();
			} else {
				logger.debug("Wrong credentials");
				JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Authentication Error", JOptionPane.INFORMATION_MESSAGE);
				usrtext.setText("");
				userpwd.setText("");

			}
		} catch (SQLException | ClassNotFoundException e) {
			logger.error(ICommonUtils.getStackTraceString(e));
			JOptionPane.showMessageDialog(frame, "Database Error", "Error", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public LoginView() {
		initialize();
	}


	private void initialize() {
		logger.debug("LoginView design started");
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Test Engine");
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Dialog", Font.BOLD, 16));
		lblUsername.setForeground(Color.BLACK);
		lblUsername.setBounds(44, 83, 93, 15);
		frame.getContentPane().add(lblUsername);

		lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.BLACK);
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 16));
		lblPassword.setBounds(44, 143, 93, 15);
		frame.getContentPane().add(lblPassword);

		userpwd = new JPasswordField();
		userpwd.setBounds(198, 142, 186, 19);
		frame.getContentPane().add(userpwd);

		JButton login = new JButton("Login");
		login.addActionListener((arg0) -> doLogin());
		login.setBounds(82, 214, 114, 25);
		frame.getContentPane().add(login);

		JButton clear = new JButton("Clear");
		clear.addActionListener((arg0)->{
			usrtext.setText("");
			userpwd.setText("");
		});
		clear.setBounds(249, 214, 114, 25);
		frame.getContentPane().add(clear);

		usrtext = new JTextField();
		usrtext.setBounds(198, 82, 186, 19);
		frame.getContentPane().add(usrtext);
		usrtext.setColumns(10);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logger.debug("LoginView design ended");
	}
}

