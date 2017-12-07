categories = $(".clickable-category");

console.log(categories);
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
    })
}


function getProductId() {
    $(".add-to-cart-button").on('click', function () {
        var clickedProductId = $(this).parents().eq(4).attr("id");
        $.ajax({

            url:'/shopping-cart',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(clickedProductId),
            success: function(response){
                var parsed = $.parseJSON(response);;
                $("#sum").text('Sum: ' + parsed['sum'] + 'bitcoin');
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
    console.log("click")
    for (let i = 0; i < defaultPrice.length; i++) {
        productQuantity = quantity[i].value;
        productDefaultPrice = defaultPrice[i].innerHTML;
        finalPrice[i].innerHTML = productDefaultPrice * productQuantity + "Ƀ";

    }
}

$(document).ready(changeFinalPrice());


$(".trash_bin").on("click", function (event) {
    trash=event.target;
    trash.parentNode.parentNode.parentNode.removeChild(trash.parentNode.parentNode)
})



