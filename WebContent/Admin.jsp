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
		<h3>管理介面:</h3>

		<div class="btn-group" role="group" aria-label="...">
			<div class="btn btn-default">
				<a href="./InsertJob" type="button">新增選修作業</a>
			</div>
			<div class="btn btn-default">
				<a href="Import" type="button">匯入 User</a>
			</div>

			<div class="btn-group" role="group">
				<form id="form1" method="get" action="./Result">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						請選擇。。。 <span class="caret"></span>
					</button>
					<%-- 						<select name="jobid" onchange="this.form.submit();">
							<option value="0" selected="selected">請選擇...</option>
							<c:forEach var="job" items="${jobs}">
								<option value="${job.id}">${job.title}</option>
							</c:forEach>
						</select>
 --%>
					<ul class="dropdown-menu">
						<c:forEach var="job" items="${jobs}">
							<li><a href="./Result?jobid=${job.id }">${job.title}</a></li>
						</c:forEach>
					</ul>
				</form>
			</div>
		</div>


		<!-- 			<button type="button" class="btn btn-default">Right</button>
 -->

		<hr>
		<%-- 		<ul>
			<li>查看選填結果：
				<form id="form1" method="get" action="./Result"
					style="margin: 0px; display: inline; text-align: center">
					<select name="jobid" onchange="this.form.submit();">
						<option value="0" selected="selected">請選擇...</option>
						<c:forEach var="job" items="${jobs}">
							<option value="${job.id}">${job.title}</option>
						</c:forEach>
					</select>
				</form>
			</li>
		</ul>
 --%>

		<h3>所有選填作業列表：${fn:length(jobs)}</h3>
		<c:forEach var="job" items="${jobs}">
			<div class="panel panel-info" id="jobbox">
				<div class="panel-heading">
					<h3 class="panel-title">
						『${job.title}』 <span style="font-size: 16px; color: #FF0000">
							-${job.status } </span>
					</h3>
				</div>
				<div class="panel-body">
					開始時間：${job.starttime}, 結束時間：${job.finishtime} <br /> 科目：<br />
					<c:forEach var="course" items="${job.courses}"> ${course.name} <br />
					</c:forEach>
				</div>
				<div class="panel-footer">
					共 ${fn:length(job.finishElectives)} 人已經完成選填。所有可以選填的人共
					${fn:length(job.allowedUsers)} 位。<br /> <span
						style="text-align: right;"><a
						href="./UpdateJob?jobid=${job.id}">編輯</a> | <a
						href="./Result?jobid=${job.id}">進行分發作業</a> | 移除</span>
				</div>
			</div>
			<%-- 				<div class="jobbox">
					<h3>
						『${job.title}』 <span style="font-size: 16px; color: #FF0000">
							-${job.status } </span>
					</h3>
					開始時間：${job.starttime}, 結束時間：${job.finishtime} <br /> 科目：<br />
					<c:forEach var="course" items="${job.courses}"> ${course.name} <br />
					</c:forEach>
					<br /> <br /> 共 ${fn:length(job.finishElectives)}
					人已經完成選填。所有可以選填的人共 ${fn:length(job.allowedUsers)} 位。<br /> <span
						style="text-align: right;"><a
						href="./UpdateJob?jobid=${job.id}">編輯</a> | <a
						href="./Result?jobid=${job.id}">進行分發作業</a> | 移除</span>
				</div>
				<br />
 --%>
		</c:forEach>
		<!-- 				<li>匯出...
					<form id="form1" method="get" action="./Export"
						style="margin: 0px; display: inline; text-align: center">
						<div style="margin: 0px; display: inline; text-align: center">
							<select name="select" onchange="this.form.submit();">
								<option value="0" selected="selected">請選擇...</option>

							</select>
						</div>
					</form>
				</li>
 -->
	</div>

	<jsp:include page="includes/Footer.jsp" />
</body>
</html>
