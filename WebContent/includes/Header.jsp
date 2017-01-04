<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<style type="text/css">
.navbar-center {
	display: inline-block;
	float: none;
	vertical-align: top;
}

.navbar-collapse-center {
	text-align: center;
}
</style>


<div class="bs-docs-header" id="content" tabindex="-1">
	<div class="container">
		<h1>高師大附中 線上選課系統</h1>
	</div>
</div>

<nav class="navbar navbar-default" role="navigation">
	<div class="container-fluid">
		<ul class="nav navbar-nav">
			<li><jsp:include page="div/SystemNow.jsp" /></li>
		</ul>

		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right">
				<li><a href="./"><span class="glyphicon glyphicon-home"
						aria-hidden="true"></span> 回首頁</a></li>
				<c:if test="${sessionScope.currentUser != null}">
					<c:if test="${sessionScope.currentUser.isAdmin}">
						<li><a href="./EditAppConfig"><span
								class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
								管理頁</a></li>
					</c:if>
					<li><a href="./Logout"><span
							class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
							${sessionScope.currentUser.username} 登出</a></li>
				</c:if>
			</ul>

		</div>
	</div>
</nav>



