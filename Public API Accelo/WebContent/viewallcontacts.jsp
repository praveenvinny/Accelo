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
<TITLE>View All Contacts</TITLE>
</HEAD>
<BODY>
<H1 style="text-align: center;">View All Contacts</H1>
<s:form>
	<TABLE align="center" border="0">
		<TR>
			<TD><s:actionerror /></TD>
		</TR>
		<TR>
			<TD><s:actionmessage /></TD>
		</TR>
	</TABLE>
	<TABLE align="center" BORDER="3">
		<TR>
			<TH>ID</TH>
			<TH>First Name</TH>
			<TH>Surname</TH>
			<TH>Email</TH>
			<TH>Phone</TH>
			<TH>Company</TH>
		</TR>
		<s:iterator value="contactsList">
<tr>
	<td><s:property value="ID"></s:property></td>
	<td><s:property value="firstName"></s:property></TD>
	<td><s:property value="surname"></s:property></TD>
	<td><s:property value="email"></s:property></TD>
	<td><s:property value="phone"></s:property></TD>
	<td>
      <s:url id="url" action="fetchContact" >
           <s:param name="mappingID"><s:property value="mappingID" /></s:param>
         </s:url>
         <s:a href="%{url}">Update</s:a>
	</TD>
	<td>
	<s:if test = "company!='UNASSIGNED'">
         <s:property value="company"></s:property>
      </s:if>
      <s:else>
      <s:url id="url" action="alignCompanies" >
           <s:param name="mappingID"><s:property value="mappingID" /></s:param>
         </s:url>
         <s:a href="%{url}">Link Company</s:a>
      </s:else>
	</TD>
	</tr>
</s:iterator>
	</TABLE>
</s:form>
<p><a href = "index.jsp">Back</a></p>
</BODY>
</HTML>