<%@ page import="java.sql.*"%>
<%@ page import="com.accelo.api.bean.CompanyBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>Company/Contact API</title>   
</head>
<body>
<p><a href="createCompany.jsp">Create a company</a></p>
<p><a href="createContact.jsp">Create a contact</a></p>
<p><a href="ViewAllContacts">Display Contacts</a></p>
<p><a href="displayCompanies">Display Companies</a></p>
<p><a href="createproject.jsp">Create a project</a></p>
<p><a href="ViewAllProjects">Display Projects</a></p>
<s:form id="formOne">
<table align="center" border="3">
<s:iterator value="companiesList">
	<tr>
		<td><s:property value="title"></s:property></td>
		<td><s:property value="website"></s:property></td>
		<td><s:property value="companyID"></s:property></td>
		<td><s:url id="url" action="clickCompany" >
           <s:param name="companyID"><s:property value="companyID" /></s:param>
         </s:url>
         <s:a href="%{url}">View</s:a></td>
         <td><s:url id="url" action="fetchCompany" >
           <s:param name="companyID"><s:property value="companyID" /></s:param>
         </s:url>
         <s:a href="%{url}">Update</s:a></td>
	</tr>
</s:iterator>
</table>
</s:form>
<s:label name="resultLabel" value=""></s:label>
</body>
</html>