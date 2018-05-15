<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@taglib uri="/struts-tags" prefix="s"%>
<title>View/Update Project</title>
</head>
<body>
<h1 align = "center">Update Project Details</h1>
<s:form action="updateProject">
<table align = "center">
<tr>
	<td>
		<b>ID:</b> <s:property value="ID"></s:property>
	</td>
</tr>
<tr>
	<td>
	<b>Project Title:</b> <s:property value="title"></s:property>
	</td>
</tr>
<tr>
	<td>
	<b>Description:</b> <s:property value="description"></s:property>
	</td>
</tr>
<tr>
	<td>
		<b>Contact:</b> <s:property value="contact"></s:property>
	</td>
</tr>
</table>
<table align = "center">
<tr><p align="center">The contacts against this project can be updated.</br>In order to do this, 
please mention the phone number and email ID of the new contact </br>and click
on update. But please ensure that the new contact</br> is mapped to the company.</p></tr>
<tr>
	<td>
		<s:textfield label="Email" name="email"></s:textfield>
	</td>
</tr>
<tr>
	<td>
		<s:textfield label="Phone" name="phone"></s:textfield>
	</td>
</tr>
<tr>
	<td align="right"><s:submit value="Update Contact for the Project"></s:submit> </td>
</tr>
<tr>
	<td><s:label name="resultLabel" value=""></s:label></td>
</tr>
</table>
</s:form>
<p><a href = "index.jsp">Back</a></p>
</body>
</html>