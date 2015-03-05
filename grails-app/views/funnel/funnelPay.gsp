<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.payment.title"/></title>
    <meta name="layout" content="noScapeLayout">
</head>

<content tag="mainContent">
    <section id="main" role="main" class="contratando">
        <div class="col-md-8 col-lg-9">
            <h1><g:message code="funnel.payment.description1"/></h1>
            <h2><g:message code="funnel.payment.description2"/></h2>
            <sec:ifLoggedIn>
                <g:render template="funnelPayLogged" model="[offerType:offerType]"/>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <g:render template="funnelPayNoLogged" model="[command:command, offerType:offerType]"/>
            </sec:ifNotLoggedIn>
        </div>
        <div class="col-md-4 col-lg-3">
            <h3><g:message code="funnel.successfulStories.offers.${offerType.group}.name"/></h3>
            <ul>
                <li><g:message code="funnel.successfulStories.offers.${offerType.group}.1"/></li>
                <li><g:message code="funnel.successfulStories.offers.${offerType.group}.2"/></li>
                <li><g:message code="funnel.successfulStories.offers.${offerType.group}.3"/></li>
            </ul>
            <h4><funnel:formatAsElegantPrice value="${totalPrice}"/> <br/><small><g:message code="funnel.successfulStories.offers.yearly.${yearly}"/></small></h4>
        </div>
    </section>
</content>

