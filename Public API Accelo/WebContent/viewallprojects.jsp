<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@taglib uri="/struts-tags" prefix="s"%>
<title>View all projects</title>
</head>
<body>
<H1 style="text-align: center;">View all projects</H1>
<s:form>
	<TABLE align="center" border="0">
		<TR>
			<TD><s:label name="resultLabel" value=""></s:label></TD>
		</TR>
	</TABLE>
	<TABLE align="center" BORDER="3">
		<TR>
			<TH>ID</TH>
			<TH>Title</TH>
			<TH>Description</TH>
			<TH>Contact</TH>
			<TH>Company</TH>
			<TH>Email</TH>
			<TH>Phone</TH>
			<TH>View/Update</TH>
		</TR>
		<s:iterator value="projectsList">
<tr>
	<td><s:property value="ID"></s:property></td>
	<td><s:property value="title"></s:property></TD>
	<td><s:property value="description"></s:property></TD>
	<td><s:property value="contact"></s:property></TD>
	<td><s:property value="company"></s:property></TD>
	<td><s:property value="email"></s:property></TD>
	<td><s:property value="phone"></s:property></TD>
	<td>
      <s:url id="url" action="viewProject" >
           <s:param name="projectID"><s:property value="ID" /></s:param>
         </s:url>
         <s:a href="%{url}">View Project</s:a>
	</TD>
	</tr>
</s:iterator>
	</TABLE>
</s:form>
<p><a href = "index.jsp">Back</a></p>
</body>
</html>