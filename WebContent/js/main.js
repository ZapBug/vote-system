// Main JavaScript file for the voting system
document.addEventListener('DOMContentLoaded', function () {
    // Set the current year in the footer
    var currentYearElement = document.getElementById("currentYear");
    if (currentYearElement) {
        currentYearElement.textContent = new Date().getFullYear();
    }
});