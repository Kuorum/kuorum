<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.payment.title"/></title>
    <meta name="layout" content="paymentGatewayFunnel">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />

    <style>
        [data-braintree-id="choose-a-way-to-pay"] {
            position: relative;
        }
    </style>
</head>

<content tag="mainContent">
    %{--<script src="https://js.braintreegateway.com/web/3.16.0/js/client.min.js"></script>--}%
    %{--<script src="path/to/bower_components/braintree-web/paypal.js"></script>--}%
    %{--<script src="path/to/bower_components/braintree-web/data-collector.js"></script>--}%
    <script src="https://js.braintreegateway.com/web/dropin/1.1.0/js/dropin.min.js"></script>

    <h1><g:message code="funnel.payment.gateway.title"/> </h1>
    <p><g:message code="funnel.payment.gateway.cycle" args="[g.message(code:'org.kuorum.rest.model.payment.SubscriptionCycleDTO.'+plan.cycleType),plan.price]"/></p>

    <div class="promotionalCodeSet">
        <p class="big-margin-top"><g:message code="funnel.payment.gateway.discount.title"/></p>
        <input type="text" name="code" class="code" id="code" placeholder="${g.message(code:'funnel.payment.gateway.discount.code.placeHolder')}" aria-required="true">
        <fieldset class="validate">
            <div class="col-xs-12 valid hidden">
                <span></span><i class="fa fa-check fa-2x"></i>
            </div>
        </fieldset>
        <a class="btn btn-blue validateCode" data-ajaxValidator="${g.createLink(mapping: 'paymentPromotionalCodeValidation')}">
            <g:message code="funnel.payment.promotionalCode.validate.button"/>
        </a>
    </div>

    <g:form mapping="paymentGateway" method="POST" name="payment-options" id="payment-options" role="form">
        <input type="hidden" name="subscriptionCycle" value="${plan.getCycleType()}"/>
        <input type="hidden" name="nonce" value=""/>
        <input type="hidden" name="promotionalCode" value=""/>
        <p class="note"><g:message code="funnel.payment.gateway.discount.note"/> </p>
        <div class="payment-method" id="payment-method"></div>
        <a class="btn disabled" id="payment-button"><g:message code="funnel.payment.gateway.button"/> </a>
        <p class="note"><g:message code="register.conditions" args="[g.createLink(mapping: 'footerTermsUse')]"/> </p>
    </g:form>

    <script>
        $(function(){
            gtmPaymentEvent("payment-page-loaded")

            // PAYMENT VALIDATE PROMOTIONAL CODE
            $('#payment div.promotionalCodeSet a.validateCode').on('click', function(e){
                e.preventDefault();
                promotionalCodeValidation();
            });

            braintree.dropin.create({
                authorization: '${token}',
                selector: '#payment-method',
                locale: '${locale.toString()}',
                recurring:true,
                paypal: {
                    flow: 'vault',
//                    displayName:'Kuorum Name',
                    billingAgreementDescription: '${message(code:'funnel.payment.gateway.paypal.agreement')}'
                }
            }, function (err, instance) {
//                console.log("########### PAYMENT FORM SUCCESS ##########")
//                console.log(err)
//                console.log(instance)

                gtmPaymentEvent("payment-platform-loaded")
                if (instance.isPaymentMethodRequestable()) {
                    // This will be true if you generated the client token
                    // with a customer ID and there is a saved payment method
                    // available to tokenize with that customer.
//                    console.log("User has a payment already added")
                    gtmPaymentEvent("payment-readyOnLoad")
                    activateButtonPay();
                }

                instance.on('paymentMethodRequestable', function (event) {
//                    console.log("ADDED TARGET")
                    if (event.type=="CreditCard"){
//                        console.log("Activating credit card")
//                        console.log( $('#payment-button'))
                        gtmPaymentEvent("payment-valid-creditCard")
                        activateButtonPay();
                    }
                    if (event.type=="PayPalAccount"){
                        gtmPaymentEvent("payment-valid-paypal")
                        clickSubmitButton()
                    }
                });

                instance.on('noPaymentMethodRequestable', function (event) {
//                    console.log('noPaymentMethodRequestable');
//                    console.log(event);
                    deactivateButtonPay();
                });

                function clickSubmitButton(e){
                    if (e!= undefined){
                        e.preventDefault();
                    }
                    gtmPaymentEvent("payment-button-submit")
                    pageLoadingOn();
                    instance.requestPaymentMethod(saveRequestPaymentMethod);
                }

                function activateButtonPay(){
                    gtmPaymentEvent("payment-button-active")
                    $('#payment-button').removeClass("disabled");
                    document.getElementById('payment-button').addEventListener('click', clickSubmitButton);
                }

                function deactivateButtonPay(){
                    $('#payment-button').addClass("disabled")
                    document.getElementById('payment-button').removeEventListener('click',clickSubmitButton)

                }
            });


            function saveRequestPaymentMethod(requestPaymentMethodErr, payload) {
                // Submit payload.nonce to your server
                pageLoadingOn();
//                console.log("SUBMIT PAYLOAD")
//                console.log(requestPaymentMethodErr)
//                console.log(payload)
                var url = '${g.createLink(mapping:'paymentGatewaySavePaymentMethod')}'
                if (payload == undefined){
                    console.log(requestPaymentMethodErr.message)
                    gtmPaymentEvent("payment-wrong-paymentMethod")
                    pageLoadingOff();
                }else{
                    gtmPaymentEvent("payment-button-saving-paymentMethod")
                    $.ajax({
                        method:"POST",
                        url: url,
                        data: {nonce: payload.nonce},
                        success: function (data) {
                            if (data.success == true){
                                var $form =$("#payment-options");
                                console.log("== SUCCESS SAVING PAYMENT METHOD AJAX ==");
                                $form.find("input[name=nonce]").val(payload.nonce)
                                gtmPaymentEvent("payment-button-submit-subscription")
                                $form.submit()
                            }else{
                                display.warn("No valid credit card");
                                pageLoadingOff();
                            }
                        },
                        dataType: 'json'
                    });
                }
            }
        });


        function promotionalCodeValidation(){
            //$('fieldset.validate .in-progress').removeClass('hidden');
            var code = $('div.promotionalCodeSet #code').val();
            var url = $('div.promotionalCodeSet a.validateCode').attr('data-ajaxValidator');
            gtmPaymentEvent("payment-button-validate-code-click")
            $.ajax({
                type: "POST",
                url: url,
                data: {code:code},
                success: function (data) {
                    if(data.validator){
                        $('div.promotionalCodeSet a.validateCode').addClass('hidden');
                        $('fieldset.validate .valid').removeClass("hidden");
                        $('div.promotionalCodeSet #code').removeClass("focusError");
                        $('div.promotionalCodeSet #code').addClass("focusValid");
                        $('form#payment-options input[name=promotionalCode]').attr('value', code);
                        $('div.promotionalCodeSet #code').attr('disabled', 'disabled');
                        gtmPaymentEvent("payment-button-validate-code-success")
                    }
                    else{
                        gtmPaymentEvent("payment-button-validate-code-error")
                        $('div.promotionalCodeSet #code').addClass("focusError");
                    }
                }
            });
        }

        function gtmPaymentEvent(step){
            if (typeof(dataLayer) != "undefined"){
                dataLayer.push({
                    'event': 'payment',
                    'pageCategory':'payment-gateway',
                    'label':step,
                    'amount':${plan.price},
                    'cycle':'${plan.getCycleType()}'
                })
            }
        }
    </script>

</content>