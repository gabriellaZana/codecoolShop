categories = $(".clickable-category");

console.log(categories);
for (var i = 0; i < categories.length; i++) {
    if(i>0){
        document.getElementById(`productsOfCategory${categories[i].id.slice(8)}`).style.display = "none";
    }
    categories[i].addEventListener('click', function () {
        productDiv = document.getElementById(`productsOfCategory${this.id.slice(8)}`);
        console.log(productDiv.style.display);
        if (productDiv.style.display === "none") {
            productDiv.style.display = "";
        } else {
            productDiv.style.display = "none";
        }
    })
}


