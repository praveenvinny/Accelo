<%@ page import="java.sql.*"%>
<%@ page import="com.accelo.api.bean.ProjectBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create a project</title>
</head>
<body>
<h1 align = "center">Project Creation</h1>
<s:form action="projectCreation">
<table align = "center">
<tr>
	<td>
		<s:textfield label="Project name" name="title"></s:textfield>
	</td>
</tr>
<tr>
	<td>
		<s:textfield label="Description" name="description"></s:textfield>
	</td>
</tr>
<tr>
	<td>
		<s:textfield label="Company Name" name="companyName"></s:textfield>
	</td>
</tr>
<tr>
	<td>
		<s:textfield label="Contact Number" name="contactNumber"></s:textfield>
	</td>
</tr>
<tr>
	<td><s:label name="resultLabel" value=""></s:label></td>
</tr>
<tr>
	<td align="right"><s:submit value="Create Project"></s:submit> </td>
</tr>
</table>
</s:form>
<p><a href = "index.jsp">Back</a></p>
</body>
</html>