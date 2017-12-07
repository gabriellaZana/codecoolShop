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


function getProductId(){
    $(".add-to-cart-button").on('click', function () {
        var clickedProductId = $(this).parents().eq(4).attr("id");
        $.ajax({
            url:'/cart',
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(clickedProductId),
            success: function(response){
                console.log(response);
            }
        })
    })
}

getProductId();


finalPrice = $(".finalPrice");
$(document).ready(function () {
    alert("asd")

})
