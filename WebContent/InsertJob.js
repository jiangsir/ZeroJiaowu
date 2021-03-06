jQuery(document).ready(
		function() {

			$("input[name='starttime']").datetimepicker({
				dateFormat : 'yy-mm-dd',
				timeFormat : 'HH:mm:ss'
			});
			$("input[name='finishtime']").datetimepicker({
				dateFormat : 'yy-mm-dd',
				timeFormat : 'HH:mm:ss'
			});

			jQuery("div[id=addcourse]").click(
					function() {
						if (jQuery("div[id=coursebox]").size()
								+ jQuery("div[id=newcoursebox]").size() < 10) {
							jQuery("div[id=coursebox]:last").clone(true)
									.insertAfter("div[id=coursebox]:last");
							// jQuery("div[id=coursebox]:last").attr("id",
							// "newcoursebox");
							jQuery("div[id=coursebox]:last input").each(
									function() {
										$(this).val("");
									});
							jQuery("div[id=coursebox]:last textarea").each(
									function() {
										$(this).val("");
									});
						}
					});

			jQuery("span[id='deleteCourse']").click(
					function() {
						var courseid = $(this).attr('courseid');
						// alert(courseid);
						var coursebox = $(this).closest("div[id='coursebox']");
						// alert(coursebox);
						jQuery.ajax({
							type : "POST",
							url : "./Course.do",
							data : "action=" + "deleteCourse&" + "courseid="
									+ courseid,
							async : false, // 一般來說不該使用，因為用了會 waiting 會在
							// ajax
							// 執行完後才會出來。
							timeout : 5000,
							beforeSend : function() {
							},
							success : function(result) {
								// alert(result);
								// jQuery("div[name=course_" + courseid
								// + "]")
								// .remove();
								// alert(coursebox.html());
								coursebox.remove();
								// $(this).closest("div[id='coursebox']")
								// .remove();
							} // success
						});
					});
		});
