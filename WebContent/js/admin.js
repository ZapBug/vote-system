// Admin JavaScript file for the voting system
document.addEventListener('DOMContentLoaded', function () {
    // Set the current year in the footer
    var currentYearElement = document.getElementById("currentYear");
    if (currentYearElement) {
        currentYearElement.textContent = new Date().getFullYear();
    }

    // Initialize tab functionality with pure JavaScript
    var tabLinks = document.querySelectorAll('.nav-tabs a');
    tabLinks.forEach(function (link) {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            // Remove active class from all tabs and tab panes
            var activeTabs = document.querySelectorAll('.nav-tabs li');
            activeTabs.forEach(function (tab) {
                tab.classList.remove('active');
            });

            var activePanes = document.querySelectorAll('.tab-pane');
            activePanes.forEach(function (pane) {
                pane.classList.remove('active', 'in');
            });

            // Add active class to current tab
            this.parentNode.classList.add('active');

            // Show current tab pane
            var target = this.getAttribute('href');
            var targetPane = document.querySelector(target);
            if (targetPane) {
                targetPane.classList.add('active', 'in');
            }
        });
    });
});