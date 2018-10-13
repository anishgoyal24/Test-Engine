package com.anish.testengine.question.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.anish.testengine.question.dto.QuestionDTO;
import com.anish.testengine.user.views.UploadTest;
import com.anish.testengine.utils.ICommonDAO;
import com.anish.testengine.utils.ICommonUtils;
import com.anish.testengine.utils.SQLConstants;


public class QuestionDAO {
	boolean uploaded;
	Connection connection;
	PreparedStatement preparedStatement;
	PreparedStatement preparedStatement2;
	private Logger logger = Logger.getLogger(UploadTest.class);
	
	
	public boolean isUploaded(ArrayList<QuestionDTO> questions, String testName, String testCode) throws SQLException {
		logger.debug("Upload Started");
		try {
			logger.debug("Trying Upload");
			connection = ICommonDAO.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(SQLConstants.UPLOAD_QUES_SQL);
			for(QuestionDTO questionDTO:questions) {
			preparedStatement.setInt(1, questionDTO.getId());
			preparedStatement.setString(2, questionDTO.getQuestion());
			preparedStatement.setString(3, questionDTO.getOptions().get(0));
			preparedStatement.setString(4, questionDTO.getOptions().get(1));
			preparedStatement.setString(5, questionDTO.getOptions().get(2));
			preparedStatement.setString(6, questionDTO.getOptions().get(3));
			preparedStatement.setInt(7, questionDTO.getAnswer());
			preparedStatement.setInt(8, questionDTO.getPositiveScore());
			preparedStatement.setInt(9, questionDTO.getNegativeScore());
			preparedStatement.setString(10, testCode);
			preparedStatement.addBatch();
			}
			int results[] = preparedStatement.executeBatch();
			boolean fail = false;
			for (int i : results) {
				if (i<1) {
					fail = true;
					break;
				}
			}
			if (fail) {
				connection.rollback();
				uploaded = false;
			}
			else {
				preparedStatement2 = connection.prepareStatement(SQLConstants.UPDATE_TEST_NAME);
				preparedStatement2.setString(1, testCode);
				preparedStatement2.setString(2, testName);
				preparedStatement2.executeUpdate();
				connection.commit();
				uploaded = true;
			}
		} catch (Exception e) {
			logger.error(ICommonUtils.getStackTraceString(e));
		}
		finally {
			if (preparedStatement!=null) {
				preparedStatement.close();
			}
			if (connection!=null) {
				connection.close();
			}
		}
		return uploaded;
	}
	}

