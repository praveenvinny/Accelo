package com.accelo.api.action;

import java.sql.SQLException;
import java.util.ArrayList;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.accelo.api.bean.ProjectBean;
import com.accelo.api.dao.Projects_DAO;

public class ProjectsAction extends ActionSupport {
	
	String title;
	String description;
	String companyName;
	String contactNumber;
	
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}


	public String createProject() throws ClassNotFoundException, SQLException {
		String result = "input";
		if ((this.getTitle().length() == 0) || (this.getTitle() == null)) {
			addFieldError("resultLabel", "Please enter the project title.");
			return "input";
		} else if ((this.getDescription().length() == 0) || (this.getDescription() == null)) {
			addFieldError("resultLabel", "Please enter the project description.");
			return "input";
		}  else {
			//String result = title + " " + description + " " + companyName + " " + contactNumber;
			//try {
				result = Projects_DAO.insertCompanyIntoDB(this);
			/*} catch (Exception e) {
				result = "The entered company and contact is not mapped.";
				addFieldError("resultLabel", result);
				return "input";
			}*/
			addFieldError("resultLabel", result);
			return "Success";
		}
	}
}
