$(document).ready(function () {
    let categories = $(".clickable-category");

    for (var i = 0; i < categories.length; i++) {
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
            categories[i].addEventListener('click', function () {
                let productDiv = document.getElementById(`productsOfCategory${this.id.slice(8)}`);
                if (productDiv.style.display === "none") {
                    productDiv.style.display = "";
                } else {
                    productDiv.style.display = "none";
                }
            })
        })
    }

    let buttons = $('button');
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].addEventListener('click', function () {
                let button = $(this);
                let products = button.parent().parent().find(".product");
                let supplier = button.html();
                for (let i = 0; i < products.length; i++) {
                    let product = products[i];
                    if (supplier === "All") {
                        product.style.display = '';
                    } else {
                        let productSuppl = product.getAttribute('data-supplier');
                        if (supplier === productSuppl) {
                            product.style.display = '';
                        } else {
                            product.style.display = 'none';
                        }
                    }
                }
            }
        )
    }


    function getProductId() {
        $(".add-to-cart-button").on('click', function () {
            var clickedProductId = $(this).parents().eq(4).attr("id");
            console.log(clickedProductId.slice(7));
            $.ajax({

                url: '/shopping-cart',
                type: 'POST',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify(clickedProductId),
                success: function (response) {
                    var parsed = $.parseJSON(response);
                    ;
                    $("#sum").text('Sum: ' +'$' + parsed['sum']);
                    $("#quantity").text(parsed['quantity'] + ' item(s)');


                }
            })
        })
    }

    getProductId();

    finalPrice = $(".finalPrice");
    defaultPrice = $(".default_price");
    quantity = $(".quantity");

    function changeFinalPrice() {
        for (let i = 0; i < defaultPrice.length; i++) {
            productQuantity = quantity[i].value;
            console.log(productQuantity)
            productDefaultPrice = defaultPrice[i].innerHTML;
            console.log(productDefaultPrice)
            finalPrice[i].innerHTML = (productDefaultPrice * productQuantity).toFixed(2);

        }
    }
    changeFinalPrice()
    $('.quantity').on('change', function () {
        changeFinalPrice()
    })

    $(".trash_bin").on("click", function (event) {
        prodId = event.target.parentNode.parentNode.id
        console.log(prodId);
        console.log(event.target.parentNode.parentNode.id)
        $.ajax({
            url: '/delete-item',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(prodId)
        })


        trash = event.target;

        trash.parentNode.parentNode.parentNode.removeChild(trash.parentNode.parentNode);
    })

    var user_form = $("#user_form");
    var shop_button = $("#shop")
    shop_button.addEventListener('click', function () {
        console.log("kattintottam");
        if (user_form.style.display === "none") {
            user_form.style.display = "";
        } else {
            user_form.style.display = "none";
        }
    })

});
