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
        sandbox:    'ASI6dDgJRqWo1cRY4nPLxGDg2qR6ZAmHdx69bm_PgTiHyVBYI66ga41BC2mm0IRB_s9yL63mfUT78YbQ',
        production: '<insert production client id>'
    },

    payment: function(data, actions) {
        let totalPrice = 0;
        $('.finalPrice').each(function (i, obj) {
            totalPrice += parseFloat(obj.innerHTML);
        });
        console.log(totalPrice);
        return actions.payment.create({
            payment: {
                transactions: [
                    {
                        amount: { total: String(totalPrice), currency: 'USD' }
                    }
                ]
            }
        });
    },

    onAuthorize: function(data, actions) {
        // Make a call to the REST api to execute the payment
        return actions.payment.execute().then(function() {
            window.alert('Payment Complete!');
            $(location).attr('href', '/');
        });
    },

    onCancel: function(data, actions) {
        /*
         * Buyer cancelled the payment
         */
    },

    onError: function(err) {
        /*
         * An error occurred during the transaction
         */
    }
}, '#paypal-button');