package com.accelo.api.bean;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.accelo.api.bean.ContactBean;
import com.accelo.api.dao.FetchCompanies_DAO;

public class CompanyDetailsBean extends ActionSupport {
	int companyID;
	String title;
	String website;
	List<ContactBean> contactsList;

	public CompanyDetailsBean() {
		contactsList = new ArrayList<ContactBean>();
	}

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<ContactBean> getContactsList() {
		return contactsList;
	}

	public void setContactsList(List<ContactBean> contactsList) {
		this.contactsList = contactsList;
	}

	public String viewCompanyDetails() throws ClassNotFoundException, SQLException {
		CompanyDetailsBean myBean = FetchCompanies_DAO.getCompanyFromDB(this.getCompanyID());
		this.companyID = myBean.companyID;
		this.title = myBean.title;
		this.website = myBean.website;
		this.contactsList = myBean.getContactsList();
		if (contactsList.size() > 0) {
			// Do nothing
		} else {
			addFieldError("resultLabel", "No contacts assigned to this company.");
		}
		return "Success";
	}
	
}
