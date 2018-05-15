package com.accelo.api.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.accelo.api.action.UpdateContactsAction;
import com.accelo.api.bean.ContactBean;
import com.accelo.api.util.ConnectDatabase;

public class Contacts_DAO {

	public static ArrayList<ContactBean> fetchContactsFromDB() throws SQLException, ClassNotFoundException {
		ArrayList<ContactBean> contactsList = new ArrayList<ContactBean>();
		ConnectDatabase.connectDB();
		String query = "SELECT DISTINCT X.*, NVL(Z.TITLE, ?) Company FROM (SELECT B.ID, B.FIRST_NAME, B.SURNAME, A.EMAIL, A.PHONE, A.ID MAPPING_ID "
				+ "FROM TA002_CONTACT_DETAILS A, TA001_CONTACT B WHERE A.CONTACT_ID = B.ID AND B.IS_ENABLED = ?) X, "
				+ "TA002_CONTACT_COMPANY_MAPP Y, TA001_COMPANY Z WHERE Z.ID(+) = Y.COMPANY_ID AND Y.MAPPING_ID (+) = X.MAPPING_ID AND Y.CONTACT_ID(+) = X.ID";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, "UNASSIGNED");
		ps.setString(2, "Y");
		ResultSet rs = ps.executeQuery();
		ContactBean contactBean;
		while (rs.next()) {
			contactBean = new ContactBean();
			contactBean.setID(rs.getInt(1));
			contactBean.setFirstName(rs.getString(2));
			contactBean.setSurname(rs.getString(3));
			contactBean.setEmail(rs.getString(4));
			contactBean.setPhone(rs.getString(5));
			contactBean.setMappingID(rs.getInt(6));
			contactBean.setCompany(rs.getString(7));
			contactsList.add(contactBean);
		}
		return contactsList;
	}

	public static String insertContactIntoDB(ContactBean contactBean) throws SQLException, ClassNotFoundException {
		String result = "Insertion failed!";
		ConnectDatabase.connectDB();

		String query = "Select count(1), ID from ACCELO.TA001_CONTACT where upper(trim(FIRST_NAME)) = ? "
				+ "and upper(trim(SURNAME)) = ? and TA001_CONTACT.IS_ENABLED = ? group by ID";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, contactBean.getFirstName().toUpperCase().trim());
		ps.setString(2, contactBean.getSurname().toUpperCase().trim());
		ps.setString(3, "Y");
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		int recordID = rs.getInt(2);

		if (count > 0) {
			query = "Select count(ID) from ACCELO.TA002_CONTACT_DETAILS where upper(trim(EMAIL)) = ? and upper(trim(PHONE)) = ? and CONTACT_ID = ?";
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setString(1, contactBean.getEmail().toUpperCase().trim());
			ps.setString(2, contactBean.getPhone().toUpperCase().trim());
			ps.setInt(3, recordID);
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1); // Number of records.
			if (count > 0) {
				result = "Contact already exists with the first name as \'" + contactBean.getFirstName()
						+ "\' and last name as \'" + contactBean.getSurname() + "\'.";
			} else {
				query = "INSERT INTO ACCELO.TA002_CONTACT_DETAILS (EMAIL, PHONE, CONTACT_ID) VALUES (?, ?, ?)";
				ConnectDatabase.connectDB();
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, contactBean.getEmail());
				ps.setString(2, contactBean.getPhone());
				ps.setInt(3, recordID);
				ps.executeUpdate();
				result = "New email and phone inserted successfully!";
			}
			return result;
		} else {
			query = "Select count(1) from ACCELO.TA001_CONTACT where upper(trim(FIRST_NAME)) = ? "
					+ "and upper(trim(SURNAME)) = ? and TA001_CONTACT.IS_ENABLED = ?";
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setString(1, contactBean.getFirstName().toUpperCase().trim());
			ps.setString(2, contactBean.getSurname().toUpperCase().trim());
			ps.setString(3, "N");
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);

			if (count > 0) {
				ConnectDatabase.connectDB();
				query = "UPDATE ACCELO.TA001_CONTACT SET IS_ENABLED = ? where upper(trim(FIRST_NAME)) = ? "
						+ "and upper(trim(SURNAME)) = ? and TA001_CONTACT.IS_ENABLED = ?";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, "Y");
				ps.setString(2, contactBean.getFirstName().toUpperCase().trim());
				ps.setString(3, contactBean.getSurname().toUpperCase().trim());
				ps.setString(4, "N");
				ps.executeUpdate();
				query = "Select ID from ACCELO.TA001_CONTACT where upper(trim(FIRST_NAME)) = ? "
						+ "and upper(trim(SURNAME)) = ? and TA001_CONTACT.IS_ENABLED = ?";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, contactBean.getFirstName().toUpperCase().trim());
				ps.setString(2, contactBean.getSurname().toUpperCase().trim());
				ps.setString(3, "Y");
				rs = ps.executeQuery();
				rs.next();
				int id = rs.getInt(1);
				query = "Select count(ID), ID from ACCELO.TA002_CONTACT_DETAILS where upper(trim(EMAIL)) = ? and upper(trim(PHONE)) = ? and CONTACT_ID = ? group by ID";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, contactBean.getEmail().toUpperCase().trim());
				ps.setString(2, contactBean.getPhone().toUpperCase().trim());
				ps.setInt(3, id);
				rs = ps.executeQuery();
				rs.next();
				count = rs.getInt(1); // Number of records.
				id = rs.getInt(2); // First one is the count. Second one is the ID.
				/**
				 * If the count is greater than zero, it means that record already exists. We
				 * don't have to do anything. If it doesn't exist, we have to write to a DB.
				 */
				if (count == 0) {
					query = "INSERT INTO ACCELO.TA002_CONTACT_DETAILS (EMAIL, PHONE, CONTACT_ID) VALUES (?, ?, ?)";
					ConnectDatabase.connectDB();
					ps = ConnectDatabase.con.prepareStatement(query);
					ps.setString(1, contactBean.getEmail());
					ps.setString(2, contactBean.getPhone());
					ps.setInt(3, id);
					ps.executeUpdate();
					result = "Contact saved successfully!";
				} else {
					result = "Contact enabled successfully!";
				}
			} else {
				query = "INSERT INTO ACCELO.TA001_CONTACT (FIRST_NAME, SURNAME) VALUES (?, ?)";
				ConnectDatabase.connectDB();
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, contactBean.getFirstName());
				ps.setString(2, contactBean.getSurname());
				ps.executeUpdate();

				query = "Select ID from ACCELO.TA001_CONTACT where upper(trim(FIRST_NAME)) = ? "
						+ "and upper(trim(SURNAME)) = ? and TA001_CONTACT.IS_ENABLED = ?";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, contactBean.getFirstName().toUpperCase().trim());
				ps.setString(2, contactBean.getSurname().toUpperCase().trim());
				ps.setString(3, "Y");
				rs = ps.executeQuery();
				rs.next();
				int id = rs.getInt(1);

				query = "INSERT INTO ACCELO.TA002_CONTACT_DETAILS (EMAIL, PHONE, CONTACT_ID) VALUES (?, ?, ?)";
				ConnectDatabase.connectDB();
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, contactBean.getEmail());
				ps.setString(2, contactBean.getPhone());
				ps.setInt(3, id);
				ps.executeUpdate();
				result = "Contact saved successfully!";
			}

			ConnectDatabase.con.close();
			return result;

		}
	}

	public static ContactBean getContactFromDB(int mappingID) throws SQLException {
		String query = "SELECT DISTINCT X.*, NVL(Z.TITLE, ?) Company FROM (SELECT B.ID, B.FIRST_NAME, B.SURNAME, A.EMAIL, A.PHONE, A.ID MAPPING_ID "
				+ "FROM TA002_CONTACT_DETAILS A, TA001_CONTACT B WHERE A.CONTACT_ID = B.ID AND B.IS_ENABLED = ?) X, "
				+ "TA002_CONTACT_COMPANY_MAPP Y, TA001_COMPANY Z WHERE Z.ID(+) = Y.COMPANY_ID AND Y.MAPPING_ID (+) = X.MAPPING_ID AND Y.CONTACT_ID(+) = X.ID AND X.MAPPING_ID = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, "UNASSIGNED");
		ps.setString(2, "Y");
		ps.setInt(3, mappingID);
		ResultSet rs = ps.executeQuery();
		rs.next();
		ContactBean contactBean = new ContactBean();
		contactBean.setID(rs.getInt(1));
		contactBean.setFirstName(rs.getString(2));
		contactBean.setSurname(rs.getString(3));
		contactBean.setEmail(rs.getString(4));
		contactBean.setPhone(rs.getString(5));
		contactBean.setMappingID(rs.getInt(6));
		contactBean.setCompany(rs.getString(7));
		return contactBean;
	}

	public static String createCompContactMapping(int companyID, int contactID, int mappingID) throws ClassNotFoundException, SQLException {
		String result = "Updation not done!";
		String query = "Select count(ID) from TA002_CONTACT_COMPANY_MAPP A where A.COMPANY_ID = ? and A.CONTACT_ID = ? and A.MAPPING_ID = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setInt(1, companyID);
		ps.setInt(2, contactID);
		ps.setInt(3, mappingID);
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		if(count>0) {
			result = "Company is already mapped with the contact.";
		} else {		
		query = "INSERT INTO ACCELO.TA002_CONTACT_COMPANY_MAPP (COMPANY_ID, CONTACT_ID, MAPPING_ID) VALUES (?, ?, ?)";
		ConnectDatabase.connectDB();
		ps = ConnectDatabase.con.prepareStatement(query);
		ps.setInt(1, companyID);
		ps.setInt(2, contactID);
		ps.setInt(3, mappingID);
		ps.executeUpdate();
		result = "Contact and Company was mapped successfully!";
		}
		
		return result;
	}

	public static ContactBean fetchContactFromDB(int mappingID) throws SQLException {
		String query = "Select A.ID CONTACT_ID, A.FIRST_NAME, A.SURNAME, B.EMAIL, B.PHONE, B.ID MAPPING_ID from TA001_CONTACT A, TA002_CONTACT_DETAILS B where A.ID = B.CONTACT_ID and A.IS_ENABLED = ? and B.ID = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, "Y");
		ps.setInt(2, mappingID);
		ResultSet rs = ps.executeQuery();
		rs.next();
		ContactBean contactBean = new ContactBean();
		contactBean.setID(rs.getInt(1));
		contactBean.setFirstName(rs.getString(2));
		contactBean.setSurname(rs.getString(3));
		contactBean.setEmail(rs.getString(4));
		contactBean.setPhone(rs.getString(5));
		contactBean.setMappingID(rs.getInt(6));
		return contactBean;
	}

	public static String updateContactDB(UpdateContactsAction updateContactsAction) throws SQLException, ClassNotFoundException {
		String result = "Update failed!";
		ConnectDatabase.connectDB();
		System.out.println("In here with the ID: "+updateContactsAction.getID());
		
		String query= "Select count(1) from ACCELO.TA001_CONTACT where id = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setInt(1, updateContactsAction.getID());
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		if(count == 1) {
			query= "UPDATE ACCELO.TA001_CONTACT SET FIRST_NAME = ?, SURNAME = ? where ID = ? ";
			ConnectDatabase.connectDB();
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setString(1, updateContactsAction.getFirstName());
			ps.setString(2, updateContactsAction.getSurname());
			ps.setInt(3, updateContactsAction.getID());
			ps.executeUpdate();
	        ConnectDatabase.con.close();
	        query= "UPDATE ACCELO.TA002_CONTACT_DETAILS SET EMAIL = ?, PHONE = ? where ID = ? ";
			ConnectDatabase.connectDB();
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setString(1, updateContactsAction.getEmail());
			ps.setString(2, updateContactsAction.getPhone());
			ps.setInt(3, updateContactsAction.getMappingID());
			ps.executeUpdate();
	        ConnectDatabase.con.close();
	        result = "Contact with the ID: "+updateContactsAction.getMappingID() + " got updated.";
			return result;
		} else {
				result = "Contact with the ID: "+updateContactsAction.getID() + " not found.";
				ConnectDatabase.con.close();
				return result;
		}
	}

}
