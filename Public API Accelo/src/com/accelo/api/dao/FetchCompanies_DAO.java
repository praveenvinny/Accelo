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

public class FetchCompanies_DAO {

	public static List<CompanyBean> getCompaniesFromDB() throws ClassNotFoundException, SQLException {
		List<CompanyBean> companiesList = new ArrayList<CompanyBean>();
		ConnectDatabase.connectDB();
		Statement st = ConnectDatabase.con.createStatement();
		String query = "SELECT DISTINCT ID, title, WEBSITE FROM ACCELO.TA001_COMPANY where IS_ENABLED = 'Y'";
		ResultSet rs = st.executeQuery(query);
		CompanyBean companyBean;
		while (rs.next()) {
			companyBean = new CompanyBean();
			companyBean.setCompanyID(rs.getInt(1));
			companyBean.setTitle(rs.getString(2));
			companyBean.setWebsite(rs.getString(3));
			companiesList.add(companyBean);
		}
		return companiesList;
	}

	public static CompanyDetailsBean getCompanyFromDB(int companyID) throws ClassNotFoundException, SQLException {
		ConnectDatabase.connectDB();
		System.out.println("Inside the fetch query.");
		String query = "Select ID, TITLE, WEBSITE from TA001_COMPANY WHERE ID = ?";
		
		PreparedStatement preparedStatement = ConnectDatabase.con.prepareStatement(query);
		preparedStatement.setInt(1, companyID);
		ResultSet rs = preparedStatement.executeQuery();
		CompanyDetailsBean companyDetailsBean = new CompanyDetailsBean();
		rs.next();

		companyDetailsBean.setCompanyID(rs.getInt(1));
		companyDetailsBean.setTitle(rs.getString(2));
		companyDetailsBean.setWebsite(rs.getString(3));
		
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

	public static String insertCompanyIntoDB(CompanyBean companyBean) throws SQLException, ClassNotFoundException {
		String result = "Insertion failed!";
		ConnectDatabase.connectDB();
		
		String query= "Select count(1) from ACCELO.TA001_COMPANY where upper(trim(title)) = ?"
				+ "and TA001_COMPANY.IS_ENABLED = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, companyBean.getTitle().toUpperCase().trim());
		ps.setString(2, "Y");
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		if(count > 0) {
			result = "Company already exists with the name as " + companyBean.getTitle();
			return result;
		} else {
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
				ConnectDatabase.connectDB();
				query="INSERT INTO ACCELO.TA001_COMPANY (TITLE, WEBSITE) "
						+ "VALUES(?,?)";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setString(1, companyBean.getTitle());
				ps.setString(2, companyBean.getWebsite());
				ps.executeUpdate();
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

	public static String updateCompanyDB(CompanyBean companyBean) throws ClassNotFoundException, SQLException {
		String result = "Update failed!";
		ConnectDatabase.connectDB();
		System.out.println("In here with the ID: "+companyBean.getCompanyID());
		
		String query= "Select count(1) from ACCELO.TA001_COMPANY where id = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setInt(1, companyBean.getCompanyID());
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		if(count == 1) {
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
				result = "Company with the ID: "+companyBean.getCompanyID() + " not found.";
				ConnectDatabase.con.close();
				return result;
		}
	}

	public static ArrayList<CompanyBean> getCompanyNamesFromDB() throws SQLException, ClassNotFoundException {
		ArrayList<CompanyBean> companiesList = new ArrayList<CompanyBean> ();
		ConnectDatabase.connectDB();
		String query = "Select distinct ID, TITLE, WEBSITE from TA001_COMPANY WHERE IS_ENABLED = ? ";
		
		PreparedStatement preparedStatement = ConnectDatabase.con.prepareStatement(query);
		preparedStatement.setString(1, "Y");
		ResultSet rs = preparedStatement.executeQuery();
		
		CompanyBean companyBean;
		
		while (rs.next()) {
			companyBean = new CompanyBean();
			companyBean.setCompanyID(rs.getInt(1));
			companyBean.setTitle(rs.getString(2));
			companiesList.add(companyBean);
		}

		return companiesList;
	}

}
