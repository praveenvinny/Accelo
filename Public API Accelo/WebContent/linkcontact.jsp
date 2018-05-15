<%--
  - Application Name: Public API
  - File name: viewallcontacts.jsp
  - Author(s): Praveen Vinny
  - Date: May 14, 2018
  - Description: To view all contacts.
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
			"http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<TITLE>Link Contact</TITLE>
</HEAD>
<BODY>
<H1 style="text-align: center;">Align a contact to a company</H1>
<s:form>
	<TABLE align="center" border="0">
		<TR>
			<TD><s:actionerror /></TD>
		</TR>
		<TR>
			<TD><s:actionmessage /></TD>
		</TR>
	</TABLE>
	<p>ID: <s:property value="contactBean.ID"></s:property></p>
	<p>First Name: <s:property value="contactBean.firstName"></s:property></p>
	<p>Surname: <s:property value="contactBean.surname"></s:property></p>
	<p>Email: <s:property value="contactBean.email"></s:property></p>
	<p>Phone: <s:property value="contactBean.phone"></s:property></p>

</br>
<p>Please click on the name of the company you would like to align from the list below:</p>
<table border = 1>
		<s:iterator value="companiesList">
		<tr>
	<td><s:property value="title"></s:property></td>
	<td><s:property value="website"></s:property></td>
<td>
      <s:url id="url" action="createContactMapping" >
           <s:param name="newCompanyID"><s:property value="companyID" /></s:param>
         </s:url>
         <s:a href="%{url}">Update</s:a>
         </td>
         </tr>
</s:iterator>
	</TABLE>
</s:form>
<p align="center"><s:label name="resultLabel" value=""></s:label></p>
<p><a href = "index.jsp">Home</a></p>
</BODY>
</HTML>