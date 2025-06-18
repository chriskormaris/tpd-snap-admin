document.addEventListener("DOMContentLoaded", () => {
	document.querySelector("#console-delete-btn").addEventListener("click", () => {
		if (confirm("Είστε σίγουροι ότι θέλετε να διαγράψετε αυτό το ερώτημα?")) {
			document.querySelector("#console-delete-form").submit();
		}
	});

	if (document.querySelector("nav select.page-size") != null) {
		document.querySelectorAll("nav select.page-size").forEach(e => {
			e.addEventListener('change', function(e) {
				console.log(e.target.parentElement);
				e.target.parentElement.submit();
			});
		});
	}


});