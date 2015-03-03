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
                            <h3><g:message code="funnel.successfulStories.offers.basicName"/></h3>
                            <g:form mapping="funnelPay" method="post" name="contratar-basica" role="form">
                                <div class="options-wrapper">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="optionsBasica" id="optionsBasica1" value="9,99" checked>
                                            <span><g:message code="funnel.successfulStories.offers.basicPrice1"/><span class="decimals"><g:message code="funnel.successfulStories.offers.basicPriceDec1"/></span> <span class="euro"><g:message code="funnel.successfulStories.offers.currency"/></span></span><g:message code="funnel.successfulStories.offers.month"/><br/><g:message code="funnel.successfulStories.offers.condition"/>
                                        </label>
                                    </div>
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="optionsBasica" id="optionsBasica2" value="14,99">
                                            <span><g:message code="funnel.successfulStories.offers.basicPrice2"/><span class="decimals"><g:message code="funnel.successfulStories.offers.basicPriceDec2"/></span> <span class="euro"><g:message code="funnel.successfulStories.offers.currency"/></span></span><g:message code="funnel.successfulStories.offers.month"/>
                                        </label>
                                    </div>
                                </div>
                                <ul>
                                    <li><g:message code="funnel.successfulStories.offers.basic1"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.basic2"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.basic3"/></li>
                                </ul>
                                <input type="submit" value="Contratar" class="btn btn-grey btn-lg btn-block"></p>
                            </g:form>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-4 active">
                            <h3><g:message code="funnel.successfulStories.offers.premiumName"/></h3>
                            <g:form mapping="funnelPay" method="post" name="contratar-premium" role="form">
                                <div class="options-wrapper">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="optionsPremium" id="optionsPremium1" value="49,99" checked>
                                            <span><g:message code="funnel.successfulStories.offers.premiumPrice1"/><span class="decimals"><g:message code="funnel.successfulStories.offers.premiumPriceDec1"/></span> <span class="euro"><g:message code="funnel.successfulStories.offers.currency"/></span></span><g:message code="funnel.successfulStories.offers.month"/><br/><g:message code="funnel.successfulStories.offers.condition"/>
                                        </label>
                                    </div>
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="optionsPremium" id="optionsPremium2" value="59,99">
                                            <span><g:message code="funnel.successfulStories.offers.premiumPrice2"/><span class="decimals"><g:message code="funnel.successfulStories.offers.premiumPriceDec2"/></span> <span class="euro"><g:message code="funnel.successfulStories.offers.currency"/></span></span><g:message code="funnel.successfulStories.offers.month"/>
                                        </label>
                                    </div>
                                </div>
                                <ul>
                                    <li><g:message code="funnel.successfulStories.offers.premium1"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.premium2"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.premium3"/></li>
                                </ul>
                                <input type="submit" value="Contratar" class="btn btn-grey btn-lg btn-block"></p>
                            </g:form>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-4">
                            <h3><g:message code="funnel.successfulStories.offers.cityhallName"/></h3>
                            <g:form mapping="funnelPay" method="post" name="contratar-ayun" role="form">
                                <div class="options-wrapper">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="optionsAyun" id="optionsAyun1" value="1500" checked class="hidden">
                                            <span><g:message code="funnel.successfulStories.offers.cityhallPrice"/><span><g:message code="funnel.successfulStories.offers.currency"/></span></span><g:message code="funnel.successfulStories.offers.year"/>
                                        </label>
                                    </div>
                                </div>
                                <ul>
                                    <li><g:message code="funnel.successfulStories.offers.cityhall1"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.cityhall2"/></li>
                                    <li><g:message code="funnel.successfulStories.offers.cityhall3"/></li>
                                </ul>
                                <input type="submit" value="Contratar" class="btn btn-grey btn-lg btn-block"></p>
                            </g:form>
                        </div>
                    </div>

                    <div class="condiciones-compra">
                        <h3><g:message code="funnel.offers.purchaseConditions.title"/></h3>
                        <p><g:message code="funnel.offers.purchaseConditions.description1"/></p>
                        <p><g:message code="funnel.offers.purchaseConditions.description2"/></p>
                        <p><g:message code="funnel.offers.purchaseConditions.description3"/></p>
                    </div>
                </section>
</content>
