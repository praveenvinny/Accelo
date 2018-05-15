package com.accelo.api.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.accelo.api.action.AllProjects;
import com.accelo.api.action.ProjectsAction;
import com.accelo.api.bean.CompanyBean;
import com.accelo.api.util.ConnectDatabase;

public class Projects_DAO {

	public static String insertCompanyIntoDB(ProjectsAction projectBean) throws SQLException, ClassNotFoundException {
		String result = "Insertion failed!";
		ConnectDatabase.connectDB();

		String query = "Select ID from TA001_COMPANY A where upper(trim(A.TITLE)) =  ? and IS_ENABLED = ? group by ID";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, projectBean.getCompanyName().trim().toUpperCase());
		ps.setString(2, "Y");
		ResultSet rs = ps.executeQuery();
		rs.next();
		int companyID = rs.getInt(1);

		query = "Select ID, CONTACT_ID from TA002_CONTACT_DETAILS where PHONE = ?";
		ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, projectBean.getContactNumber());
		rs = ps.executeQuery();
		rs.next();
		int mappingID = rs.getInt(1);
		int contactID = rs.getInt(2);

		ConnectDatabase.connectDB();
		query = "INSERT INTO ACCELO.TA001_PROJECT (TITLE, COMPANY, CONTACT, DESCRIPTION, MAPPING_ID) VALUES (?, ?, ?, ?, ?)";
		ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, projectBean.getTitle());
		ps.setInt(2, companyID);
		ps.setInt(3, contactID);
		ps.setString(4, projectBean.getDescription());
		ps.setInt(5, mappingID);
		ps.executeUpdate();
		result = "Successfully inserted!";
		return result;
	}

	public static ArrayList<AllProjects> getProjectsList() throws SQLException, ClassNotFoundException {
		ArrayList<AllProjects> projectsList = new ArrayList<AllProjects>();
		ConnectDatabase.connectDB();
		Statement st = ConnectDatabase.con.createStatement();
		String query = "Select A.ID, A.TITLE, A.DESCRIPTION, B.FIRST_NAME ||' '|| B.SURNAME CONTACT, C.TITLE COMPANY, D.EMAIL, D.PHONE FROM TA001_PROJECT A, TA001_CONTACT B, TA001_COMPANY C, TA002_CONTACT_DETAILS D where A.CONTACT = B.ID and A.COMPANY = C.ID and D.ID = A.MAPPING_ID";
		ResultSet rs = st.executeQuery(query);
		AllProjects projectBean;
		while (rs.next()) {
			projectBean = new AllProjects();
			projectBean.setID(rs.getInt(1));
			projectBean.setTitle(rs.getString(2));
			projectBean.setDescription(rs.getString(3));
			projectBean.setContact(rs.getString(4));
			projectBean.setCompany(rs.getString(5));
			projectBean.setEmail(rs.getString(6));
			projectBean.setPhone(rs.getString(7));
			projectsList.add(projectBean);
		}
		return projectsList;
	}

	public static AllProjects getProjectByID(int projectID) throws SQLException, ClassNotFoundException {
		ConnectDatabase.connectDB();
		Statement st = ConnectDatabase.con.createStatement();
		String query = "Select A.ID, A.TITLE, A.DESCRIPTION, B.FIRST_NAME ||' '|| B.SURNAME CONTACT, C.TITLE COMPANY, D.EMAIL, D.PHONE FROM TA001_PROJECT A, TA001_CONTACT B, TA001_COMPANY C, TA002_CONTACT_DETAILS D where A.CONTACT = B.ID and A.COMPANY = C.ID and D.ID = A.MAPPING_ID and A.ID = "
				+ projectID;
		ResultSet rs = st.executeQuery(query);
		AllProjects projectBean;
		rs.next();
		projectBean = new AllProjects();
		projectBean.setID(rs.getInt(1));
		projectBean.setTitle(rs.getString(2));
		projectBean.setDescription(rs.getString(3));
		projectBean.setContact(rs.getString(4));
		projectBean.setCompany(rs.getString(5));
		projectBean.setEmail(rs.getString(6));
		projectBean.setPhone(rs.getString(7));
		return projectBean;
	}
	
	public static String updateProjectDB(AllProjects projectBean) throws SQLException, ClassNotFoundException {
		String result = "Update Failed!";
		ConnectDatabase.connectDB();
		
		
		String query = "Select count(1) from TA002_CONTACT_DETAILS A where upper(trim(A.EMAIL)) = ? and upper(trim(A.PHONE)) = ?";
		PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
		ps.setString(1, projectBean.getEmail().trim().toUpperCase());
		ps.setString(2, projectBean.getPhone().trim().toUpperCase());
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		
		if(count== 0) {
			return "No records exists in the database with the details provided.";
		} else {
			query = "Select A.ID from TA002_CONTACT_DETAILS A where upper(trim(A.EMAIL)) = ? and upper(trim(A.PHONE)) = ?";
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setString(1, projectBean.getEmail().trim().toUpperCase());
			ps.setString(2, projectBean.getPhone().trim().toUpperCase());
			rs = ps.executeQuery();
			rs.next();
			int mappingID = rs.getInt(1);
			
			query = "Select ID from TA001_COMPANY A where upper(trim(A.TITLE)) =  ? and IS_ENABLED = ?";
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setString(1, projectBean.getCompany().trim().toUpperCase());
			ps.setString(2, "Y");
			rs = ps.executeQuery();
			rs.next();
			int reqCompanyID = rs.getInt(1);	// We should check if this is the mapped ID.
			
			query = "Select COMPANY_ID, CONTACT_ID from TA002_CONTACT_COMPANY_MAPP A where A.CONTACT_ID = (Select CONTACT_ID from TA002_CONTACT_DETAILS where ID = ?)";
			ps = ConnectDatabase.con.prepareStatement(query);
			ps.setInt(1, mappingID);
			rs = ps.executeQuery();
			rs.next();
			int newCompanyID = rs.getInt(1);
			int contactID = rs.getInt(2);
			
			if(newCompanyID != reqCompanyID) {
				return "The company is not mapped with the contact.";
			} else {
				query= "Update TA001_PROJECT SET CONTACT = ? , MAPPING_ID = ? where ID = ?";
				ps = ConnectDatabase.con.prepareStatement(query);
				ps.setInt(1, contactID);
				ps.setInt(2, mappingID);
				ps.setInt(3, projectBean.getProjectID());
				ps.executeUpdate();
		        ConnectDatabase.con.close();
				result = "Contact successfully updated with the project.";
				return result;
			}
		}
		
	}
}
