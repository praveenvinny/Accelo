package com.accelo.api.action;

import java.sql.SQLException;
import java.util.ArrayList;

import com.accelo.api.dao.Projects_DAO;
import com.opensymphony.xwork2.ActionSupport;

public class AllProjects extends ActionSupport {
	private int ID;
	private String title;
	private String description;
	private String contact;
	private String company;
	private String email;
	private String phone;
	
	public static int oldProjectID;
	public static String oldEmail;
	public static String oldPhone;

	private int projectID;

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	private ArrayList<AllProjects> projectsList;

	public AllProjects() {
		projectsList = new ArrayList<AllProjects>();
	}

	public ArrayList<AllProjects> getProjectsList() {
		return projectsList;
	}

	public void setProjectsList(ArrayList<AllProjects> projectsList) {
		this.projectsList = projectsList;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String viewProjectDetails() throws ClassNotFoundException, SQLException {
		this.projectsList = Projects_DAO.getProjectsList();
		if (projectsList.size() > 0) {
			// Do nothing
		} else {
			addFieldError("resultLabel", "No projects available.");
		}
		return "Success";
	}

	public String viewProjectByID() throws ClassNotFoundException, SQLException {
		AllProjects project = new AllProjects();
		System.out.println("Project ID = "+this.projectID);
		project = Projects_DAO.getProjectByID(this.getProjectID());
		this.company = project.company;
		this.ID = project.ID;
		this.contact = project.contact;
		this.description = project.description;
		this.email = project.email;
		this.phone = project.phone;
		this.title = project.title;
		AllProjects.oldEmail = this.email;
		AllProjects.oldPhone = this.phone;
		AllProjects.oldProjectID = this.ID;
		return "Success";
	}
	
	public String updateContact() throws ClassNotFoundException, SQLException {
		super.validate();
		if ((this.email.length() == 0) || (this.email == null)) {
			addFieldError("resultLabel", "Please enter the email.");
			return "input";
		} else if ((this.phone.length() == 0) || (this.phone == null)) {
			addFieldError("resultLabel", "Please enter the phone.");
			return "input";
		} else if ((this.email.equals(AllProjects.oldEmail))
				&& (this.phone.equals(AllProjects.oldPhone))) {
			this.projectID = AllProjects.oldProjectID;
			viewProjectByID();
			addFieldError("resultLabel", "Values are same as before. You had made no changes.");
			return "input";
		} else {
			/**
			 * Doing the copy operations to static variables in order to preserve
			 * values when the page gets refreshed.
			 */
			this.projectID = AllProjects.oldProjectID;
			System.out.println(this.projectID);
			System.out.println(this.email);
			System.out.println(this.phone);
			AllProjects.oldEmail = this.email;
			AllProjects.oldPhone = this.phone;
			String result = Projects_DAO.updateProjectDB(this);
			addFieldError("resultLabel", result);
			return "Success";
		}
	}
}
