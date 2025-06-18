document.addEventListener("DOMContentLoaded", () => {
	let rootElements = document.querySelectorAll('.filterable-fields');
	let dataGrid = document.getElementById('table-div');
	let filtersDiv = document.getElementById('filters-div');
	let showHideFiltersButton = document.getElementById('show-hide-filters');

    function hideFilters() {
        if (rootElements.length) {
            rootElements[0].classList.add('d-none');
        }
        dataGrid.classList.remove('col-9');
        dataGrid.classList.add('col');
        filtersDiv.classList.remove('col-3');
        showHideFiltersButton.innerHTML = 'Εμφάνιση Φίλτρων';
        localStorage.setItem("filtersHidden", "true");
    }

	function showFilters() {
	    if (rootElements.length) {
            rootElements[0].classList.remove('d-none');
        }
        dataGrid.classList.remove('col');
        dataGrid.classList.add('col-9');
        filtersDiv.classList.add('col-3');
        showHideFiltersButton.innerHTML = 'Απόκρυψη Φίλτρων';
        localStorage.setItem("filtersHidden", "false");
	}

    if (localStorage.getItem("filtersHidden") === "true") {
        hideFilters();
    } else if (localStorage.getItem("filtersHidden") === "false") {
        showFilters();
    }

    if (showHideFiltersButton != null) {
        showHideFiltersButton.addEventListener('click', function(e) {
            if (localStorage.getItem("filtersHidden") && localStorage.getItem("filtersHidden") === "false") {
                hideFilters();
            } else if (!localStorage.getItem("filtersHidden") || localStorage.getItem("filtersHidden") === "true") {
                showFilters();
            }
        });
    }

	rootElements.forEach(root => {
		let fields = root.querySelectorAll('.filterable-field');

		let activeFilters = root.querySelectorAll(".active-filter");
		activeFilters.forEach(activeFilter => {
			activeFilter.addEventListener('click', function(e) {
				let formId = activeFilter.dataset.formid;
				document.getElementById(formId).submit();
			});
		});

		fields.forEach(field => {
			field.querySelector(".card-header").addEventListener('click', function(e) {
				if (field.querySelector(".card-body").classList.contains('d-none')) {
					field.querySelector(".card-body").classList.remove('d-none');
					field.querySelector(".card-body").classList.add('d-block');
					field.querySelector(".card-header i.bi").classList.remove('bi-caret-right');
					field.querySelector(".card-header i.bi").classList.add('bi-caret-down');
				} else {
					field.querySelector(".card-body").classList.remove('d-block');
					field.querySelector(".card-body").classList.add('d-none');
					field.querySelector(".card-header i.bi").classList.remove('bi-caret-down');
					field.querySelector(".card-header i.bi").classList.add('bi-caret-right');
				}
			});
		});	
	});
	
	
});