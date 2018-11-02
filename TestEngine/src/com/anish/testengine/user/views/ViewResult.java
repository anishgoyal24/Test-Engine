package com.anish.testengine.user.views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.ICommonDAO;
import com.anish.testengine.utils.SQLConstants;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewResult extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel defaultTableModel;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private String test_code;
	private String username;
	private int score;
	private int attempted;
	private int correct;
	private int wrong;

	public ViewResult(UserDTO userDTO) throws ClassNotFoundException, SQLException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Dashboard dashboard = new Dashboard(userDTO);
				dashboard.setVisible(true);
				dispose();
			}
		});
		setTitle("Results");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		defaultTableModel = new DefaultTableModel();
		defaultTableModel.addColumn("Test Code");
		defaultTableModel.addColumn("UserID");
		defaultTableModel.addColumn("Score");
		defaultTableModel.addColumn("Attempted");
		defaultTableModel.addColumn("Correct");
		defaultTableModel.addColumn("Wrong");
		try {
			connection = ICommonDAO.getConnection();
			preparedStatement = connection.prepareStatement(SQLConstants.VIEW_RESULT);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				test_code = resultSet.getString("test_code");
				username = resultSet.getString("userid");
				score = resultSet.getInt("score");
				attempted = resultSet.getInt("attempted");
				correct = resultSet.getInt("correct");
				wrong = resultSet.getInt("wrong");
				defaultTableModel.addRow(new Object[] {test_code, username, score, attempted, correct, wrong});
			}
		}
		finally {
			if (resultSet!=null) {
				resultSet.close();
			}
			if (preparedStatement!=null) {
				preparedStatement.close();
			}
			if (connection!=null) {
				connection.close();
			}
		}
		table = new JTable(defaultTableModel);
		scrollPane.setViewportView(table);
		setVisible(true);
	}

}
