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

	<jsp:include page="includes/Header.jsp" />
	<div class="container">
		<h3>新增一個學期的選填作業</h3>
		<p>&nbsp;</p>
		<form id="form1" method="post" action="" role="form">
			<div class="form-group">
				<label for="title"> 主旨： </label>
				<p class="help-block">* 如：國中/高中選修課程選填作業, 國中高中社團選填作業</p>
				<input type="text" class="form-control" id="title" name="title"
					value="${job.title}" />
			</div>
			<div class="form-group">
				<label for="content"> 內容： </label>
				<p class="help-block"></p>
				<textarea class="form-control" id="content" name="content">${job.content}</textarea>
			</div>
			<div class="form-group">
				<label for="semester"> 學期代號： </label>
				<p class="help-block">* 請以數字代表學期，如 981 代表 98學年度第一學期</p>
				<input type="text" class="form-control" id="semester"
					name="semester" value="${job.semester}" />
			</div>
			<div class="form-group">
				<label for="allowedusers"> 有資格進入作業的學生清單： </label>
				<p class="help-block">
					使用正規表示式(開始選課之後就不應再更動，否則選填資料會被清除)<br /> 如：國中98[1-2][0-9]{4},
					高中81[0-2][0-9][0-9][0-9]
				</p>
				<textarea class="form-control" id="allowedusers" name="allowedusers">${job.allowedusers}</textarea>

			</div>
			<div class="form-group">
				<label for="ipset"> 本選課作業限定的範圍 </label>
				<p class="help-block">
					* 使用 CIDR 表示法。<br /> 如：全面開放為 [0.0.0.0/0], 限定 192.168.1.0~255
					可以進行選課則為: [192.268.1.0/24]
				</p>
				<input type="text" class="form-control" id="ipset" name="ipset"
					value="${job.ipset}" />
			</div>
			<div class="form-group">
				<label for="starttime"> 開始時間： </label> <input type="text"
					class="form-control" id="starttime" name="starttime"
					value="<fmt:formatDate value="${job.starttime}" pattern="yyyy-MM-dd HH:mm:ss"/>
          " />
			</div>
			<div class="form-group">
				<label for="finishtime">結束時間：</label> <input type="text"
					class="form-control" id="finishtime" name="finishtime"
					value="<fmt:formatDate value="${job.finishtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
          " />
			</div>
			<%-- 			<div class="form-group">
				<label for="coursecsv">快速設定課程項目</label>
				<p class="help-block">* 請依下列格式編寫 csv 內容，一筆資料一行，以 # 開頭的資料行將被忽略。</p>
				<textarea class="form-control" id="coursecsv" name="coursecsv"
					rows="8">#課程名稱,授課教師,人數上限,課程內容
${coursecsv}</textarea>

			</div>
 --%>
			<div class="container">
				<div class="row">
					<h3>課程項目：</h3>
					<div class="col-md-12">
						<c:forEach var="course" items="${courses}" varStatus="varstatus">
							<div id="coursebox">
								<input name="courseid" type="hidden" value="${course.id}" />
								<div class="row">
									<div class="col-md-5">
										<span id="course_index">#${varstatus.count }</span>
										<div class="form-group">
											<label for="coursename" class="col-sm-3 control-label">
												課程名稱： </label>
											<div class="col-sm-9">
												<input type="text" class="form-control" id="coursename"
													name="coursename" value="${course.name}" />
											</div>
										</div>
										<div class="form-group">
											<label for="teacher" class="col-sm-3 control-label">
												授課教師：</label>
											<div class="col-sm-9">
												<input type="text" class="form-control" id="teacher"
													name="teacher" value="${course.teacher}" />
											</div>
										</div>
										<div class="form-group">
											<label for="coursecapacity" class="col-sm-3 control-label">
												人數限制：</label>
											<div class="col-sm-9">
												<input type="text" class="form-control" id="coursecapacity"
													name="coursecapacity" value="${course.capacity}" />
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<label for="coursecontent">課程內容：</label>
										<textarea class="form-control" id="coursecontent"
											name="coursecontent" rows="3">${course.content}</textarea>
										<%-- 									<textarea name="coursecontent"
										style="width: 100%; height: 10em;" id="coursecontent">${course.content}</textarea>
 --%>

									</div>
									<div class="col-md-1">
										<button type="button" class="btn btn-default"
											id="removeCourse">
											<span class="glyphicon glyphicon-remove" title="移除本課程"></span>
										</button>

										<button type="button" class="btn btn-default"
											id="duplicateCourse">
											<span class="glyphicon glyphicon-plus" title="再製本課程"></span>
										</button>
									</div>

								</div>
								<hr />
							</div>
						</c:forEach>
					</div>
				</div>
			</div>


			<%-- 			<c:forEach var="course" items="${courses}">
				<div id="coursebox" name="course_${course.id}">
					<div class="contentbox">
						<div style="float: left; width: 30%">
							<input name="courseid" type="hidden" value="${course.id}" />
							課程名稱：<input name="coursename" type="text" id="coursename"
								value="${course.name}" style="height: 1.2em;" /> <br /> <br />
							授課教師： <input name="teacher" type="text" id="teacher"
								value="${course.teacher}" size="20" /> ，人數限制： <input
								name="coursecapacity" type="text" id="coursecapacity"
								value="${course.capacity}" size="5" maxlength="5" /> 人
						</div>
						<div style="float: left; width: 60%">
							課程內容： <br />
							<textarea name="coursecontent" style="width: 100%; height: 10em;"
								id="coursecontent">${course.content}</textarea>
						</div>
						<div style="clear: both"></div>
						<span class="link" id="deleteCourse" courseid="${course.id}">刪除本課程</span><br />
					</div>
				</div>
			</c:forEach>
 --%>
			<div>
				<button type="button" class="btn btn-default" id="addcourse">
					<span class="glyphicon glyphicon-plus"></span> 增加課程
				</button>
			</div>
			<div>

				<input name="jobid" type="hidden" value="${job.id}" /> <br /> <input
					type="submit" value="送出" class="btn btn-success" />
			</div>
		</form>
	</div>

	<jsp:include page="includes/Footer.jsp" />

</body>
</html>
