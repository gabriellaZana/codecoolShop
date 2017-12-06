$(document).ready(function () {
    let categories = $(".clickable-category");

    for (let i = 0; i < categories.length; i++) {
        if (i > 0) {
            document.getElementById(`productsOfCategory${categories[i].id.slice(8)}`).style.display = "none";
        }
        categories[i].addEventListener('click', function () {
            let productDiv = document.getElementById(`productsOfCategory${this.id.slice(8)}`);
            console.log(productDiv.style.display);
            if (productDiv.style.display === "none") {
                productDiv.style.display = "";
            } else {
                productDiv.style.display = "none";
            }
        })
    }

    let buttons = $('button');
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].addEventListener('click', function () {
                let button = $(this);
                console.log(typeof button);
                let products = button.parent().parent().find(".product");
                let supplier = button.html();
                for (let i = 0; i < products.length; i++) {
                    let product = products[i];
                    let productSuppl = product.getAttribute('data-supplier');
                    if (supplier === productSuppl) {
                        product.style.display = '';
                    } else {
                        product.style.display = 'none';
                    }
                }
            }
        )
    }
})
;



