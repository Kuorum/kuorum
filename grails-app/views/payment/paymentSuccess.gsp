<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.payment.title"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />

</head>

<content tag="mainContent">

    <h1>GREAT</h1>
    <p>you will be redirect in <span id="remainingSeconds">3</span></p>
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
        })
    </script>
</content>