<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<div id="header">
	<h1>
		<a href="./" id="headerh1">高師大附中 線上選課系統</a>
	</h1>
</div>
<div id="menu">
	<ul>
		<c:if test="${sessionScope.onlineUser!=null}">
			<li>${sessionScope.onlineUser.username}您好！</li>
			<li class="selected"><a href="./Logout">離開</a></li>
		</c:if>
	</ul>
</div>
<jsp:include page="div/SystemNow.jsp" />