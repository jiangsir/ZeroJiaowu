<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.session_locale}" />
<fmt:setBundle basename="resource" />
<jsp:useBean id="now" class="java.util.Date" />
<script language="javascript">
	jQuery(document).ready(function() {
		mytime(parseInt(jQuery("#now").text()));
	});

	function mytime(nowtime) {
		var nowdate = new Date();
		nowdate.setTime(nowtime);
		jQuery("#now").text(formatDate(nowdate, "y-MM-dd HH:mm:ss"));
		jQuery.interval(function() {
			var nowdate = new Date();
			nowtime = nowtime + 1000;
			//  alert("nowtime="+nowtime);
			nowdate.setTime(nowtime);
			jQuery("#now").text(formatDate(nowdate, "y-MM-dd HH:mm:ss"));
		}, 1000);
	}
</script>


<div style="color: #FF0000; text-align: center; font-size: 1.5em; padding: 0.3em;">
	目前系統時間：<span id="now">${now.time}</span>
</div>
