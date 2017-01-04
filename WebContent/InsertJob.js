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
			var select = $("select[name='max_choose']");
			console.log("select.data('max_choose')="
					+ select.data('max_choose'));
			$(
					"select[name='max_choose'] option[value="
							+ select.data('max_choose') + "]").prop('selected',
					true);
			// $('.id_100 option[value=val2]').prop('selected', true);

			jQuery("button[id=addcourse]").click(
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
						var count = 0;
						jQuery("span[id='course_index']").each(function() {
							$(this).html("#" + ++count);
						});
					});

			jQuery("button[id=duplicateCourse]").click(function() {
				var coursebox = $(this).closest("div[id=coursebox]");
				// alert(coursebox);
				var clone = coursebox.clone(true);
				clone.find("input[name=courseid]").val("");
				clone.insertAfter(coursebox);

				var count = 0;
				jQuery("span[id='course_index']").each(function() {
					$(this).html("#" + ++count);
				});
			});

			jQuery("span[id='deleteCourse']").click(function() {
				var courseid = $(this).attr('courseid');
				// alert(courseid);
				var coursebox = $(this).closest("div[id='coursebox']");
				// alert(coursebox);
			});

			jQuery("button[id='removeCourse']").click(
					function() {
						var index = jQuery("button[id='removeCourse']").index(
								this);
						var size = jQuery("button[id='removeCourse']").size();
						if (size > 1) {
							var coursebox = $(this).closest(
									"div[id='coursebox']");
							coursebox.remove();
						}
						var count = 0;
						jQuery("span[id='course_index']").each(function() {
							$(this).html("#" + ++count);
						});
						jQuery.ajax({
							type : "POST",
							url : "./Course.do",
							data : "action=" + "deleteCourse&" + "courseid="
									+ $(this).data('courseid'),
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
