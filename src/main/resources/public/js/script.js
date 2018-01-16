function addEventListenerToSupplierButtons() {
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
}


function addEventListenerToCategoryBanners() {
    let categories = $(".clickable-category");
    for (let i = 0; i < categories.length; i++) {
        if (i > 0) {
            document.getElementById(`productsOfCategory${categories[i].id.slice(8)}`).style.display = "none";
        }

        categories[i].addEventListener('click', function () {
            let productDiv = document.getElementById(`productsOfCategory${this.id.slice(8)}`);
            if (productDiv.style.display === "none") {
                productDiv.style.display = "";
            } else {
                productDiv.style.display = "none";

            }
        })
    }
}


function getProductId() {
    $(".add-to-cart-button").on('click', function () {
            let clickedProductId = $(this).parents().eq(4).attr("id");
            $.ajax({
                url: '/shopping-cart',
                type: 'POST',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify(clickedProductId),
                success: function (response) {
                    //console.log("sent")
                    let parsed = $.parseJSON(response);
                    $("#sum").text('Sum: ' + '$' + parsed['sum']);
                    $("#quantity").text(parsed['quantity'] + ' item(s)');
                }
            })
        }
    )
}


function addEventListenerToTrashBin() {
    $(".trash_bin").on("click", function (event) {
        let prodId = event.target.parentNode.parentNode.id;
        $.ajax({
            url: '/delete-item',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(prodId)
        });
        let trash = event.target;
        trash.parentNode.parentNode.parentNode.removeChild(trash.parentNode.parentNode);
        displayTotalPrice();
    });
}


function countFinalPrice() {
    let quantity = $(".quantity");
    let finalPrice = $(".finalPrice");
    let defaultPrice = $(".default_price");
    let productQuantity;
    let productDefaultPrice;
    for (let i = 0; i < defaultPrice.length; i++) {
        productQuantity = quantity[i].value;
        productDefaultPrice = defaultPrice[i].innerHTML;
        finalPrice[i].innerHTML = (productDefaultPrice * productQuantity).toFixed(2);
    }
    displayTotalPrice();
}


function addEventListenerToQuantity() {
    let quantity = $(".quantity");
    quantity.on('change', function() {
        countFinalPrice()
    });
}


function addEventListenerToShopButton() {
    let user_form = document.getElementById("user_form");
    let shop = document.getElementById("shop");
    if (shop) {
        shop.addEventListener('click', function() {
            user_form.style.display = "inline";
        });
    }
}

function displayTotalPrice(){
    let totalPrice = 0;
    $('.finalPrice').each(function (i, obj) {
        console.log(obj.innerHTML);
        totalPrice += parseFloat(obj.innerHTML);
    })

    $('#total_price').text( 'Total price is: ' + totalPrice);
}

function submitClicked(){
    $('#submit').on('click', function () {
        alert('Successful order!');
        $.ajax({
            url: '/submit-cart',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify("asd"),
            success: function (response) {
                $(location).attr('href', '/');
            }
        });

    })
}


function register(){
    $('#register').on('submit', function (event) {
        event.preventDefault();
        let userData = $('#register').serializeArray();
        alert("Successfull reg");
        $.ajax({
            url:'/register',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data:JSON.stringify(userData),
            success: function (response) {
                console.log(response);
            }
        })
    })
}


$(document).ready(function () {
    addEventListenerToSupplierButtons();
    addEventListenerToCategoryBanners();
    addEventListenerToQuantity();
    addEventListenerToTrashBin();
    addEventListenerToShopButton();
    getProductId();
    countFinalPrice();
    displayTotalPrice();
    submitClicked();
    register();
    
});
