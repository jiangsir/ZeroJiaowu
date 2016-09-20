<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<title>高師大附中 線上選課系統</title>
<meta charset="UTF-8">
<!-- <link rel="stylesheet" href="css/bootstrap.min.css" />
<script src="jscripts/bootstrap.min.js"></script>
 -->
<link rel="stylesheet" title="Normal" type="text/css" media="screen"
	href="./screen.css" />

<script src="jscripts/jquery-ui-1.10.4.custom/js/jquery-1.10.2.js"></script>
<script
	src="jscripts/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.min.js"></script>

<link rel="stylesheet" type="text/css" media="screen"
	href="jscripts/jquery-ui-1.10.4.custom/css/smoothness/jquery-ui-1.10.4.custom.min.css" />
	
<link href="styles/jquery-ui-timepicker-addon.css" rel="stylesheet" />
<script src="jscripts/jquery-ui-timepicker-addon.js"></script>
	
	
<script type="text/javascript" src="jscripts/js_date.js"></script>
<script type="text/javascript"
	src="jscripts/jquery.timeout.interval.idle.js"></script>

<script type="text/javascript">
	jQuery(document).ready(
			function() {
				$("input:first").focus();
				$("button, input[type=submit], [type='button'], [type='confirm']").button();
/* 				$("button").button().click(function(event) {
					event.preventDefault(); // 讓預設的動作失效！
				});
 */
				$.urlParam = function(name) {
					var results = new RegExp('[\?&]' + name + '=([^&#]*)')
							.exec(window.location.href);
					return results[1] || 0;
				}
			});
</script>
