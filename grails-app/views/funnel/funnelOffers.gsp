<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.offers.title"/></title>
    <meta name="layout" content="funnelLayout">
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
                        <h3>Condiciones de compra</h3>
                        <p>Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend reformidans ei. Omnium euismod in pri, eam ei oblique numquam dignissim, vel et erant.</p>
                        <p>Consul legendos expetendis id vis. Ne saperet civibus rationibus has. Enim semper maiestatis no quo, ius at erat tollit. Adhuc epicuri intellegam te est. Solet animal apeirian eu est, malis legendos dissentias te usu. Vim at sint meliore officiis, mucius epicurei vel et. Duo fastidii quaestio an. Vim at veri eripuit fabellas, prompta docendi ea mei. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus.</p>
                    </div>
                </section>
</content>
