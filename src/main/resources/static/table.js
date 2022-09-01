
	$(document).on("click", '.expandChildTable', function() {
		$(this).toggleClass('selected').closest('tr').next().toggle();
	});
	$(function() {
		$.ajax({
			url: server_url+":"+server_port+"/stops"
		}).then(function(data) {
			var table_rows = "";
			for (var i = 0; i < data.length; i++) {
				var line = data[i];
				table_rows = table_rows
					+ "<tr><td><span class=\"expandChildTable\"></span></td><td>"
					+ line.lineNumber
					+ "</td><td>"
					+ line.origin.stopPointaddress.StopPointName
					+ "</td><td>"
					+ line.destination.stopPointaddress.StopPointName
					+ "</td></tr><tr class=\"childTableRow\"><td colspan=\"4\"><div class=\"progress\">";
				var total_stops = line.stops.length;
				for (var j = 0; j < total_stops; j++) {
					var stop = line.stops[j];
					table_rows = table_rows
						+ "<div class=\"circle done\"><div class=\"label_box\"><span class=\"label\"></span><span class=\"title\">"
						+ stop.stopPointaddress.StopPointName
						+ "</span></div></div>";
					if (j < total_stops - 1) {
						table_rows = table_rows
							+ "<span class=\"bar done\"></span>";
					}
				}
				table_rows = table_rows + "</div></td></tr>";
			}
			$('.tabledata').append(table_rows);
		}).fail(function(request) {
				console.log(request);
				alert_info = "<div class=\"alert alert-danger\" role=\"alert\">"+request.status+"<br>"+request.responseText+"</div>"
				 $('p.alert-success').remove();
				 $('p').append(alert_info);
		});
	});

	