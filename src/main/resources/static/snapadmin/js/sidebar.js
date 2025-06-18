document.addEventListener("DOMContentLoaded", () => {
    let sidebar = document.querySelector('.sidebar');
    let sidebarTop = document.querySelector('.sidebar-top');
    let sidebarBottom = document.querySelector('.sidebar-bottom');
    let menuSubheading = document.querySelector('.menu-subheading');
    let menuEntryText = document.querySelectorAll('.menu-entry-text');
    let expandCollapseSidebarButton = document.querySelector('.expand-collapse-sidebar');
    let version = document.querySelector('.version');
    let mainContent = document.querySelector('.main-content');

    function collapseSidebar() {
        sidebar.classList.add("sidebar-collapsed");
        sidebarTop.classList.add("sidebar-collapsed");
        sidebarBottom.classList.add("sidebar-collapsed");
        menuSubheading.classList.add("d-md-none");
        menuEntryText.forEach(e => {
            e.classList.add("d-md-none");
        });
        expandCollapseSidebarButton.innerHTML = "&raquo;";
        expandCollapseSidebarButton.classList.add("sidebar-collapsed");
        version.classList.add("d-none");
        mainContent.classList.add("main-content-expanded");
        localStorage.setItem("sidebarCollapsed", "true");
    }

    function expandSidebar() {
        sidebar.classList.remove("sidebar-collapsed");
        sidebarTop.classList.remove("sidebar-collapsed");
        sidebarBottom.classList.remove("sidebar-collapsed");
        menuSubheading.classList.remove("d-md-none");
        menuEntryText.forEach(e => {
            e.classList.remove("d-md-none");
        });
        expandCollapseSidebarButton.innerHTML = "&laquo;";
        expandCollapseSidebarButton.classList.remove("sidebar-collapsed");
        version.classList.remove("d-none");
        mainContent.classList.remove("main-content-expanded");
        localStorage.setItem("sidebarCollapsed", "false");
    }

	if (localStorage.getItem("sidebarCollapsed") === "true") {
        collapseSidebar();
    } else if (localStorage.getItem("sidebarCollapsed") === "false") {
        expandSidebar();
    }

    if (expandCollapseSidebarButton != null) {
        expandCollapseSidebarButton.addEventListener('click', function(e) {
            if (localStorage.getItem("sidebarCollapsed") && localStorage.getItem("sidebarCollapsed") === "false") {
                collapseSidebar();
            } else if (!localStorage.getItem("sidebarCollapsed") || localStorage.getItem("sidebarCollapsed") === "true") {
                expandSidebar();
            }
        });
    }

});