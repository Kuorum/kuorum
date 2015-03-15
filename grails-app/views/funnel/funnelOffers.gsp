<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.offers.title"/></title>
    <meta name="layout" content="noScapeLayout">
</head>

<content tag="mainContent">
    <section id="main" role="main" class="pide-cuenta">
            <h1><g:message code="funnel.successfulStories.offers.title"/></h1>
            <h2><g:message code="funnel.successfulStories.offers.subtitle"/></h2>
                    <div class="promo-options clearfix">
                        <div class="col-xs-12 col-sm-12 col-md-4">
                            <h3><g:message code="funnel.successfulStories.offers.basic.name"/></h3>
                            <g:form mapping="funnelPay" method="post" name="contratar-basica" role="form">
                                <div class="options-wrapper">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="offerType" value="${kuorum.core.model.OfferType.BASIC_YEARLY}" checked>
                                            <funnel:formatAsElegantPrice value="${kuorum.core.model.OfferType.BASIC_YEARLY.price}"/>
                                            <g:message code="funnel.successfulStories.offers.month"/><br/>
                                            <g:message code="funnel.successfulStories.offers.condition"/>
                                        </label>
                                    </div>
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="offerType" value="${kuorum.core.model.OfferType.BASIC_MONTHLY}">
                                            <funnel:formatAsElegantPrice value="${kuorum.core.model.OfferType.BASIC_MONTHLY.price}"/>
                                            <g:message code="funnel.successfulStories.offers.month"/>
                                        </label>
                                    </div>
                                </div>
                                <ul>
                                    <li><g:message code="funnel.successfulStories.offers.basic.1"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.basic.2"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.basic.3"/></li>
                                </ul>
                                <input type="submit" value="${g.message(code:'funnel.offers.button')}" class="btn btn-grey btn-lg btn-block"></p>
                            </g:form>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-4 active">
                            <h3><g:message code="funnel.successfulStories.offers.premium.name"/></h3>
                            <g:form mapping="funnelPay" method="post" name="contratar-premium" role="form">
                                <div class="options-wrapper">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="offerType" value="${kuorum.core.model.OfferType.PREMIUM_YEARLY}" checked>
                                            <funnel:formatAsElegantPrice value="${kuorum.core.model.OfferType.PREMIUM_YEARLY.price}"/>
                                            <g:message code="funnel.successfulStories.offers.month"/>
                                            <br/>
                                            <g:message code="funnel.successfulStories.offers.condition"/>
                                        </label>
                                    </div>
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="offerType" value="${kuorum.core.model.OfferType.PREMIUM_MONTHLY}">
                                            <funnel:formatAsElegantPrice value="${kuorum.core.model.OfferType.PREMIUM_MONTHLY.price}"/>
                                            <g:message code="funnel.successfulStories.offers.month"/>
                                        </label>
                                    </div>
                                </div>
                                <ul>
                                    <li><g:message code="funnel.successfulStories.offers.premium.1"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.premium.2"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.premium.3"/></li>
                                </ul>
                                <input type="submit" value="${g.message(code:'funnel.offers.button')}" class="btn btn-grey btn-lg btn-block"></p>
                            </g:form>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-4">
                            <h3><g:message code="funnel.successfulStories.offers.cityHall.name"/></h3>
                            <g:form mapping="funnelPay" method="post" name="contratar-ayun" role="form">
                                <div class="options-wrapper">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="offerType" value="${kuorum.core.model.OfferType.CITY_HALL}" checked class="hidden">
                                        <funnel:formatAsElegantPrice value="${kuorum.core.model.OfferType.CITY_HALL.price}"/>
                                        <g:message code="funnel.successfulStories.offers.year"/>
                                        </label>
                                    </div>
                                </div>
                                <ul>
                                    <li><g:message code="funnel.successfulStories.offers.cityHall.1"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.cityHall.2"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.cityHall.3"/></li>
                                </ul>
                                <input type="submit" value="${g.message(code:'funnel.offers.button')}" class="btn btn-grey btn-lg btn-block"></p>
                            </g:form>
                        </div>
                    </div>

                    <div class="condiciones-compra">
                        <h3><g:message code="funnel.offers.purchaseConditions.title"/></h3>
                        <p></p>
                        <p><strong><g:message code="funnel.offers.purchaseConditions.subtitle1"/></p></strong>
                        <p><g:message code="funnel.offers.purchaseConditions.description1"/></p>
                        <p><g:message code="funnel.offers.purchaseConditions.description2"/></p>
                        <p><strong><g:message code="funnel.offers.purchaseConditions.subtitle2"/></p></strong>
                        <p><g:message code="funnel.offers.purchaseConditions.description3"/></p>
                        <p><g:message code="funnel.offers.purchaseConditions.description4"/></p>
                        <p><strong><g:message code="funnel.offers.purchaseConditions.subtitle3"/></p></strong>
                        <p><g:message code="funnel.offers.purchaseConditions.description5"/></p>
                        <p><g:message code="funnel.offers.purchaseConditions.description6"/></p>
                        <p><g:message code="funnel.offers.purchaseConditions.description7"/></p>
                        <p><strong><g:message code="funnel.offers.purchaseConditions.subtitle4"/></p></strong>
                        <p><g:message code="funnel.offers.purchaseConditions.description8"/></p>
                    </div>
                </section>
</content>
