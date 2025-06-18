document.addEventListener("DOMContentLoaded", () => {
    let menuSubheading = document.querySelector('.menu-subheading');
    let sidebar = document.querySelector('.sidebar');
    let sidebarTop = document.querySelector('.sidebar-top');
    let sidebarBottom = document.querySelector('.sidebar-bottom');
    let menuEntryText = document.querySelectorAll('.menu-entry-text');
	let version = document.getElementById('version');
	let expandCollapseSidebarButton = document.getElementById('expand-collapse-sidebar');
    let mainContent = document.querySelector('.main-content');

    let x = window.matchMedia("(max-width: 767px)");
    function resize() {
        if (x.matches) {
            expandCollapseSidebarButton.style.width = "53px";
            sidebar.style.width = "53px";
            sidebarTop.style.width = "53px";
            sidebarBottom.style.width = "53px";
            mainContent.style.width = "calc(100% - 53px)";
            version.classList.remove("d-block");
            version.classList.add("d-none");
        } else if (localStorage.getItem("collapsed_sidebar") == "false") {
            expandCollapseSidebarButton.style.width = "200px";
            sidebar.style.width = "200px";
            sidebarTop.style.width = "200px";
            sidebarBottom.style.width = "200px";
            mainContent.style.width = "calc(100% - 200px)";
            version.classList.remove("d-none");
            version.classList.add("d-block");
        }
    }

    function collapseSidebar() {
        menuSubheading.classList.remove("d-md-block");
        menuSubheading.classList.add("d-md-none");
        menuEntryText.forEach(e => {
            e.classList.remove("d-md-block");
            e.classList.add("d-md-none");
        });
        version.classList.remove("d-block");
        version.classList.add("d-none");
        expandCollapseSidebarButton.innerHTML = "&raquo;";
        expandCollapseSidebarButton.style.width = "53px";
        sidebar.style.width = "53px";
        sidebarTop.style.width = "53px";
        sidebarBottom.style.width = "53px";
        mainContent.style.width = "calc(100% - 53px)";
        localStorage.setItem("collapsed_sidebar", "true");
    }

    function expandSidebar() {
        menuSubheading.classList.remove("d-md-none");
        menuSubheading.classList.add("d-md-block");
        menuEntryText.forEach(e => {
            e.classList.remove("d-md-none");
            e.classList.add("d-md-block");
        });
        expandCollapseSidebarButton.innerHTML = "&laquo;";
        localStorage.setItem("collapsed_sidebar", "false");
        resize();
    }

    window.addEventListener('resize', resize, true);

	if (localStorage.getItem("collapsed_sidebar") == "true") {
        collapseSidebar();
    } else if (localStorage.getItem("collapsed_sidebar") == "false") {
        expandSidebar();
    }

    if (expandCollapseSidebarButton != null) {
        expandCollapseSidebarButton.addEventListener('click', function(e) {
            if (!localStorage.getItem("collapsed_sidebar") || localStorage.getItem("collapsed_sidebar") == "false") {
                collapseSidebar();
            } else if (localStorage.getItem("collapsed_sidebar") == "true") {
                expandSidebar();
            }
        });
    }

});