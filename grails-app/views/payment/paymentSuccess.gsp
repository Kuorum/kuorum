<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.subscriptionPaid.title"/></title>
    <meta name="layout" content="paymentGatewayFunnel">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />

</head>

<content tag="mainContent">

    <h1><g:message code="funnel.subscriptionPaid.title"/> </h1>
    <h2><g:message code="funnel.subscriptionPaid.description1"/> </h2>
    <p><g:message code="funnel.subscriptionPaid.description2"/> </p>
    <p><g:message code="funnel.subscriptionPaid.redirect" args="[3, urlRedirectAfterPay]" encodeAs="raw"/></p>
    <script>
        $(function(){
            var seconds =parseInt($("#remainingSeconds").html());
            setTimeout(decreaseSeconds, 1000)

            function decreaseSeconds(){
                seconds = seconds -1;
                $("#remainingSeconds").html(seconds)
                if (seconds == 0){
                    window.location="${urlRedirectAfterPay}";
                }else{
                    setTimeout(decreaseSeconds, 1000);
                }
            }

            if (typeof(dataLayer) != "undefined"){
                dataLayer.push({
                    'event': 'payment',
                    'pageCategory':'payment-success',
                    'label':'success'
                })
            }
        })
    </script>
</content>