<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<jsp:include page="includes/head.jsp" />

</head>
<body>
	<jsp:include page="includes/Header.jsp" />
	<div class="row">
		<div class="col-md-4"></div>
		<div class="col-md-4">
			<form class="form-horizontal" role="form" action="Login"
				method="post">
				<h3>確認您的身份</h3>
				<p>請輸入您的學號以及身份證字號</p>
				<div class="form-group">

					<label for="inputEmail3" class="col-sm-3 control-label">
						學號: </label>
					<div class="col-sm-9">
						<input name="account" type="password" class="form-control" />
					</div>
				</div>
				<div class="form-group">

					<label for="inputPassword3" class="col-sm-3 control-label">
						身份證字號: </label>
					<div class="col-sm-9">
						<input name="passwd" type="password" class="form-control" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-default">登入</button>
					</div>
				</div>
			</form>
		</div>
		<div class="col-md-4"></div>
	</div>

	<jsp:include page="includes/Footer.jsp" />

</body>
</html>
