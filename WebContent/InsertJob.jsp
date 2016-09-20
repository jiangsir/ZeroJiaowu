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

<script type="text/javascript"
	src="./jscripts/jscalendar-1.0/calendar.js"></script>
<script type="text/javascript"
	src="./jscripts/jscalendar-1.0/calendar-big5-utf8.js"></script>
<script type="text/javascript"
	src="./jscripts/jscalendar-1.0/calendar-setup.js"></script>
<script type="text/javascript" src="InsertJob.js"></script>
</head>
<body>
	<div id="main">
		<jsp:include page="includes/Header.jsp" />
		<div id="content">
			<div class="article">
				<h3>新增一個學期的選填作業</h3>
				<p>&nbsp;</p>
				<form id="form1" method="post" action="">
					<div>
						<br /> 主旨： <input name="title" type="text" id="title"
							value="${job.title}" size="80" maxlength="80" /> <br /> *
						如：國中/高中選修課程選填作業, 國中高中社團選填作業
					</div>
					<div>
						內容：<br />
						<textarea name="content" cols="70" rows="5" ixd="content">${job.content}</textarea>
						<br /> 學期代號： <input name="semester" type="text" id="semester"
							value="${job.semester}" size="10" maxlength="10" /> <br /> *
						請以數字代表學期，如 981 代表 98學年度第一學期
					</div>
					<div>
						有資格進入作業的學生清單：使用正規表示式(開始選課之後就不應再更動，否則選填資料會被清除)<br />
						如：國中98[1-2][0-9]{4}, 高中81[0-2][0-9][0-9][0-9]
					</div>
					<div>
						<textarea name="allowedusers" cols="70" rows="1" id="allowedusers">${job.allowedusers}</textarea>
					</div>
					<div>
						本選課作業限定的範圍, 使用 CIDR 表示法。<br /> 如：全面開放為 [0.0.0.0/0], 限定
						192.168.1.0~255 可以進行選課則為: [192.268.1.0/24]
					</div>
					<div>
						<input name="ipset" type="text" value="${job.ipset}"
							style="width: 60%; height: 1.2em;" /> <br />
					</div>
					<div>
						開始時間： <input name="starttime" type="text" id="starttime"
							value="<fmt:formatDate value="${job.starttime}" pattern="yyyy-MM-dd HH:mm:ss"/>
          " />

					</div>
					<div>
						結束時間： <input name="finishtime" type="text" id="finishtime"
							value="<fmt:formatDate value="${job.finishtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
          " />

					</div>
					<h3>快速設定課程項目，請依下列格式編寫 csv 內容，一筆資料一行，以 # 開頭的資料行將被忽略。</h3>
					<div class="contentbox">
						<textarea name="coursecsv" style="width: 90%; height: 30em;">#課程名稱,授課教師,人數上限,課程內容
${coursecsv}</textarea>
					</div>
					<h3>課程項目：</h3>
					<c:forEach var="course" items="${courses}">
						<div id="coursebox" name="course_${course.id}">
							<div class="contentbox">
								<div style="float: left; width: 30%">
									<input name="courseid" type="hidden" value="${course.id}" />
									課程名稱：<input name="coursename" type="text" id="coursename"
										value="${course.name}" style="height: 1.2em;" />
									<br /> <br /> 授課教師： <input name="teacher" type="text"
										id="teacher" value="${course.teacher}" size="20" /> ，人數限制： <input
										name="coursecapacity" type="text" id="coursecapacity"
										value="${course.capacity}" size="5" maxlength="5" /> 人
								</div>
								<div style="float: left; width: 60%">
									課程內容： <br />
									<textarea name="coursecontent"
										style="width: 100%; height: 10em;" id="coursecontent">${course.content}</textarea>
								</div>
								<div style="clear: both"></div>
								<span class="link" id="deleteCourse" courseid="${course.id}">刪除本課程</span><br />
							</div>
						</div>
					</c:forEach>
					<hr />
					<div id="addcourse" class="link">增加課程</div>
					<p>
						<input name="jobid" type="hidden" value="${job.id}" /> <input
							type="submit" name="Submit" value="確定" />
					</p>
				</form>
			</div>
		</div>
		<jsp:include page="includes/Footer.jsp" />
	</div>
</body>
</html>
