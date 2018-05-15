package com.accelo.api.action;

import java.sql.SQLException;
import java.util.ArrayList;

import com.opensymphony.xwork2.ActionSupport;

import com.accelo.api.bean.ContactBean;
import com.accelo.api.dao.Contacts_DAO;

public class ViewAllContactsAction extends ActionSupport {
	private ArrayList<ContactBean> contactsList;
	private int mappingID;

	/**
	 * @return contactsList
	 */
	public ArrayList<ContactBean> getContactsList() {
		return contactsList;
	}

	/**
	 * @param contactsList
	 */
	public void setContactsList(ArrayList<ContactBean> contactsList) {
		this.contactsList = contactsList;
	}

	/**
	 * @return contactID
	 */
	public int getMappingID() {
		return mappingID;
	}

	/**
	 * @param contactID
	 */
	public void setMappingID(int mappingID) {
		this.mappingID = mappingID;
	}

	public String execute() {
		String result = "FAIL";
		try {
			contactsList = Contacts_DAO.fetchContactsFromDB();
			if(contactsList.isEmpty()==true) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}
		} catch (ClassNotFoundException eClassNotFoundException) {
			result = "FAIL";
			addActionError("Unable to process! Please try again!");
		} catch (SQLException eSException) {
			result = "FAIL";
			addActionError("Trouble accessing database! Please try again!");
		}
		return result;
	}

}
