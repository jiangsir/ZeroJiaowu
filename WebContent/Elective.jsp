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

<script type="text/javascript">
	jQuery(document).ready(function() {
		$("div[name=course]:first").show();

		$("#courseid1").change(function() { //事件發生
			jQuery('option:selected', this).each(function() { //印出選到多個值
				jQuery("div[name=course]").hide('slow');
				jQuery("div[id=" + this.value + "]").show('slow');
			});
		});
		$("#courseid2").change(function() { //事件發生
			jQuery('option:selected', this).each(function() { //印出選到多個值
				jQuery("div[name=course]").hide('slow');
				jQuery("div[id=" + this.value + "]").show('slow');
			});
		});
		$("#courseid3").change(function() { //事件發生
			jQuery('option:selected', this).each(function() { //印出選到多個值
				jQuery("div[name=course]").hide('slow');
				jQuery("div[id=" + this.value + "]").show('slow');
			});
		});
		$("#courseid4").change(function() { //事件發生
			jQuery('option:selected', this).each(function() { //印出選到多個值
				jQuery("div[name=course]").hide('slow');
				jQuery("div[id=" + this.value + "]").show('slow');
			});
		});

	});
</script>
</head>
<jsp:useBean id="date" class="java.util.Date" /><body>
	<jsp:include page="includes/Header.jsp" />
	<div class="container">
		<c:forEach var="job" items="${jobs}">
			<div id="row">
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
						<div class="col-md-5">
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
 --%>
									<h3>${sessionScope.currentUser.username}您好！</h3>
									<p>
										請選擇您想選修的課程 <br /> ※ 注意： 不同的志願序請勿選取相同課程。這樣並不會增加中簽的機率。
										請依照順序選擇，請小心選擇確認之後就無法更改囉！<br />
									</p>
									<form action="" method="post">
										<c:forEach var="i" begin="1" end="${job.max_choose}" step="1">
											<h4>
												<label for="item_a">第 ${i} 志願：</label>
											</h4>
											<select id="courseid${i}" name="courseid${i}"
												style="width: 100%; font-size: 1.5em;">
												<c:forEach var="course" items="${job.courses}">
													<option value="${course.id}">${course.name}(上限
														${course.capacity}人)</option>
												</c:forEach>
											</select>
										</c:forEach>
										<!-- 										<p class="button">
											<button name="submit" type="submit">確認！</button>
										</p>
 -->
										<div>
											<br /> <br /> <input name="jobid" type="hidden"
												value="${job.id}" />
											<button type="submit" class="btn btn-success">確認</button>
										</div>
									</form>
									<br />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="col-md-7">
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
	</div>
	<jsp:include page="includes/Footer.jsp" />
</body>
</html>
