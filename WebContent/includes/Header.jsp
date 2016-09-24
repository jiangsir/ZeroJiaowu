<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>



<div class=container>
	<div class="jumbotron well">
		<h2>
			<a href="./" id="headerh1">高師大附中 線上選課系統</a>
		</h2>
	</div>
	<nav class="navbar navbar-default" role="navigation">
		<div class="container-fluid">
			<ul class="nav navbar-nav">
				<li><jsp:include page="div/SystemNow.jsp" /></li>
			</ul>

			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">

					<c:if test="${sessionScope.currentUser != null}">
						<c:if test="${sessionScope.currentUser.isAdmin}">
							<li><a href="./EditAppConfig"><span
									class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
									管理頁</a></li>
						</c:if>
						<li><a href="./Logout"><span
								class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
								離開</a></li>
					</c:if>
				</ul>

			</div>
		</div>
	</nav>

</div>

