categories = $(".clickable-category");

console.log(categories);
for (var i = 0; i < categories.length; i++) {
    categories[i].addEventListener('click', function (event) {
        productDiv = document.getElementById(`productsOfCategory${this.id.slice(8)}`);
        console.log(productDiv.style.display);
        if (productDiv.style.display === "none") {
            productDiv.style.display = "";
        } else {
            productDiv.style.display = "none";
        }
    })
}


