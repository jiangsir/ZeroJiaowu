<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<title>高師大附中 線上選課系統</title>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">

<!-- 選擇性佈景主題 -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
<script
	src=https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js></script>
<!-- 最新編譯和最佳化的 JavaScript -->
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>

<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/css/bootstrap-dialog.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/js/bootstrap-dialog.min.js"></script>

<link href="styles/jquery-ui-timepicker-addon.css" rel="stylesheet" />
<script src="jscripts/jquery-ui-timepicker-addon.js"></script>

<script type="text/javascript" src="jscripts/js_date.js"></script>
<script type="text/javascript"
	src="jscripts/jquery.timeout.interval.idle.js"></script>

<script type="text/javascript">
	jQuery(document)
			.ready(
					function() {
						$("input:first").focus();
						$(
								"button, input[type=submit], [type='button'], [type='confirm']")
								.button();
						/* 				$("button").button().click(function(event) {
						 event.preventDefault(); // 讓預設的動作失效！
						 });
						 */
						$.urlParam = function(name) {
							var results = new RegExp('[\?&]' + name
									+ '=([^&#]*)').exec(window.location.href);
							return results[1] || 0;
						}
					});
</script>
