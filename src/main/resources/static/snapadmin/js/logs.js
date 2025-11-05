document.addEventListener("DOMContentLoaded", () => {
	let form = document.getElementById('log-filter-form');
	
	if (form == null) return;
	
	let selects = document.querySelectorAll('select');
	selects.forEach(select => {
		select.addEventListener('change', function(e) {
            let input = document.createElement('input');
            input.setAttribute('name', 'page');
            input.setAttribute('value', this.parentElement.querySelector("input[name=\"page\"]").value);
            input.setAttribute('type', 'hidden');
            form.appendChild(input);

            input = document.createElement('input');
            input.setAttribute('name', 'pageSize');
            input.setAttribute('value', e.target.value);
            input.setAttribute('type', 'hidden');
            form.appendChild(input);

            const params = new URLSearchParams(window.location.search);

            if (params.get('sortKey') != null) {
                input = document.createElement('input');
                input.setAttribute('name', 'sortKey');
                input.setAttribute('value', params.get('sortKey'));
                input.setAttribute('type', 'hidden');
                form.appendChild(input);
            }

            if (params.get('sortOrder') != null) {
                input = document.createElement('input');
                input.setAttribute('name', 'sortOrder');
                input.setAttribute('value', params.get('sortOrder'));
                input.setAttribute('type', 'hidden');
                form.appendChild(input);
            }

            form.submit();
		});
	});
});