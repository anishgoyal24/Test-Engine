package com.anish.testengine.user.views;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.anish.testengine.question.dto.QuestionDTO;
import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.ICommonDAO;
import com.anish.testengine.utils.ICommonUtils;
import com.anish.testengine.utils.SQLConstants;

public class TakeTest {

	private JFrame frame;
	JRadioButton optionA = new JRadioButton("");
	JRadioButton optionB = new JRadioButton("");
	JRadioButton optionC = new JRadioButton("");
	JRadioButton optionD = new JRadioButton("");
	JLabel negative = new JLabel("");
	JLabel positive = new JLabel("");
	JTextArea textArea = new JTextArea();
	JButton btnNext = new JButton("Next");
	JButton btnPrevious = new JButton("Previous");
	JButton btnClear = new JButton("Clear");
	ButtonGroup buttonGroup = new ButtonGroup();
	private String testCode;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	Logger logger = Logger.getLogger(TakeTest.class);
	private ArrayList<QuestionDTO> questions;
	private int currentIndex;
	private int[] answers;
	private UserDTO userDTO;
	private int score;
	private int correct;
	private int wrong;
	
	public TakeTest(String testCode, UserDTO userDTO) throws ClassNotFoundException, SQLException {
		this.testCode = testCode;
		this.userDTO = userDTO;
		currentIndex = 0;
		initialize();
		loadTest();
		answers = new int[questions.size()];
		printQuestion();
		enableDisable();
	}

	
	private void initialize() {
		logger.debug("Creating TakeTest view");
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				submitTest();
				Dashboard dashboard = new Dashboard(userDTO);
				dashboard.setVisible(true);
				frame.dispose();
			}
		});
		
		frame.setResizable(false);
		frame.setBounds(100, 100, 637, 444);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		optionA.setBounds(86, 199, 144, 23);
		frame.getContentPane().add(optionA);
		
		optionB.setBounds(86, 236, 144, 23);
		frame.getContentPane().add(optionB);
		
		optionC.setBounds(86, 273, 144, 23);
		frame.getContentPane().add(optionC);
		
		optionD.setBounds(86, 310, 144, 23);
		frame.getContentPane().add(optionD);
		
		buttonGroup.add(optionA);
		buttonGroup.add(optionB);
		buttonGroup.add(optionC);
		buttonGroup.add(optionD);
		btnPrevious.addActionListener(arg0->{
			setAnswer();
			currentIndex--;
			enableDisable();
			printQuestion();
		});
		
		btnPrevious.setBounds(78, 355, 114, 25);
		frame.getContentPane().add(btnPrevious);
		btnNext.addActionListener((arg0)->{
			setAnswer();
			currentIndex++;
			enableDisable();
			printQuestion();
		});
		
		btnNext.setBounds(204, 355, 114, 25);
		frame.getContentPane().add(btnNext);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener((arg0)->{
			setAnswer();
			submitTest();
		});
		btnSubmit.setBounds(467, 355, 114, 25);
		frame.getContentPane().add(btnSubmit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(86, 56, 481, 105);
		frame.getContentPane().add(scrollPane);
		
		scrollPane.setViewportView(textArea);
		frame.setVisible(true);
		textArea.setEditable(false);
		btnClear.addActionListener((arg0)->{
			buttonGroup.clearSelection();
		});
		
		btnClear.setBounds(341, 355, 114, 25);
		frame.getContentPane().add(btnClear);
		
		positive.setBounds(482, 173, 33, 15);
		frame.getContentPane().add(positive);
		
		negative.setBounds(522, 173, 33, 15);
		frame.getContentPane().add(negative);
		logger.debug("TakeTest design ended");
		
	}
	
	private void loadTest() throws SQLException, ClassNotFoundException {
		try {
			logger.debug("loading test");
			questions = new ArrayList<>();
			connection = ICommonDAO.getConnection();
			preparedStatement = connection.prepareStatement(SQLConstants.GET_QUESTIONS);
			preparedStatement.setString(1, testCode);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				QuestionDTO questionDTO = new QuestionDTO();
				questionDTO.setQuestion(resultSet.getString("name"));
				questionDTO.setId(resultSet.getInt("id"));
				questionDTO.setAnswer(resultSet.getInt("answer"));
				questionDTO.setNegativeScore(resultSet.getInt("negative"));
				questionDTO.setPositiveScore(resultSet.getInt("positive"));
				ArrayList<String> options = new ArrayList<>();
				options.add(resultSet.getString("optiona"));
				options.add(resultSet.getString("optionb"));
				options.add(resultSet.getString("optionc"));
				options.add(resultSet.getString("optiond"));
				questionDTO.setOptions(options);
				questions.add(questionDTO);
			}
			
			logger.debug("test loaded");
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
			logger.debug("connections closed");
		}
	}
	
	private void printQuestion() {
		 textArea.setText(questions.get(currentIndex).getId() + questions.get(currentIndex).getQuestion());
		 optionA.setText(questions.get(currentIndex).getOptions().get(0));
		 optionB.setText(questions.get(currentIndex).getOptions().get(1));
		 optionC.setText(questions.get(currentIndex).getOptions().get(2));
		 optionD.setText(questions.get(currentIndex).getOptions().get(3));
		 positive.setText(Integer.toString(questions.get(currentIndex).getPositiveScore()));
		 negative.setText(Integer.toString(questions.get(currentIndex).getNegativeScore()));
		 if (answers[currentIndex] == 1) {
			 optionA.setSelected(true);
		 }
		 if (answers[currentIndex] == 2) {
			 optionB.setSelected(true);
		 }
		 if (answers[currentIndex] == 3) {
			 optionC.setSelected(true);
		 }
		 if (answers[currentIndex] == 4) {
			 optionD.setSelected(true);
		 }
		 if (answers[currentIndex] == 0) {
			 buttonGroup.clearSelection();
		 }
	}
	
	private void enableDisable() {
		if (questions.size()<=1) {
			btnNext.setEnabled(false);
			btnPrevious.setEnabled(false);
		}
		else {
			if (currentIndex==0) {
				btnPrevious.setEnabled(false);
				btnNext.setEnabled(true);
			}
			else if (currentIndex==questions.size()-1) {
				btnNext.setEnabled(false);
				btnPrevious.setEnabled(true);
			}
			else {
				btnNext.setEnabled(true);
				btnPrevious.setEnabled(true);
			}
		}
		buttonGroup.clearSelection();
	}
	
	public void setAnswer() {
		if (optionA.isSelected()) {
			answers[currentIndex] = 1;
		}
		if (optionB.isSelected()) {
			answers[currentIndex] = 2;
		}
		if (optionC.isSelected()) {
			answers[currentIndex] = 3;
		}
		if (optionD.isSelected()) {
			answers[currentIndex] = 4;
		}
		if (buttonGroup.isSelected(null)) {
			answers[currentIndex] = 0;
		}
	}
	
	public void submitTest() {
		try {
			calculateScore();
		} catch (ClassNotFoundException | SQLException e) {
			ICommonUtils.getStackTraceString(e);
			JOptionPane.showMessageDialog(frame, "Some error occurred", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void calculateScore() throws ClassNotFoundException, SQLException {
		logger.debug("calculating score");
		btnNext.setEnabled(false);
		btnPrevious.setEnabled(false);
		btnClear.setEnabled(false);
		for (int i = 0; i < questions.size(); i++) {
			if (answers[i] == 0) {
				score+=0;
			}
			else if (answers[i] == questions.get(i).getAnswer()) {
				score+= questions.get(i).getPositiveScore();
				correct++;
			}
			else {
				score -= questions.get(i).getNegativeScore();
				wrong++;
			}
		}
		uploadScore();
		JOptionPane.showMessageDialog(frame, "Score is: " + score, "Score", JOptionPane.INFORMATION_MESSAGE);
		Dashboard dashboard = new Dashboard(userDTO);
		dashboard.setVisible(true);
		frame.dispose();
	}
	
	private void uploadScore() throws ClassNotFoundException, SQLException {
		try {
			logger.debug("uploading score");
			connection = ICommonDAO.getConnection();
			preparedStatement = connection.prepareStatement(SQLConstants.OUTPUT_ANSWER);
			preparedStatement.setString(1, testCode);
			preparedStatement.setString(2, userDTO.getUsername());
			preparedStatement.setInt(3, score);
			preparedStatement.setInt(4, correct + wrong);
			preparedStatement.setInt(5, correct);
			preparedStatement.setInt(6, wrong);
			preparedStatement.executeUpdate();
			logger.debug("score uploaded");
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
			logger.debug("connections closed");
		}
	}
}
