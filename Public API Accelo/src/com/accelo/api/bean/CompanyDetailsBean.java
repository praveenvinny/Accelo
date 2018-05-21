/**
 * Assignment done for the job at Accelo
 *
 * File Name       : CompanyDetailsBean.java
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

import com.opensymphony.xwork2.ActionSupport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.accelo.api.bean.ContactBean;
import com.accelo.api.dao.FetchCompanies_DAO;

/**
 * This class defines the variables and methods required for manipulating the
 * details which connects company and contacts.
 * 
 * @author Praveen Vinny
 *
 */
public class CompanyDetailsBean extends ActionSupport {
	int companyID;		// To store the company ID.
	String title;		// To store the title of the company.
	String website;		// To store the company website.
	List<ContactBean> contactsList;	// To store the contacts corresponding to the company.

	/**
	 * To obtain the details of the contacts assigned to the company.
	 */
	public CompanyDetailsBean() {
		contactsList = new ArrayList<ContactBean>();
	}

	/**
	 * To obtain the company ID.
	 * @return
	 */
	public int getCompanyID() {
		return companyID;
	}

	/**
	 * To set the company ID.
	 * @param companyID
	 */
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	/**
	 * This method is used to obtain the title of the company.
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This method is used to set the title of the company.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * To get the details of the company website.
	 * @return
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * To set the details of the company website.
	 * @param website
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * To obtain the list of contacts associated with a company.
	 * @return
	 */
	public List<ContactBean> getContactsList() {
		return contactsList;
	}

	/**
	 * The following method populates the list of contacts.
	 * @param contactsList
	 */
	public void setContactsList(List<ContactBean> contactsList) {
		this.contactsList = contactsList;
	}

	/**
	 * The following method is used to obtain the details corresponding to each company.
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String viewCompanyDetails() throws ClassNotFoundException, SQLException {
		/**
		 * The company ID gets set from the JSP page through user's choice.
		 * All the values corresponding to the company are displayed.
		 */
		CompanyDetailsBean myBean = FetchCompanies_DAO.getCompanyFromDB(this.getCompanyID());
		this.companyID = myBean.companyID;
		this.title = myBean.title;
		this.website = myBean.website;
		this.contactsList = myBean.getContactsList();
		if (contactsList.size() > 0) {
			// Do nothing because the JSP page will have the details displayed.
		} else {
			addFieldError("resultLabel", "No contacts assigned to this company.");
		}
		return "Success";
	}
	
}
