package com.accelo.api.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import com.accelo.api.dao.Contacts_DAO;
import com.accelo.api.bean.ContactBean;

public class ContactsAction extends ActionSupport implements
ModelDriven<ContactBean> {

	public ContactBean contactBean = new ContactBean();
	
	List<ContactBean> contactsList;
	
	/**
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 * @return
	 */
	@Override
	public ContactBean getModel() {
		return contactBean;
	}

	public ContactsAction() {
		contactsList = new ArrayList<ContactBean>();
	}

	public String createContact() throws ClassNotFoundException, SQLException {

		super.validate();
		if ((contactBean.getFirstName().length() == 0) || (contactBean.getFirstName() == null)) {
			addFieldError("resultLabel", "Please enter the first name.");
			return "input";
		} else if ((contactBean.getSurname().length() == 0) || (contactBean.getSurname() == null)) {
			addFieldError("resultLabel", "Please enter the surname.");
			return "input";
		} else if ((contactBean.getPhone().length() == 0) || (contactBean.getPhone() == null)) {
			addFieldError("resultLabel", "Please enter the phone number.");
			return "input";
		} else if ((contactBean.getEmail().length() == 0) || (contactBean.getEmail() == null)) {
			addFieldError("resultLabel", "Please enter the email.");
			return "input";
		} else {
			String result = Contacts_DAO.insertContactIntoDB(contactBean);
			addFieldError("resultLabel", result);
			return "Success";
		}
	}

}
