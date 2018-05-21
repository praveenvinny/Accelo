/**
 * Assignment done for the job at Accelo
 *
 * File Name       : CompanyBean.java
 *
 * Description     : Manage the details of storing, updating and managing company details.
 *
 * Version         : 1.0.0.
 *
 * Created Date    : May 11, 2018
 * 
 * Created By 	   : Praveen Vinny
 */

package com.accelo.api.bean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.accelo.api.dao.FetchCompanies_DAO;

/**
 * This class defines the variables and methods required for manipulating the
 * company details.
 * 
 * @author Praveen Vinny
 *
 */
public class CompanyBean extends ActionSupport {
	private String title; // To store the company name.
	private String website; // To store the name of the website.
	private int companyID; // To store the company ID.
	private CompanyDetailsBean companyDetailsBean; // Used for manipulations on the bean.

	/**
	 * The static variables declared below are used to preserve old values. They are
	 * used to check if the values are changed by user during updation.
	 */
	public static int existingID;
	public static String existingTitle;
	public static String existingWebsite;

	/**
	 * To get the value of the company ID.
	 * 
	 * @return
	 */
	public int getCompanyID() {
		return companyID;
	}

	/**
	 * To set the value of the company ID.
	 * 
	 * @param companyID
	 */
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	/**
	 * To obtain the title value.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * To set the value of the company title.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * To get the value of the company website returned.
	 * 
	 * @return
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * To set the value of the company website.
	 * 
	 * @param website
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	public List<CompanyBean> companiesList; // Stores the list of companies fetched from the DB.

	/**
	 * To obtain the values of the companies from the active object.
	 * 
	 * @return
	 */
	public List<CompanyBean> getCompaniesList() {
		return companiesList;
	}

	/**
	 * To set the values to the list of companies.
	 * 
	 * @param companiesList
	 */
	public void setCompaniesList(List<CompanyBean> companiesList) {
		this.companiesList = companiesList;
	}

	/**
	 * The following method is used to populate the list of companies.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String validateCompaniesLoad() throws ClassNotFoundException {
		try {
			/**
			 * The method declared in the DAO is called to populate the array list for
			 * companies.
			 */
			this.setCompaniesList(FetchCompanies_DAO.getCompaniesFromDB());
		} catch (SQLException e) {
			addFieldError("resultLabel", "Trouble accessing database. Please ensure DB is setup correctly.");
		}
		return "Success";
	}

	/**
	 * The following method is used to insert a new company into the database.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String createCompany() throws ClassNotFoundException, SQLException {
		super.validate();
		if ((this.title.length() == 0) || (this.title == null)) {
			addFieldError("resultLabel", "Please enter the title of the company.");
			return "input";
		} else if ((this.website.length() == 0) || (this.website == null)) {
			addFieldError("resultLabel", "Please enter the website.");
			return "input";
		} else {
			/**
			 * The DAO method is used to insert values into the database.
			 */
			String result = FetchCompanies_DAO.insertCompanyIntoDB(this);
			addFieldError("resultLabel", result);
			return "Success";
		}
	}

	/**
	 * This method is used to obtain the details of a company identified by a
	 * company ID. The company ID is identified by the user's choice by clicking on
	 * the corresponding link.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String companyFetch() throws ClassNotFoundException, SQLException {
		/**
		 * The value of the company ID gets assigned by the user's choice on the JSP
		 * page. The set() method gets invoked automatically and the details are saved.
		 * The DAO method returns an object which gets assigned to the Company bean.
		 */
		CompanyDetailsBean myBean = FetchCompanies_DAO.getCompanyFromDB(this.getCompanyID());
		/**
		 * The following steps are used to set the values to the current object. This
		 * will in turn get assigned to the values in the JSP page.
		 */
		this.companyID = myBean.companyID;
		this.title = myBean.title;
		this.website = myBean.website;
		/**
		 * The assignment of values to the static variables are helpful during updation.
		 * If the values are unchanged, user can be given a message that the values are
		 * the same.
		 */
		CompanyBean.existingID = this.companyID;
		CompanyBean.existingTitle = this.title;
		CompanyBean.existingWebsite = this.website;
		return "Success";
	}

	/**
	 * The following method is used to update the details corresponding to the company.
	 * The ID and details are obtained from the user based on the inputs in the JSP page.
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String updateCompany() throws ClassNotFoundException, SQLException {
		/**
		 * The super class method validate() is used to check if the fields are entered right.
		 * The action support library package has to be imported in order to use this method.
		 */
		super.validate();
		if ((this.title.length() == 0) || (this.title == null)) {
			addFieldError("resultLabel", "Please enter the title of the company.");
			return "input";
		} else if ((this.website.length() == 0) || (this.website == null)) {
			addFieldError("resultLabel", "Please enter the website.");
			return "input";
		} else if ((this.website.equals(CompanyBean.existingWebsite))
				&& (this.title.equals(CompanyBean.existingTitle))) {
			addFieldError("resultLabel", "Values are same as before. You had made no changes.");
			return "input";
		} else {
			this.companyID = CompanyBean.existingID;
			/**
			 * The following method defined in the DAO method helps to update the company values.
			 */
			String result = FetchCompanies_DAO.updateCompanyDB(this);
			addFieldError("resultLabel", result);
			CompanyBean.existingTitle = this.title;
			CompanyBean.existingWebsite = this.website;
			return "Success";
		}
	}

	/**
	 * This is the default constructor of the class.
	 * This method is used to assign memory to the array list and the bean variable defined in this.
	 */
	public CompanyBean() {
		companiesList = new ArrayList<>();
		companyDetailsBean = new CompanyDetailsBean();
	}

}
