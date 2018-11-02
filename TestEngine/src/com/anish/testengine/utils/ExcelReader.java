package com.anish.testengine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.anish.testengine.question.dao.QuestionDAO;
import com.anish.testengine.question.dto.QuestionDTO;

public class ExcelReader {
	private static Logger logger = Logger.getLogger(QuestionDAO.class);
	
	
	public static ArrayList<QuestionDTO> readExcel(String path) throws IOException {
		boolean isFirstRun = true;
		QuestionDTO questionDTO;
		ArrayList<QuestionDTO> questions = new ArrayList<>();
		int counter = 0;
		logger.debug("Starting connection");
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
		logger.debug("Connection succeded");
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		Iterator<Row> rows = hssfSheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			if (isFirstRun) {
				isFirstRun = false;
				continue;
			}
			int index = 0;
			counter = 0;
			ArrayList<String> options = new ArrayList<>();
			Iterator<Cell> cells = row.cellIterator();
			questionDTO = new QuestionDTO();
			try {
				while(cells.hasNext()) {
					Cell cell = cells.next();
					if (counter == 0) {
						questionDTO.setId((int)cell.getNumericCellValue());
					}
					if (counter==1) {
						questionDTO.setQuestion(cell.getStringCellValue());
					}
					if (counter >1 && counter <6) {
						options.add(index, cell.getStringCellValue());
						index++;
					}
					if (counter==6) {
						questionDTO.setAnswer((int)cell.getNumericCellValue());
					}
					if (counter==7) {
						questionDTO.setPositiveScore((int)cell.getNumericCellValue());
					}
					if (counter==8) {
						questionDTO.setNegativeScore((int)cell.getNumericCellValue());
					}
					counter++;
			}
			questionDTO.setOptions(options);
			questions.add(questionDTO);
		}
		catch (Exception e) {
			logger.error(ICommonUtils.getStackTraceString(e));
			throw e;
		}
	}
		logger.debug("All rows read");
		if (hssfWorkbook!=null) {
			hssfWorkbook.close();
		}
		if (fileInputStream!=null) {
			fileInputStream.close();
		}
		logger.debug("All connections closed");
		return questions;
	}
}
