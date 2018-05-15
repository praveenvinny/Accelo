package com.accelo.api.bean;

public class ContactBean {
	private int ID;
	private String firstName;
	private String surname;
	private String email;
	private String phone;
	private String company;
	private int mappingID;
	

	public int getMappingID() {
		return mappingID;
	}

	public void setMappingID(int mappingID) {
		this.mappingID = mappingID;
	}

	public ContactBean() {
		// DO NOTHING
	}

	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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
	
}
