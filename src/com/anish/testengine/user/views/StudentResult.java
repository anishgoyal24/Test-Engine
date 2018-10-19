package com.anish.testengine.user.views;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.ICommonDAO;
import com.anish.testengine.utils.SQLConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StudentResult extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private UserDTO userDTO;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private String testCode;
	private int score;
	private int attempted;
	private int correct;
	private int wrong;
	private DefaultTableModel defaultTableModel;
	

	public StudentResult(UserDTO userDTO) throws ClassNotFoundException, SQLException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Dashboard dashboard = new Dashboard(userDTO);
				dashboard.setVisible(true);
				dispose();
			}
		});
		defaultTableModel = new DefaultTableModel();
		this.userDTO = userDTO;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		defaultTableModel.addColumn("Test Code");
		defaultTableModel.addColumn("Score");
		defaultTableModel.addColumn("Attempted");
		defaultTableModel.addColumn("Correct");
		defaultTableModel.addColumn("Wrong");
		getContent();
		table = new JTable(defaultTableModel);
		scrollPane.setViewportView(table);
	}
	
	private void getContent() throws ClassNotFoundException, SQLException {
		try{
			connection = ICommonDAO.getConnection();
			preparedStatement = connection.prepareStatement(SQLConstants.STUDENT_RESULT);
			preparedStatement.setString(1, userDTO.getUsername());
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				testCode = resultSet.getString("test_code");
				score = resultSet.getInt("score");
				correct = resultSet.getInt("correct");
				wrong = resultSet.getInt("wrong");
				attempted = resultSet.getInt("attempted");
				defaultTableModel.addRow(new Object[] {testCode, score, attempted, correct, wrong});
			}
		}
		finally {
			if(resultSet!=null) {
				resultSet.close();
			}
			if(preparedStatement!=null) {
				preparedStatement.close();
			}
			if (connection!=null) {
				connection.close();
			}
		}
		
	}

}
