<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingPrices.page.title"/></title>
    <meta name="layout" content="landingLayout">
    <parameter name="special-cssClass" value="prices"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingPrices.page.title'),
                      kuorumDescription:g.message(code:'landingPrices.page.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images', file:'background-prices.jpg')
              ]"/>
</head>

<content tag="special">
    <g:render template="landingPrices/pricesSection"/>
</content>

<content tag="mainContent">
    <g:render template="landingPrices/weceInfo"/>
</content>
