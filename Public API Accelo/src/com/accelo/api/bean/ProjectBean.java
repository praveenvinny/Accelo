package com.accelo.api.bean;

public class ProjectBean {
	String title;	// To store the project title.
	int company;	// To store the ID corresponding to the company.
	int contact;	// To store the mapping ID corresponding to the contact.
	String description;	// To store the description corresponding to the project.
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCompany() {
		return company;
	}
	public void setCompany(int company) {
		this.company = company;
	}
	public int getContact() {
		return contact;
	}
	public void setContanct(int contact) {
		this.contact = contact;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
