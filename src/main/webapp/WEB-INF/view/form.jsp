<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<script src="${staticPath}/vendor/jquery.min.js"></script>
<script src="${staticPath}/vendor/modernizr-custom.min.js"></script>
<link href="${staticPath}/style.css" rel="stylesheet" type="text/css">
<script src="${staticPath}/utils.js"></script>
<link href="${staticPath}/custom/${user}/style.css" rel="stylesheet" type="text/css">
<script src="${staticPath}/custom/${user}/script.js"></script>
<meta charset="utf-8">
<title>Welcome</title>
</head>
<body>
	<form method="post">
		<textarea name="query" style="width: 100%; height: 50%"></textarea>
	</form>
</body>
</html>
