let price = 0;

paypal.Button.render({
    env: 'sandbox', // Or 'production',

    commit: true, // Show a 'Pay Now' button

    style: {
        color: 'blue',
        size: 'medium',     // small | medium | large | responsive
        label: 'paypal',    // gold | blue | silver | black
        shape: 'rect',      // pill | rect
        tagline: false
    },

    // PayPal Client IDs - replace with your own
    // Create a PayPal app: https://developer.paypal.com/developer/applications/create
    client: {
        sandbox: 'ASI6dDgJRqWo1cRY4nPLxGDg2qR6ZAmHdx69bm_PgTiHyVBYI66ga41BC2mm0IRB_s9yL63mfUT78YbQ',
        production: '<insert production client id>'
    },

    payment: function (data, actions) {
        $.ajax({
            url: 'shopping-cart-total-price',
            type: 'GET',
            contentType: 'application/json; charset=UTF-8',
            async: false,
            success: function (response) {
                price = response;
            },
            error: function (e) {
                console.log(e);
                alert("Sorry, something went wrong!");
            }
        });
        return actions.payment.create({
            payment: {
                transactions: [
                    {
                        amount: {total: price, currency: 'USD'}
                    }
                ]
            }
        });

    },

    onAuthorize: function (data, actions) {
        // Make a call to the REST api to execute the payment
        return actions.payment.execute().then(function () {
            window.alert('Payment Complete!');
            $.ajax({
                url: 'empty-cart',
                type: 'GET',
                contentType: 'application/json; charset=UTF-8',
                success: function(response) {
                    $(location).attr('href', '/');
                }
            })
        });
    },

    onCancel: function (data, actions) {
        alert("Payment cancelled!");
    },

    onError: function (err) {
        if (price === "0.0") {
            alert("There's nothing in your cart!");
        } else {
            console.log(price);
            alert("Sorry, something went wrong!");
        }
    }
}, '#paypal-button');
