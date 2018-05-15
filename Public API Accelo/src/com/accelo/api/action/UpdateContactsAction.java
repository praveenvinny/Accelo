package com.accelo.api.action;

import java.sql.SQLException;

import com.opensymphony.xwork2.ActionSupport;

import com.accelo.api.bean.ContactBean;
import com.accelo.api.dao.Contacts_DAO;

public class UpdateContactsAction extends ActionSupport {

	private int ID; // contact ID
	private int mappingID;
	private String firstName;
	private String surname;
	private String phone;
	private String email;

	public static int oldID; // contact ID
	private static int oldMappingID;
	public static String existingFirstName;
	public static String existingSurname;
	public static String existingPhone;
	public static String existingEmail;

	public int getMappingID() {
		return mappingID;
	}

	public void setMappingID(int mappingID) {
		this.mappingID = mappingID;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() throws ClassNotFoundException, SQLException {
		ContactBean contactBean = Contacts_DAO.fetchContactFromDB(this.mappingID);
		UpdateContactsAction.oldID = contactBean.getID();
		UpdateContactsAction.existingFirstName = contactBean.getFirstName();
		UpdateContactsAction.existingSurname = contactBean.getSurname();
		UpdateContactsAction.existingPhone = contactBean.getPhone();
		UpdateContactsAction.existingEmail = contactBean.getEmail();
		UpdateContactsAction.oldMappingID = contactBean.getMappingID();
		this.ID = UpdateContactsAction.oldID;
		this.firstName = UpdateContactsAction.existingFirstName;
		this.surname = UpdateContactsAction.existingSurname;
		this.phone = UpdateContactsAction.existingPhone;
		this.email = UpdateContactsAction.existingEmail;
		this.mappingID = UpdateContactsAction.oldMappingID;
		return "Success";
	}

	public String updateContact() throws ClassNotFoundException, SQLException {

		super.validate();
		if ((this.firstName.length() == 0) || (this.firstName == null)) {
			addFieldError("resultLabel", "Please enter the new first name.");
			return "input";
		} else if ((this.surname.length() == 0) || (this.surname == null)) {
			addFieldError("resultLabel", "Please enter the surname.");
			return "input";
		} else if ((this.phone.length() == 0) || (this.phone == null)) {
			addFieldError("resultLabel", "Please enter the updated phone number.");
			return "input";
		} else if ((this.email.length() == 0) || (this.email == null)) {
			addFieldError("resultLabel", "Please enter the new email.");
			return "input";
		} else if ((this.firstName.equals(UpdateContactsAction.existingFirstName))
				&& (this.surname.equals(UpdateContactsAction.existingSurname))
				&& (this.phone.equals(UpdateContactsAction.existingPhone))
				&& (this.email.equals(UpdateContactsAction.existingEmail))) {
			addFieldError("resultLabel", "Values are same as before. You had made no changes.");
			return "input";
		} else {
			this.mappingID = UpdateContactsAction.oldMappingID;
			this.ID = UpdateContactsAction.oldID;
			String result = Contacts_DAO.updateContactDB(this);
			addFieldError("resultLabel", result);
			UpdateContactsAction.existingFirstName = this.getFirstName();
			UpdateContactsAction.existingSurname = this.getSurname();
			UpdateContactsAction.existingPhone = this.getPhone();
			UpdateContactsAction.existingEmail = this.getEmail();
			return "Success";
		}
	}
}
