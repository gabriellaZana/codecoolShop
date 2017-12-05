categories = $(".clickable-category");

console.log(categories);
for (let i = 0; i < categories.length; i++) {
    if (i > 0) {
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

buttons = $('button');
for (let i = 0; i < buttons.length; i++) {
    buttons[i].addEventListener('click', function () {
        button = buttons[i];
        products = button.parent()
        console.log(typeof products)
        supplier = button.innerHTML;

        for (let j = 0; j < products.length; i++) {
            if (supplier === products[j].data('supplier')) {
                products[j].style.display = '';
            } else {
                products[j].style.display = 'none';
            }
        }
    })
}



