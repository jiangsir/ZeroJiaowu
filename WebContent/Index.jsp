<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="includes/head.jsp" />

</head>
<body>
	<jsp:include page="includes/Header.jsp" />
	<div class="container">
		<c:forEach var="runningjob" items="${runningjobs}">
			<div>
				<h1>
					進入<a href="./Elective?jobid=${runningjob.id}">${runningjob.title}</a>
				</h1>
				<div>
					<pre>${runningjob.content}</pre>
				</div>
				<h4>
					本選課作業將於
					<fmt:formatDate value="${runningjob.finishtime}"
						pattern="yyyy-MM-dd HH:mm:ss" />
					結束，請務必注意時間。
				</h4>
				<div>共 ${fn:length(runningjob.finishElectives)} 人已經完成選填。</div>
				<%-- 					所有符合選填資格者共
					${fn:length(runningjob.allowedUsers)} 位。完成率
					<fmt:formatNumber pattern="##0.#"
						value="${fn:length(runningjob.finishElectives)*100/fn:length(runningjob.allowedUsers)}" />
					%<br />
 --%>
				<hr />
			</div>
		</c:forEach>
		<c:if test="${fn:length(runningjobs)==0}">
			<h1>目前沒有任何進行中的選課作業。</h1>
			<br />
			<br />
			<hr />
		</c:if>

		<c:forEach var="suspendingjob" items="${suspendingjobs}">
			<div>
				<h1>${suspendingjob.title}</h1>
				<div>
					<pre>${suspendingjob.content}</pre>
				</div>
				<h4>
					將於
					<fmt:formatDate value="${suspendingjob.starttime}"
						pattern="yyyy-MM-dd HH:mm:ss" />
					開始進行，請稍候再來。
				</h4>

				<br />
				<hr />
			</div>
		</c:forEach>
	</div>
	<jsp:include page="includes/Footer.jsp" />

</body>
</html>
