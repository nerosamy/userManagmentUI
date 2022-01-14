package com.fawry.promoLoyalty.angularAutomation.strategy.testData;

import com.fawry.promoLoyalty.angularAutomation.strategy.testData.TestDataStrategy;

import java.util.ArrayList;


public class OracleDBTestData implements TestDataStrategy {

	public void connectToDB(String dataBaseConnectionString) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<ArrayList<Object>> loadTestData(String connectionProperties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[][] getTestDataFromExtSource(String connectionProperties, Class dataModelClass) {
		return new Object[0][];
	}


}
