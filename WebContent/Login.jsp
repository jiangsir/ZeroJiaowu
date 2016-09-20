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
	<div id="main">
		<jsp:include page="includes/Header.jsp" />
		<div id="panel">
			<br />
			<div class="right">
				<h3>確認您的身份</h3>
				<p>請輸入您的學號以及身份證字號</p>
				<p style="font-size: large; font-weight: bold; color: red;">${sessionScope.LoginMessage}</p>
				<form action="Login" method="post">
					<h4>
						<label for="item_a">學號:</label>
					</h4>
					<p class="input">
						<input name="account" type="password" id="account" size="50" />
					</p>
					<h4>
						<label for="item_b">身份證字號:</label>
					</h4>
					<p class="input">
						<input name="passwd" type="password" id="passwd" size="50" />
					</p>
					<p class="button">
						<button name="submit" type="submit">送出</button>
					</p>
				</form>
			</div>
			<!--            <a
                href="https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=156955164629.apps.googleusercontent.com&redirect_uri=http://apps.nknush.kh.edu.tw/ZeroJiaowu/OAuth2Callback&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email">以Google帳戶登入</a>
 -->
		</div>
		<jsp:include page="includes/Footer.jsp" />
	</div>
</body>
</html>
