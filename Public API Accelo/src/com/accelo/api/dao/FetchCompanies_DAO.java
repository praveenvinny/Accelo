/**
 * Assignment done for the job at Accelo
 *
 * File Name       : FetchCompanies_DAO.java
 *
 * Description     : Manage the database operations related to company details.
 *
 * Version         : 1.0.0.
 *
 * Created Date    : May 11, 2018
 * 
 * Created By 	   : Praveen Vinny
 */

package com.accelo.api.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.accelo.api.bean.CompanyBean;
import com.accelo.api.bean.CompanyDetailsBean;
import com.accelo.api.bean.ContactBean;
import com.accelo.api.util.ConnectDatabase;

/**
 * This class defines the operations required to obtain the details related to a company from the DB.
 * @author Praveen Vinny
 *
 */
public class FetchCompanies_DAO {

	/**
	 * The following method creates a list with the details of all active companies in the database.
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static List<CompanyBean> getCompaniesFromDB() throws ClassNotFoundException, SQLException {
		List<CompanyBean> companiesList = new ArrayList<CompanyBean>(); // List to store company details.
		ConnectDatabase.connectDB();
		Statement st = ConnectDatabase.con.createStatement();
		String query = "SELECT DISTINCT ID, title, WEBSITE FROM ACCELO.TA001_COMPANY where IS_ENABLED = 'Y'";
		ResultSet rs = st.executeQuery(query);
		CompanyBean companyBean;	// A new object is required each time to get values assigned.
		while (rs.next()) {
			companyBean = new CompanyBean();	// A new object is created by invoking the constructor.
			companyBean.setCompanyID(rs.getInt(1));
			companyBean.setTitle(rs.getString(2));
			companyBean.setWebsite(rs.getString(3));
			companiesList.add(companyBean);	// Object is pushed to the list post assignment of values.
		}
		return companiesList;
	}

	/**
	 * The details corresponding to a particular company are fetched from DB by this method.
	 * @param companyID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static CompanyDetailsBean getCompanyFromDB(int companyID) throws ClassNotFoundException, SQLException {
		ConnectDatabase.connectDB();
		String query = "Select ID, TITLE, WEBSITE from TA001_COMPANY WHERE ID = ?";
		
		/**
		 * PreparedStatement is used to prevent SQL injections and to assign values to the query.
		 * This query will fetch the details such as the company ID, title and website.
		 */
		PreparedStatement preparedStatement = ConnectDatabase.con.prepareStatement(query);
		preparedStatement.setInt(1, companyID);
		ResultSet rs = preparedStatement.executeQuery();
		CompanyDetailsBean companyDetailsBean = new CompanyDetailsBean();
		rs.next();

		companyDetailsBean.setCompanyID(rs.getInt(1));
		companyDetailsBean.setTitle(rs.getString(2));
		companyDetailsBean.setWebsite(rs.getString(3));
		
		/**
		 * A contact list needs to be created by fetching the details of all contacts associated with
		   this company.
		 * 
		 * The following method obtains the list of contacts.
		 */
		
		List<ContactBean> contactsList = new ArrayList<ContactBean>();
		
		query = "Select A.FIRST_NAME, A.SURNAME, B.EMAIL, B.PHONE FROM TA001_COMPANY X, TA001_CONTACT A,\r\n" + 
				"TA002_CONTACT_DETAILS B, TA002_CONTACT_COMPANY_MAPP C WHERE A.ID = B.CONTACT_ID AND C.CONTACT_ID = A.ID AND C.MAPPING_ID = B.ID AND C.COMPANY_ID = X.ID\r\n" + 
				"AND X.IS_ENABLED = ? AND C.IS_ENABLED = ? AND C.COMPANY_ID = ?";
		preparedStatement = ConnectDatabase.con.prepareStatement(query);
		preparedStatement.setString(1, "Y");
		preparedStatement.setString(2, "Y");
		preparedStatement.setInt(3, companyID);
		rs = preparedStatement.executeQuery();
		ContactBean contactBean;
		while (rs.next()) {
			contactBean = new ContactBean();
			contactBean.setFirstName(rs.getString(1));
			contactBean.setSurname(rs.getString(2));
			contactBean.setEmail(rs.getString(3));
			contactBean.setPhone(rs.getString(4));	
			contactsList.add(contactBean);
		}

		companyDetailsBean.setContactsList(contactsList);
		return companyDetailsBean;
	}

	/**
	 * This method is used to create a new company in the database.
	 * @param companyBean
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static String insertCompanyIntoDB(CompanyBean companyBean) throws SQLException, ClassNotFoundException {
		String result = "Insertion failed!";	// Default value of the return message.
		ConnectDatabase.connectDB();
		
		/**
		 * Checking if the company already exists in the database.
		 */
		String query= "Select count(1) from ACCELO.TA001_COMPANY where upper(trim(title)) = ?"
				+ "and TA001_COMPANY.IS_ENABLED = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, companyBean.getTitle().toUpperCase().trim());
		ps.setString(2, "Y");
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);	// The number of companies gets returned to this variable.
		
		if(count > 0) {	
			// If more than one value exists in the database.
			result = "Company already exists with the name as " + companyBean.getTitle();
			return result;
		} else {
			// Checking if a company exists but in the disabled state in the database.
			query= "Select count(1) from ACCELO.TA001_COMPANY where upper(trim(title)) = ? "
					+ "and TA001_COMPANY.IS_ENABLED = ?";
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setString(1, companyBean.getTitle().toUpperCase().trim());
			ps.setString(2, "N");
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
	        ConnectDatabase.con.close();
			
			if(count > 0) {
				// If a company exists in the database, but was disabled, just enable that one.
				ConnectDatabase.connectDB();
				query= "UPDATE ACCELO.TA001_COMPANY SET IS_ENABLED = ? where upper(trim(title)) = ? "
						+ "and TA001_COMPANY.IS_ENABLED = ?";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, "Y");
				ps.setString(2, companyBean.getTitle().toUpperCase().trim());
				ps.setString(3, "N");
				ps.executeUpdate();
		        ConnectDatabase.con.close();
				result = "The system disabled company with the title as " + companyBean.getTitle() + " is enabled again.";
				return result;
			} else {
				/**
				 * If a company doesn't exist at all, create a new one in the database.
				 */
				ConnectDatabase.connectDB();
				query="INSERT INTO ACCELO.TA001_COMPANY (TITLE, WEBSITE) "
						+ "VALUES(?,?)";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, companyBean.getTitle());
				ps.setString(2, companyBean.getWebsite());
				ps.executeUpdate();
				
				/**
				 * Obtaining the ID and title of the company inserted into the database.
				 */
				query= "Select id, title from ACCELO.TA001_COMPANY where upper(trim(title)) = ? "
						+ "and TA001_COMPANY.IS_ENABLED = ?";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, companyBean.getTitle().toUpperCase().trim());
				ps.setString(2, "Y");
				rs = ps.executeQuery();
				rs.next();
				int id = rs.getInt(1);
				String companyTitle = rs.getString(2);
				result = "Company created with title as "+ companyTitle + " and ID "+id;
				ConnectDatabase.con.close();
				return result;
			}
		}
	}

	/**
	 * The following method is used to update the details corresponding to a company.
	 * @param companyBean
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static String updateCompanyDB(CompanyBean companyBean) throws ClassNotFoundException, SQLException {
		String result = "Update failed!";
		ConnectDatabase.connectDB();
		
		/**
		 * Checking if the company ID exists in the database.
		 * The incorrect ID cannot come here because the value is set by user's click on company.
		 * But this can happen if the value is entered manually by the user.
		 */
		String query= "Select count(1) from ACCELO.TA001_COMPANY where id = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setInt(1, companyBean.getCompanyID());
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		if(count == 1) {
			// Update the values corresponding to the ID as soon as the matching ID is found.
			query= "UPDATE ACCELO.TA001_COMPANY SET title = ?, website = ? where ID = ? ";
			ConnectDatabase.connectDB();
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setString(1, companyBean.getTitle());
			ps.setString(2, companyBean.getWebsite());
			ps.setInt(3, companyBean.getCompanyID());
			ps.executeUpdate();
	        ConnectDatabase.con.close();
	        result = "Company with the ID: "+companyBean.getCompanyID() + " got updated.";
			return result;
		} else {
			// If the count is 0, ID is not found. Then, display the message as the ID is not found.
				result = "Company with the ID: "+companyBean.getCompanyID() + " not found.";
				ConnectDatabase.con.close();
				return result;
		}
	}

	/**
	 * This method is used to populate the list of all companies from the database.
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<CompanyBean> getCompanyNamesFromDB() throws SQLException, ClassNotFoundException {
		// The following array list is used to store the details of all companies.
		ArrayList<CompanyBean> companiesList = new ArrayList<CompanyBean> ();
		ConnectDatabase.connectDB();
		// Fetching all records from the database where the details are enabled.
		String query = "Select distinct ID, TITLE, WEBSITE from TA001_COMPANY WHERE IS_ENABLED = ? ";
		
		PreparedStatement preparedStatement = ConnectDatabase.con.prepareStatement(query);
		preparedStatement.setString(1, "Y");
		ResultSet rs = preparedStatement.executeQuery();
		
		CompanyBean companyBean;	// Each row of result set is set to new instances of the bean.
		
		while (rs.next()) {
			companyBean = new CompanyBean();
			companyBean.setCompanyID(rs.getInt(1));
			companyBean.setTitle(rs.getString(2));
			companiesList.add(companyBean);
		}

		return companiesList;	// List of companies are returned.
	}

}
