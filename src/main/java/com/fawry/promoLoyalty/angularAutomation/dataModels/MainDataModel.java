package com.fawry.promoLoyalty.angularAutomation.dataModels;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class MainDataModel {

	@ExcelRow
	private int rowIndex;

	@ExcelCell(0)
	private String testCaseId;
	@ExcelCell(1)
	private String testCaseTitle;
	@ExcelCell(2)
	private String testScope;

	private String addOrUpdateAction;

	private String errType;
	private String expectedMessage;
	private String idInDB;
	private String dbCriteria;


	public String getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getTestCaseTitle() {
		return testCaseTitle;
	}

	public void setTestCaseTitle(String testCaseTitle) {
		this.testCaseTitle = testCaseTitle;
	}

	public String getTestScope() {
		return testScope;
	}

	public void setTestScope(String testScope) {
		this.testScope = testScope;
	}

	public String getAddOrUpdateAction() {
		return addOrUpdateAction;
	}

	public void setAddOrUpdateAction(String addOrUpdateAction) {
		this.addOrUpdateAction = addOrUpdateAction;
	}

	public String getExpectedMessage() {
		return expectedMessage;
	}
	public void setExpectedMessage(String expectedMessage) {
			this.expectedMessage = expectedMessage;
		}

	public String getErrType() {
		return errType;
	}

	public void setErrType(String errType) {
		this.errType = errType;
	}

	public String getIdInDB() {
		return idInDB;
	}

	public void setIdInDB(String idInDB) {
		this.idInDB = idInDB;
	}

	public String getDbCriteria() {
		return dbCriteria;
	}

	public void setDbCriteria(String dbCriteria) {
		this.dbCriteria = dbCriteria;
	}
		
}
