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

<script language="javascript">
	jQuery(document).ready(function() {
		$("div[name=course]:first").show();

		$("#course1").change(function() { //事件發生
			jQuery('option:selected', this).each(function() { //印出選到多個值
				jQuery("div[name=course]").hide('slow');
				jQuery("div[id=" + this.value + "]").show('slow');
			});
		});
		$("#course2").change(function() { //事件發生
			jQuery('option:selected', this).each(function() { //印出選到多個值
				jQuery("div[name=course]").hide('slow');
				jQuery("div[id=" + this.value + "]").show('slow');
			});
		});
		$("#course3").change(function() { //事件發生
			jQuery('option:selected', this).each(function() { //印出選到多個值
				jQuery("div[name=course]").hide('slow');
				jQuery("div[id=" + this.value + "]").show('slow');
			});
		});
		$("#course4").change(function() { //事件發生
			jQuery('option:selected', this).each(function() { //印出選到多個值
				jQuery("div[name=course]").hide('slow');
				jQuery("div[id=" + this.value + "]").show('slow');
			});
		});

	});
</script>
</head>
<jsp:useBean id="date" class="java.util.Date" /><body>
	<div id="main">
		<jsp:include page="includes/Header.jsp" />
		<div id="content"></div>
		<c:forEach var="job" items="${jobs}">
			<div id="panel">
				<div class="h1">
					<h1>『${job.title}』</h1>
				</div>
				<br />
				<pre>${job.content}</pre>
				<hr />
				<br />
				<c:choose>
					<c:when test="${date.time < job.starttime.time}"> 尚未開始，請稍候！ </c:when>
					<c:when test="${date.time > job.finishtime.time}"> 本選填作業已經結束！ </c:when>
					<c:when
						test="${date.time > job.starttime.time && date.time < job.finishtime.time}">
						<div class="left">
							<c:choose>
								<c:when test="${elective.isReserved==true}"> 您是舊生保障名額，因此不需再選填一次。<br />
								</c:when>
								<c:when test="${elective.isFinish==true}"> ${elective.result} <br />
								</c:when>
								<c:otherwise>
									<%--             <jsp:useBean id="courseBean" class="tw.zerojudge.Beans.CourseBean"/>
 --%>
									<%-- 									<jsp:setProperty name="courseBean" property="jobid"
										value="${job.id}" />
 --%>            ${sessionScope.onlineUser.username} 您好！ <br />
									<br />
									<br />
									<p>
										請選擇您想選修的課程 <br /> ※ 注意： 不同的志願序請勿選取相同課程。這樣並不會增加中簽的機率。
										請依照順序選擇，請小心選擇確認之後就無法更改囉！<br />
									</p>
									<form action="" method="post">
										<c:forEach var="i" begin="1" end="${job.max_choose}" step="1">
											<h4>
												<label for="item_a">第 ${i} 志願：</label>
											</h4>
											<select id="course${i}" name="course${i}"
												style="width: 100%; font-size: 1.5em;">
												<c:forEach var="course" items="${job.courses}">
													<option value="${course.name}">${course.name}(上限
														${course.capacity}人)</option>
												</c:forEach>
											</select>
										</c:forEach>
										<input name="jobid" type="hidden" value="${job.id}" />
										<p class="button">
											<button name="submit" type="submit">確認！</button>
										</p>
									</form>
									<br />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="right">
							<c:forEach var="course" items="${job.courses}">
								<div name="course" id="${course.name}"
									style="display: none; float: left;">
									<h3>課程名稱：${course.name}</h3>
									<br />
									<h3>課程內容：</h3>
									<pre style="">${course.content}</pre>
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</div>
		</c:forEach>
		<jsp:include page="includes/Footer.jsp" />
	</div>
</body>
</html>
