<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/>-<g:message code="landingPrices.title"/> </title>
    <meta name="layout" content="landingLayout">
    <parameter name="special-cssClass" value="prices"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingPrices.title'),
                      kuorumDescription:'',
                      kuorumImage:r.resource(dir:'images', file:'background-prices.png', absolute:true)
              ]"/>
</head>

<content tag="special">
    <g:render template="landingPrices/pricesSection"/>
</content>

<content tag="mainContent">
    <g:render template="landingPrices/weceInfo"/>
</content>
