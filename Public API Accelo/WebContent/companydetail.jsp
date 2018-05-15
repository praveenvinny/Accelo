<%@ page import="java.sql.*"%>
<%@ page import="com.accelo.api.bean.CompanyDetailsBean"%>
<%@ page import="com.accelo.api.bean.ContactBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Company Details</title>
</head>
<body>
<s:form>
			<h1>
				<s:property value="title"></s:property>
			</h1>
			<p>
				<b>Website:</b> <s:property value="website"></s:property>
			</p>
			<p>
				<b>ID:</b>
				<s:property value="companyID"></s:property>
			</p>
			</br>
			<p>The contact details are as follows: </p>
			<p><s:label name="resultLabel" value=""></s:label></p>
			<table>
				<s:iterator value="contactsList">
					<tr>
						<td><b>First Name: </b><s:property value="firstName"></s:property></td>
						<td><b>Surname: </b><s:property value="surname"></s:property></td>
						<td><b>Email: </b><s:property value="email"></s:property></td>
						<td><b>Phone: </b><s:property value="phone"></s:property></td>
					</tr>
				</s:iterator>
			</table>
	</s:form>
</body>
</html>