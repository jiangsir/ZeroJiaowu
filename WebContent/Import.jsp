<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="includes/head.jsp" />

</head>
<body>
	<div id="main">
		<jsp:include page="includes/Header.jsp" />
		<div id="content">
			<h3>匯入 CSV 資料(User 資料):</h3>
			<form id="form1" method="post" action="">
				<p>
					匯入的 User 若不存在則會新增，若存在則更新。<br /> 可用欄位：*account, username, passwd,
					number, comment, usergroup (此處 account 為主鍵，不可重複，重複的話，則進行 update)
				</p>
				<p>使用方法：第一行負責指定欄位名稱，可以自由任意組合以上幾個欄位，只要下面的資料對的上即可。下面的資料，如果欄位數目多於第一行所指定的欄位，就會被略過。</p>
				<p>例：</p>
				<pre>
      number, account, username, passwd, comment
      01,9920101,吳宜儒,A111111111,高一仁(女)
      02,9920102,吳靜姝,A222222222,高一仁(女)
      03,9920103,周品薰,A333333333,高一仁(女)</pre>
				<div>
					<textarea name="csvdata" cols="80" rows="20" id="csvdata"></textarea>

					<input type="submit" name="Submit" value="送出" />
				</div>
			</form>
			<p>&nbsp;</p>
			<p>&nbsp;</p>
		</div>
		<jsp:include page="includes/Footer.jsp" />
	</div>
</body>
</html>
