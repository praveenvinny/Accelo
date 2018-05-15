package com.accelo.api.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;


import com.accelo.api.bean.CompanyBean;
import com.accelo.api.bean.CompanyDetailsBean;
import com.accelo.api.bean.ContactBean;
import com.accelo.api.dao.Contacts_DAO;
import com.accelo.api.dao.FetchCompanies_DAO;

public class LinkContactWithCompany extends ActionSupport   {
	
	public static int oldMappingID;
	
	public static int oldContactID;
	
	private int mappingID;
	
	private int newCompanyID;
	
	
	public int getNewCompanyID() {
		return newCompanyID;
	}

	public void setNewCompanyID(int newCompanyID) {
		this.newCompanyID = newCompanyID;
	}

	public ContactBean getContactBean() {
		return contactBean;
	}

	public void setContactBean(ContactBean contactBean) {
		this.contactBean = contactBean;
	}

	public ContactBean contactBean = new ContactBean();
	
	public int getMappingID() {
		return mappingID;
	}

	public void setMappingID(int mappingID) {
		this.mappingID = mappingID;
	}

	public List<CompanyBean> companiesList;

	public List<CompanyBean> getCompaniesList() {
		return companiesList;
	}
	public void setCompaniesList(List<CompanyBean> companiesList) {
		this.companiesList = companiesList;
	}
	
	public String showResults() throws ClassNotFoundException, SQLException {
		this.setCompaniesList(FetchCompanies_DAO.getCompaniesFromDB());
		LinkContactWithCompany.oldMappingID = mappingID;
		ContactBean myBean = Contacts_DAO.getContactFromDB(mappingID);
		LinkContactWithCompany.oldContactID = myBean.getID();
		this.contactBean = myBean;
		return "Success";
	}
	
	public String createMapping() throws ClassNotFoundException, SQLException {
		String result = "Success";
		try {
			result = Contacts_DAO.createCompContactMapping(this.newCompanyID, LinkContactWithCompany.oldContactID, LinkContactWithCompany.oldMappingID);
			addFieldError("resultLabel", result);
			result = "Success";
		} catch (ClassNotFoundException eClassNotFoundException) {
			result = "FAIL";
			addActionError("Unable to process! Please try again!");
		} catch (SQLException eSException) {
			result = "FAIL";
			addActionError("Trouble accessing database! Please try again!");
		}
		return result;
	}
}
