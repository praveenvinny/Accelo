/**
 * Assignment done for the job at Accelo
 *
 * File Name       : ContactBean.java
 *
 * Description     : Manage the details of storing, updating and managing contact details.
 *
 * Version         : 1.0.0.
 *
 * Created Date    : May 12, 2018
 * 
 * Created By 	   : Praveen Vinny
 */

package com.accelo.api.bean;

/**
 * This class is used to store the details corresponding to each contact in the database.
 * @author Praveen Vinny
 *
 */
public class ContactBean {
	private int ID;	// To store the contact ID.
	private String firstName;	// To store the first name of the contact.
	private String surname;	// To store the surname of the contact.
	private String email;	// To store the email ID of the contact.
	private String phone;	// To store the phone number of the contact.
	private String company;	// To store the name of the company corresponding to the contact.
	private int mappingID;	// To store the mapping ID between the ontact details and primary contact.
	

	/**
	 * To obtain the mapping ID between contact details and primary contact.
	 * @return
	 */
	public int getMappingID() {
		return mappingID;
	}

	/**
	 * To set the value of the mapping ID.
	 * @param mappingID
	 */
	public void setMappingID(int mappingID) {
		this.mappingID = mappingID;
	}

	/**
	 * The default constructor of the class.
	 */
	public ContactBean() {
		// DO NOTHING
	}

	/**
	 * To obtain the name of the company.
	 * @return
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * To set the name of the company.
	 * @param company
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * To obtain the contact ID.
	 * @return
	 */
	public int getID() {
		return ID;
	}


	/**
	 * To set the contact ID.
	 * @param iD
	 */
	public void setID(int iD) {
		ID = iD;
	}


	/**
	 * To obtain the first name.
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * To set the contact first name.
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * To obtain the surname.
	 * @return
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * To set the surname.
	 * @param surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * To obtain the email.
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * To set the email ID.
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * To obtain the contact number.
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * To set the contact number.
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}	
	
}
