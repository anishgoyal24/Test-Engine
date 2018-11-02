package com.anish.testengine.user.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import com.anish.testengine.question.dao.QuestionDAO;
import com.anish.testengine.question.dto.QuestionDTO;
import com.anish.testengine.user.dto.UserDTO;
import com.anish.testengine.utils.ExcelReader;
import com.anish.testengine.utils.ICommonUtils;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UploadTest extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JFileChooser jFileChooser;
	private Logger logger = Logger.getLogger(UploadTest.class);
	private ArrayList<QuestionDTO> questions;
	private String path;
	private JTextField testName;
	private JTextField testCode;
	
	public void upload() throws IOException, SQLException {
		questions = ExcelReader.readExcel(path);
		if (questions!=null && questions.size()>0) {
			QuestionDAO questionDAO = new QuestionDAO();
			boolean isUploaded = questionDAO.isUploaded(questions, testName.getText(), testCode.getText());
			String message = isUploaded?"Uploaded...":"Not Uploaded....";
			JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
		
	public UploadTest(UserDTO userDTO) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Dashboard dashboard = new Dashboard(userDTO);
				dashboard.setVisible(true);
				dispose();
			}
		});
		questions = null;
		logger.debug("UploadTest design started");
		setResizable(false);
		setTitle("Upload Test");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 189);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblChooseAnExcel = new JLabel("Choose an Excel file");
		lblChooseAnExcel.setBounds(12, 29, 179, 15);
		contentPane.add(lblChooseAnExcel);
		
		textField = new JTextField();
		textField.setBounds(155, 27, 260, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener((arg0)->{
			logger.debug("Choosing file");
			jFileChooser = new JFileChooser();
			jFileChooser.setFileFilter(new FileNameExtensionFilter("Excel file", "xls"));
			int r = jFileChooser.showOpenDialog(null);
			if (r==JFileChooser.OPEN_DIALOG) {
				textField.setText(jFileChooser.getSelectedFile().getAbsolutePath());
				path = textField.getText();
			}
		});
		btnBrowse.setBounds(77, 120, 114, 25);
		contentPane.add(btnBrowse);
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener((arg0)->{
			try {
				upload();
			} catch (Exception e) {
				logger.error(ICommonUtils.getStackTraceString(e));
				JOptionPane.showMessageDialog(this,"Upload Error", "Cannot Upload file. Error", JOptionPane.INFORMATION_MESSAGE);
			}
		});
			
		btnUpload.setBounds(246, 120, 114, 25);
		contentPane.add(btnUpload);
		
		JLabel lblTestName = new JLabel("Test Name");
		lblTestName.setBounds(12, 60, 133, 15);
		contentPane.add(lblTestName);
		
		testName = new JTextField();
		testName.setBounds(155, 58, 260, 19);
		contentPane.add(testName);
		testName.setColumns(10);
		
		JLabel lblTestCode = new JLabel("Test Code");
		lblTestCode.setBounds(12, 93, 133, 15);
		contentPane.add(lblTestCode);
		
		testCode = new JTextField();
		testCode.setBounds(155, 89, 260, 19);
		contentPane.add(testCode);
		testCode.setColumns(10);
		logger.debug("UploadTest design ended");
}
	}
