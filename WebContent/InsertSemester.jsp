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
		jQuery("#DoFenfa").click(function() {
			jQuery("#waiting").show();
			//alert("after doFenfa()");
			doFenfa();
		});
		jQuery("#ReFenfa").click(function() {
			jQuery("#waiting").show();
			//alert("after doFenfa()");
			reFenfa();
		});
	});

	function reFenfa() {
		jQuery("#waiting").show();
		jQuery.ajax({
			type : "POST",
			url : "./ReFenfa.do",
			//data: "semester="+${param.semester},
			data : "semester=" + jQuery.urlParam('semester'),
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
			//data: "semester="+${param.semester},
			data : "semester=" + jQuery.urlParam('semester'),

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
<body>
	<div id="main">
		<jsp:include page="includes/Header.jsp" />
		<div id="content">
			<div class="article">
				<h3>
					選填結果:<span id="ReFenfa" style="text-decoration: underline">清除所有分發結果</span>
					<c:if test="${fn:length(nonselectedelectives)>0}">(尚未完成分發工作!!(${fn:length(nonselectedelectives)}) -- <span
							id="DoFenfa" style="text-decoration: underline">進行分發</span>
						<img src="./images/waiting.gif" id="waiting" style="display: none"></img>
						<span id="status"></span>)
		<br />
					</c:if>
				</h3>
				未被分發的學生名單： <br />
				<c:forEach var="nonselectedelective" items="${nonselectedelectives}">
		${nonselectedelective.useraccount}(${nonselectedelective.user.username}): 
		</c:forEach>
				<ul>
					<c:forEach var="course" items="${courses}">
						<li><h3>${course.type}-${course.name},共
								${fn:length(course.electives)}人</h3></li>
						<c:forEach var="elective" items="${course.electives}">
							<%-- 							<jsp:useBean id="userBean" class="tw.zerojudge.Beans.UserBean" />
							<jsp:setProperty name="userBean" property="useraccount"
								value="${elective.useraccount}" />
 --%>		   ${elective.nth}th: ${elective.useraccount} (${elective.user.username})
		  </c:forEach>
					</c:forEach>
				</ul>
			</div>
		</div>
		<jsp:include page="includes/Footer.jsp" />
	</div>
</body>
</html>
