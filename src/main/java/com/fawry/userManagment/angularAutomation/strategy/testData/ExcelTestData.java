package com.fawry.userManagment.angularAutomation.strategy.testData;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.constants.excelIndices.ExcelInices;
import com.fawry.userManagment.angularAutomation.dataModels.MainDataModel;
import com.fawry.userManagment.angularAutomation.utils.Log;
import com.fawry.userManagment.angularAutomation.utils.PropertiesFilesHandler;
import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import org.apache.poi.ss.usermodel.Row;
import com.fawry.userManagment.angularAutomation.utils.ExcelHandler;


public class ExcelTestData implements TestDataStrategy {

	public ArrayList<ArrayList<Object>> loadTestData(String filePathAndSheetNo)
	{
		ArrayList<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
		try {

			String filePath = filePathAndSheetNo.split(";")[0];
			String sheetNo = filePathAndSheetNo.split(";")[1];

			Iterator<Row> rows = ExcelHandler.loadExcelSheetRows(filePath, Integer.parseInt(sheetNo));

			//get get header columns number
			short headerColumnsNum = rows.next().getLastCellNum();

			// get smoke test scope flag config from properties file
			PropertiesFilesHandler propLoader = new PropertiesFilesHandler();
			Properties prop = propLoader.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
			String isSmockTestScopeEnabled = prop.getProperty(GeneralConstants.SMOKE_TEST_FLAG);

			while (rows.hasNext()) {
				Row row = rows.next();
				ArrayList<Object> cellsObjects = new ArrayList<Object>();
				ArrayList<String> rowCells = ExcelHandler.getExcelRowCells(row, headerColumnsNum);

				for (int i = 0; i < rowCells.size(); i++) {
					Object cell = new Object();
					cell = rowCells.get(i);
					cellsObjects.add(cell);
				}
				// if smoke test scope is enabled, load rows that are marked as smoke only
				if (isSmockTestScopeEnabled.equalsIgnoreCase(GeneralConstants.TRUE)) {
					if (cellsObjects.get(ExcelInices.TEST_SCOPE_INDEX).toString().equalsIgnoreCase(GeneralConstants.TEST_SCOPE_SMOKE))
						results.add(cellsObjects);
				} else
					results.add(cellsObjects);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return results;

	}

	@Override
	public Object[][] getTestDataFromExtSource(String filePathAndSheetNo, Class dataModelClass) {

		Object[][] result = null;

		try {
			String filePath = filePathAndSheetNo.split(";")[0];
			String sheetNo = filePathAndSheetNo.split(";")[1];

			PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().sheetIndex(Integer.parseInt(sheetNo)).build();
			List<Class> testDataList = Poiji.fromExcel(new File(filePath), dataModelClass, options);


			List inScopeTestDataList = new ArrayList();

			// get smoke test scope flag config from properties file
			PropertiesFilesHandler propLoader = new PropertiesFilesHandler();
			Properties prop = propLoader.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
			String isSmockTestScopeEnabled = prop.getProperty(GeneralConstants.SMOKE_TEST_FLAG);


			// if smoke test scope is enabled, load rows that are marked as smoke only in test data sheet
			if (isSmockTestScopeEnabled.equalsIgnoreCase(GeneralConstants.TRUE)) {
//				System.out.println("MainDataModel.class.cast(testDataList.get(i)) " + MainDataModel.class.cast(testDataList.get(0)));
				for (int i = 0; i < testDataList.size(); i++)
					if (MainDataModel.class.cast(testDataList.get(i)).getTestScope().equalsIgnoreCase("smoke"))
						inScopeTestDataList.add(testDataList.get(i));
			}
			// otherwise get all test cases"smoke and full" from test data sheet
			else
				for (int i = 0; i < testDataList.size(); i++)
					inScopeTestDataList.add(testDataList.get(i));


			// fill in the object[][] to be returned to the data provider
			result = new Object[inScopeTestDataList.size()][1];

			for(int i=0; i<inScopeTestDataList.size(); i++)
				result[i][0] = inScopeTestDataList.get(i);

		} catch (NumberFormatException e) {
			Log.error("Error occured in " + new Object() {
			}
					.getClass()
					.getName() + "." + new Object() {
			}
					.getClass()
					.getEnclosingMethod()
					.getName(), e);
		}

		return result;

	}





}
