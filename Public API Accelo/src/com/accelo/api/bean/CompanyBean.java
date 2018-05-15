package com.accelo.api.bean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.accelo.api.dao.FetchCompanies_DAO;

public class CompanyBean extends ActionSupport {
	private String title;
	private String website;
	private int companyID;
	private CompanyDetailsBean companyDetailsBean;
	
	public static int existingID;
	public static String existingTitle;
	public static String existingWebsite;
	
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
	
	public List<CompanyBean> companiesList;

	public List<CompanyBean> getCompaniesList() {
		return companiesList;
	}
	public void setCompaniesList(List<CompanyBean> companiesList) {
		this.companiesList = companiesList;
	}
	
	public String validateCompaniesLoad() throws ClassNotFoundException, SQLException {
		this.setCompaniesList(FetchCompanies_DAO.getCompaniesFromDB());
		return "Success";
	}
	
	public String createCompany() throws ClassNotFoundException, SQLException {
		
		super.validate();
		if ((this.title.length() == 0) || (this.title == null)) {
			addFieldError("resultLabel", "Please enter the title of the company.");
			return "input";
		} else if ((this.website.length() == 0) || (this.website == null)) {
			addFieldError("resultLabel", "Please enter the website.");
			return "input";
		} else {
			System.out.println("Title: " + title + " and website: "+website);
			String result = FetchCompanies_DAO.insertCompanyIntoDB(this);
			addFieldError("resultLabel", result);
			return "Success";
		}
	}
	
	public String companyFetch() throws ClassNotFoundException, SQLException {
		CompanyDetailsBean myBean = FetchCompanies_DAO.getCompanyFromDB(this.getCompanyID());
		this.companyID = myBean.companyID;
		this.title = myBean.title;
		this.website = myBean.website;
		CompanyBean.existingID = this.companyID;
		CompanyBean.existingTitle = this.title;
		CompanyBean.existingWebsite = this.website;
		return "Success";
	}
	
public String updateCompany() throws ClassNotFoundException, SQLException {
		
		super.validate();
		if ((this.title.length() == 0) || (this.title == null)) {
			addFieldError("resultLabel", "Please enter the title of the company.");
			return "input";
		} else if ((this.website.length() == 0) || (this.website == null)) {
			addFieldError("resultLabel", "Please enter the website.");
			return "input";
		} else if ((this.website.equals(CompanyBean.existingWebsite)) && (this.title.equals(CompanyBean.existingTitle))) {
			addFieldError("resultLabel", "Values are same as before. You had made no changes.");
			return "input";
		} else {
			this.companyID = CompanyBean.existingID;
			String result = FetchCompanies_DAO.updateCompanyDB(this);
			addFieldError("resultLabel", result);
			CompanyBean.existingTitle = this.title;
			CompanyBean.existingWebsite = this.website;
			return "Success";
		}
	}

	public CompanyBean() {
		companiesList=new ArrayList<>();
		companyDetailsBean = new CompanyDetailsBean();
	}

}
