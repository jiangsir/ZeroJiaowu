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

<style type="text/css">
.namebox {
	width: 80%;
	height: 250px;
	overflow: auto;
	border: 1px;
	border-style: solid;
}
</style>
<script language="javascript">
	jQuery(document).ready(
			function() {
				jQuery("#reservedselections").hide();
				jQuery("#nonfenfaelectives").hide();
				jQuery("#submittedelectives").hide();
				jQuery("tr").hover(function() {
					$(this).addClass("tr_background");
				}, function() {
					$(this).removeClass("tr_background");
				});

				jQuery("#DoFenfa").click(function() {
					jQuery("#waiting").show();
					doFenfa();
					location.reload();
				});
				jQuery("#ReFenfa").click(function() {
					jQuery("#waiting").show();
					//alert("after doFenfa()");
					reFenfa();
					location.reload();
				});
				jQuery("#open_reserved").click(function() {
					jQuery("#reservedselections").show();
				});
				jQuery("#hide_reserved").click(function() {
					jQuery("#reservedselections").hide();
				});
				jQuery("#open_nonfenfa").click(function() {
					jQuery("#nonfenfaelectives").show();
				});
				jQuery("#hide_nonfenfa").click(function() {
					jQuery("#nonfenfaelectives").hide();
				});
				jQuery("#open_submitted").click(function() {
					jQuery("#submittedelectives").show();
				});
				jQuery("#hide_submitted").click(function() {
					jQuery("#submittedelectives").hide();
				});

				jQuery("a#deleteElective").click(
						function() {
							//event.preventDefault(); // 讓預設的動作失效！
							jQuery.ajax({
								type : "POST",
								url : "./Elective.do",
								//data: "jobid="+${param.jobid},
								data : "action=deleteElective&id="
										+ $(this).attr('electiveid'),
								// async: false, // 一般來說不該使用，因為用了會 waiting 會在 ajax 執行完後才會出來。
								timeout : 5000,
								beforeSend : function() {
								},
								success : function(result) {
								}, // success       
							});
						});
				jQuery("a#doUnlock").click(
						function() {
							//event.preventDefault(); // 讓預設的動作失效！
							jQuery.ajax({
								type : "POST",
								url : "./Elective.do",
								//data: "jobid="+${param.jobid},
								data : "action=changeLock&id="
										+ $(this).attr('electiveid'),
								// async: false, // 一般來說不該使用，因為用了會 waiting 會在 ajax 執行完後才會出來。
								timeout : 5000,
								beforeSend : function() {
								},
								success : function(result) {
								}, // success       
							});
						});
				jQuery("a#doLock").click(
						function() {
							//event.preventDefault(); // 讓預設的動作失效！
							//alert($(this).attr('electiveid'));
							jQuery.ajax({
								type : "POST",
								url : "./Elective.do",
								//data: "jobid="+${param.jobid},
								data : "action=changeLock&id="
										+ $(this).attr('electiveid'),
								// async: false, // 一般來說不該使用，因為用了會 waiting 會在 ajax 執行完後才會出來。
								timeout : 5000,
								beforeSend : function() {
								},
								success : function(result) {
								}, // success       
							});
						});
			});

	function reFenfa() {
		jQuery("#waiting").show();
		jQuery.ajax({
			type : "POST",
			url : "./ReFenfa.do",
			//data: "jobid="+${param.jobid},
			data : "jobid=" + jQuery.urlParam('jobid'),
			// async: false, // 一般來說不該使用，因為用了會 waiting 會在 ajax 執行完後才會出來。
			timeout : 5000,
			beforeSend : function() {
				jQuery("#waiting").show();
			},
			success : function(result) {
				//alert("success, result="+result);
				if (result == "Done") {
					jQuery("#ReFenfa").html("分發完成！！");
					jQuery("#waiting").hide();
					return;
				}
			}, // success		
		});
	}

	function doFenfa() {
		jQuery("#waiting").show();
		jQuery.ajax({
			type : "POST",
			url : "./DoFenfa.do",
			//data: "jobid="+${param.jobid},
			data : "jobid=" + jQuery.urlParam('jobid'),

			// async: false, // 一般來說不該使用，因為用了會 waiting 會在 ajax 執行完後才會出來。
			timeout : 5000,
			beforeSend : function() {
				jQuery("#waiting").show();
			},
			success : function(result) {
				//alert("success, result="+result);
				if (result == "Done") {
					jQuery("#DoFenfa").html("分發完成！！");
					jQuery("#waiting").hide();
					return;
				}
			}, // success		
		});
	}
</script>
</head>
<%-- <jsp:useBean id="jobBean" class="tw.zerojudge.Beans.JobBean"/>
<jsp:setProperty name="jobBean" property="jobid" value="${param.jobid}" />
 --%>
<body>
	<jsp:include page="includes/Header.jsp" />
	<div class="container">

		<!-- 				<h2>
					<span id="ReFenfa"
						style="text-decoration: underline; cursor: pointer;">[清除所有分發結果]</span>
					| <span id="DoFenfa"
						style="text-decoration: underline; cursor: pointer;"
						title="進行分發，並且會清除以前分發的結果。保障名額不受影響。">[進行分發]</span><img
						src="./images/waiting.gif" id="waiting" style="display: none"></img><span
						id="status"></span>
				</h2>
 -->

		<div class="btn-group" role="group" aria-label="...">
			<div class="btn btn-default" id="ReFenfa">清除所有分發結果</div>
			<div class="btn btn-default" id="DoFenfa"
				title="進行分發，並且會清除以前分發的結果。保障名額不受影響。">
				進行分發 <img src="./images/waiting.gif" id="waiting"
					style="display: none"></img> <span id="status"></span>
			</div>
			<div class="btn btn-default">
				<a href="./Export.api?target=getResults_XLS&jobid=${job.id }"
					type="button">匯出分發結果 xls</a>
			</div>
		</div>
		<%-- 		<div>
			<span id="ReFenfa" type="button">[清除所有分發結果]</span> | <span
				id="DoFenfa" type="button" title="進行分發，並且會清除以前分發的結果。保障名額不受影響。">[進行分發]</span><img
				src="./images/waiting.gif" id="waiting" style="display: none"></img><span
				id="status"></span> | <a
				href="./Export.api?target=getResults_XLS&jobid=${job.id }"
				type="button">匯出分發結果 xls</a>
		</div>
 --%>
		<h3>基本資訊：</h3>
		<div>
			符合選填資格人數(規則：${job.allowedusers})：人共
			${fn:length(jobBean.allowedUsers)} 位<br /> 已完成選填者： 共
			${fn:length(jobBean.finishElectives)} 位。 <br /> <br />
		</div>
		<h3>選填狀況:</h3>
		<div class="panel-group" id="accordion" role="tablist"
			aria-multiselectable="true">
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="headingOne">
					<h4 class="panel-title">
						<a role="button" data-toggle="collapse" data-parent="#accordion"
							href="#collapseOne" aria-expanded="true"
							aria-controls="collapseOne"> 舊生保留名額(共
							${fn:length(reservedselections)} 人) </a>
					</h4>
				</div>
				<div id="collapseOne" class="panel-collapse collapse in"
					role="tabpanel" aria-labelledby="headingOne">
					<div class="panel-body">
						<div id="reservedselections" class="namebox">
							<c:forEach var="reservedselection" items="${reservedselections}">
          ${reservedselection.account}(${reservedselection.user.username},${reservedselection.user.comment}${reservedselection.user.number}): ${reservedselection.selected}<br />
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="headingTwo">
					<h4 class="panel-title">
						<a class="collapsed" role="button" data-toggle="collapse"
							data-parent="#accordion" href="#collapseTwo"
							aria-expanded="false" aria-controls=" collapseTwo">
							已經完成選填，但尚未分發完成者(共 ${fn:length(submittedelectives)} 人)： </a>
					</h4>
				</div>
				<div id="collapseTwo" class="panel-collapse collapse"
					role="tabpanel" aria-labelledby="headingTwo">
					<div class="panel-body">

						<div class="namebox">
							<c:forEach var="submittedelective" items="${submittedelectives}">
          ${submittedelective.account}(${submittedelective.user.username},${submittedelective.user.comment}${submittedelective.user.number}): 1.${submittedelective.course1.name}, 2.${submittedelective.course2.name}, 3.${submittedelective.course3.name}, 4.${submittedelective.course4.name}, ${submittedelective.submittime}<br />
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="headingThree">
					<h4 class="panel-title">
						<a class="collapsed" role="button" data-toggle="collapse"
							data-parent="#accordion" href="#collapseThree"
							aria-expanded="false" aria-controls="collapseThree">
							尚未分發的學生名單（若已進行分發，仍在這裡，代表無法分發。）(共
							${fn:length(nonfenfaedelectives)} 人)：</a>
					</h4>
				</div>
				<div id="collapseThree" class="panel-collapse collapse"
					role="tabpanel" aria-labelledby="headingThree">
					<div class="panel-body">
						<table class="table table-hover">
							<tr>
								<td>序號</td>
								<td style="width: 12%">學生</td>
								<td>第一志願</td>
								<td>第二志願</td>
								<td>第三志願</td>
								<td>第四志願</td>
								<td>submittime</td>
								<td style="width: 10%">option</td>
							</tr>
							<c:forEach var="nonfenfaedelective"
								items="${nonfenfaedelectives}" varStatus="varstatus">
								<tr>
									<td>${varstatus.count }</td>
									<td>${nonfenfaedelective.account}:${nonfenfaedelective.user.username}<br />(${nonfenfaedelective.user.comment}
										${nonfenfaedelective.user.number})
									</td>
									<td>${nonfenfaedelective.course1.name}</td>
									<td>${nonfenfaedelective.course2.name}</td>
									<td>${nonfenfaedelective.course3.name}</td>
									<td>${nonfenfaedelective.course4.name}</td>
									<td><c:if
											test="${nonfenfaedelective.course1.name!='' || nonfenfaedelective.course2.name!='' || nonfenfaedelective.course3.name!='' || nonfenfaedelective.course4.name!='' }">${nonfenfaedelective.submittime}</c:if>
									</td>
									<td><c:if test="${nonfenfaedelective.lock==1}">
											<a href="" electiveid="${nonfenfaedelective.id }"
												id="doUnlock" title="解鎖"><img
												src="images/lock_${nonfenfaedelective.lock}.jpg"
												height="18px"></img></a>
										</c:if> <c:if test="${nonfenfaedelective.lock==0}">
											<a href="" electiveid="${nonfenfaedelective.id }" id="doLock">鎖定</a>
										</c:if> ｜ <a href="" id="deleteElective"
										electiveid="${nonfenfaedelective.id}"
										title="刪除：填錯的、不該填的、另有安排者。">刪除</a></td>
								</tr>
							</c:forEach>
						</table>

					</div>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="heading4">
					<h4 class="panel-title">
						<a class="collapsed" role="button" data-toggle="collapse"
							data-parent="#accordion" href="#collapse4" aria-expanded="false"
							aria-controls=" collapse4">未上網選課共
							${fn:length(job.nonSubmitUsers)}人，名單</a>
					</h4>
				</div>
				<div id="collapse4" class="panel-collapse collapse" role="tabpanel"
					aria-labelledby="heading4">
					<div class="panel-body">
						<div class="namebox">
							<c:forEach var="nonSubmitUser" items="${job.nonSubmitUsers}">
                        ${nonSubmitUser.value.account},${nonSubmitUser.value.username},${nonSubmitUser.value.comment},${nonSubmitUser.value.number}<br />
							</c:forEach>
						</div>
					</div>
				</div>
			</div>

		</div>

		<%-- 		<div>
			<br /> 舊生保留名額(共 ${fn:length(reservedselections)} 人) -- <span
				id="open_reserved">[展開]</span><span id="hide_reserved">[隱藏]</span>
			<div id="reservedselections" class="namebox">
				<c:forEach var="reservedselection" items="${reservedselections}">
          ${reservedselection.account}(${reservedselection.user.username},${reservedselection.user.comment}${reservedselection.user.number}): ${reservedselection.selected}<br />
				</c:forEach>
			</div>
			<br /> 已經完成選填者(共 ${fn:length(submittedelectives)} 人)：<span
				id="open_submitted">[展開]</span><span id="hide_submitted">[隱藏]</span><br />
			<div id="submittedelectives" class="namebox">
				<c:forEach var="submittedelective" items="${submittedelectives}">
          ${submittedelective.account}(${submittedelective.user.username},${submittedelective.user.comment}${submittedelective.user.number}): 1.${submittedelective.course1}, 2.${submittedelective.course2}, 3.${submittedelective.course3}, 4.${submittedelective.course4}, ${submittedelective.submittime}<br />
				</c:forEach>
			</div>
			<table>
				<tr>
					<td>序號</td>
					<td style="width: 12%">學生</td>
					<td>第一志願</td>
					<td>第二志願</td>
					<td>第三志願</td>
					<td>第四志願</td>
					<td>submittime</td>
					<td style="width: 10%">option</td>
				</tr>
				<c:forEach var="nonfenfaedelective" items="${nonfenfaedelectives}"
					varStatus="varstatus">
					<tr>
						<td>${varstatus.count }</td>
						<td>${nonfenfaedelective.account}:${nonfenfaedelective.user.username}<br />(${nonfenfaedelective.user.comment}
							${nonfenfaedelective.user.number})
						</td>
						<td>${nonfenfaedelective.course1}</td>
						<td>${nonfenfaedelective.course2}</td>
						<td>${nonfenfaedelective.course3}</td>
						<td>${nonfenfaedelective.course4}</td>
						<td><c:if
								test="${nonfenfaedelective.course1!='' || nonfenfaedelective.course2!='' || nonfenfaedelective.course3!='' || nonfenfaedelective.course4!='' }">${nonfenfaedelective.submittime}</c:if>
						</td>
						<td><c:if test="${nonfenfaedelective.lock==1}">
								<a href="" electiveid="${nonfenfaedelective.id }" id="doUnlock"
									title="解鎖"><img
									src="images/lock_${nonfenfaedelective.lock}.jpg" height="18px"></img></a>
							</c:if> <c:if test="${nonfenfaedelective.lock==0}">
								<a href="" electiveid="${nonfenfaedelective.id }" id="doLock">鎖定</a>
							</c:if> ｜ <a href="" id="deleteElective"
							electiveid="${nonfenfaedelective.id}" title="刪除：填錯的、不該填的、另有安排者。">刪除</a></td>
					</tr>
				</c:forEach>
			</table>


			<br /> 無法分發的學生名單(共 ${fn:length(nonfenfaedelectives)} 人)： <span
				id="open_nonfenfa">[展開]</span><span id="hide_nonfenfa">[隱藏]</span><br />
								<div id="nonfenfaelectives" class="namebox">
						<c:forEach var="nonfenfaedelective" items="${nonfenfaedelectives}">
		${nonfenfaedelective.account}(${nonfenfaedelective.user.username}-${nonfenfaedelective.user.comment} ${nonfenfaedelective.user.number}): 1th.${nonfenfaedelective.course1}, 2th.${nonfenfaedelective.course2}, 3th.${nonfenfaedelective.course3}, 4th.${nonfenfaedelective.course4}
		<c:if
								test="${nonfenfaedelective.course1!='' || nonfenfaedelective.course2!='' || nonfenfaedelective.course3!='' || nonfenfaedelective.course4!='' }">, ${nonfenfaedelective.submittime}</c:if>
							<br />
						</c:forEach>
					</div>

		</div>
 --%>
		<h2>分發結果：</h2>
		<%-- 					<jsp:useBean id="userBean" class="tw.zerojudge.Beans.UserBean" />
 --%>

		<c:forEach var="course" items="${courses}">
			<%-- 			<div style="margin-top: 2em;">
				<h3 style="display: inline;">${course.type}-${course.name},共
					${fn:length(course.electives)}人 | ${course.capacity}人 <a
						href="./Export.api?target=getStudents_CSV&courseid=${course.id}"
						type="button">下載CSV </a>
				</h3>
			</div> --%>
			<div class="panel panel-info" id="coursebox">
				<div class="panel-heading">
					<h3 class="panel-title">${course.type}-${course.name},共
						${fn:length(course.electives)}人 | ${course.capacity}人 <a
							href="./Export.api?target=getStudents_CSV&courseid=${course.id}"
							type="button">下載CSV </a>
					</h3>
				</div>
				<div class="panel-body">
					<table class="table table-hover">
						<tr>
							<td>index</td>
							<td>nth</td>
							<td>user</td>
							<td style="width: 16%">1th</td>
							<td>2th</td>
							<td>3th</td>
							<td>4th</td>
							<td>LOCK</td>
							<td style="width: 5%">opt</td>
						</tr>
						<c:forEach var="elective" items="${course.electives}"
							varStatus="varstatus">
							<tr>
								<td>${varstatus.count}</td>
								<td>${elective.nth}th</td>
								<td>${elective.account}<br />${elective.user.username}</td>
								<td>${elective.course1}</td>
								<td>${elective.course2}</td>
								<td>${elective.course3}</td>
								<td>${elective.course4}</td>
								<td><c:if test="${elective.lock==1}">

										<a href="" electiveid="${elective.id }" id="doUnlock"
											title="解鎖"><img src="images/lock_${elective.lock}.jpg"
											height="18px"></img></a>
									</c:if> <c:if test="${elective.lock==0}">
										<a href="" electiveid="${elective.id }" id="doLock">鎖定</a>
									</c:if></td>
								<td><a href="" id="deleteElective"
									electiveid="${nonfenfaedelective.id}"
									title="刪除：填錯的、不該填的、另有安排者。">刪除</a></td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>

			<hr></hr>
		</c:forEach>


	</div>
	<jsp:include page="includes/Footer.jsp" />
</body>
</html>
