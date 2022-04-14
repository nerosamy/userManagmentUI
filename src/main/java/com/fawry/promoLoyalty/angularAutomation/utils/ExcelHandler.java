package com.fawry.promoLoyalty.angularAutomation.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelHandler {

// Get row iterator of excel file's sheet
	public static Iterator<Row> loadExcelSheetRows(String filePath, int sheetNo)
	{
		Workbook workbook = null;
		Iterator<Row> rowIterator = null;

		try
		{
			workbook = WorkbookFactory.create(new File(filePath));
			Sheet sheet = workbook.getSheetAt(sheetNo);

			// Obtain a rowIterator and columnIterator and iterate over them
			rowIterator = sheet.rowIterator();

			// Closing the workbook
			workbook.close();
		}
		catch (EncryptedDocumentException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return rowIterator;
	}



	// load excel sheet's row's cells into an array of strings
	
	public static ArrayList<String> getExcelRowCells(Row row, short columnsNum)

	{
		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();
		ArrayList<String> rowCellsValues = new ArrayList<String>();
		
		// iterate on row's cells to capture their values
		for(int i=0; i< columnsNum; i++)

		{
			// if there an empty cell in the row, return as empty string not null
			Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

			String cellValue= "";

			if(cell !=null)
				cellValue = dataFormatter.formatCellValue(cell);
			
			//  add cell value to the array of string that stores row's cells values
			rowCellsValues.add(cellValue);
		}

		return rowCellsValues; 
	}


}
