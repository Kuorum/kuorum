<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="noScapeLayout">
    <parameter name="specialContainerCssClass" value="edit-post" />
</head>

<content tag="intro">
</content>

<content tag="mainContent">
    <article class="kakareo post promo" role="article" itemscope itemtype="http://schema.org/Article">
        <h1><g:message code="post.promote.step1.title"/></h1>
        <p><g:message code="post.promote.step1.p1"/></p>
        <div class="row promo-options">
            <g:each in="${prices}" var="price" status="i">
                <div class="col-xs-12 col-sm-12 col-md-4">
                    <div class="link-wrapper">
                        <g:link mapping="postPaiment" params="${post.encodeAsLinkProperties() + [amount:price.price]}" class="hidden">
                            <g:message code="post.promote.step1.pay.${i}.linkTitle" args="[price.price]"/>
                        </g:link>
                        <div class="mails"><span class="fa fa-user"></span>+${price.numMails}</div>
                        <h2><g:message code="post.promote.step1.pay.${i}.title" args="[price.price,price.numMails]"/> </h2>
                        <p><g:message code="post.promote.step1.pay.${i}.description" args="[price.price,price.numMails]"/> </p>
                        <div class="euros">${price.price}€</div>
                        <h3><g:message code="post.promote.step1.pay.promote" args="[price.price,price.numMails]"/></h3>
                        %{--<p><small><g:message code="post.promote.step1.pay.footer" args="[price.price,price.numMails]"/> </small></p>--}%
                    </div>
                </div>
            </g:each>
        </div>

        <div class="form-promo row">
            <h4 class="col-xs-12 col-sm-12 col-md-6"><g:message code="post.promote.step1.pay.custom.title"/> </h4>
            <formUtil:validateForm bean="${command}" form="paimentForm" method="GET"/>
            <g:form mapping="postPaiment" name="paimentForm" class="col-xs-12 col-sm-12 col-md-6" role="form" params="${post.encodeAsLinkProperties()}">
                <div class="form-group">
                    <formUtil:input command="${command}" field="amount" type="number" labelCssClass="sr-only"/>
                </div>
                <button type="submit" class="btn btn-blue btn-lg"><g:message code="post.promote.step1.pay.custom.submit"/> </button>
            </g:form>
        </div>

        <h2><g:message code="post.promote.step1.h2.1.title"/></h2>
        <p><g:message code="post.promote.step1.h2.1.p1"/></p>
        <p><g:message code="post.promote.step1.h2.1.p2"/></p>

        <h2><g:message code="post.promote.step1.h2.2.title"/></h2>
        <p><g:message code="post.promote.step1.h2.2.p1"/></p>
        <p><g:message code="post.promote.step1.h2.2.p2"/></p>

        <h2><g:message code="post.promote.step1.h2.3.title"/></h2>
        <p><g:message code="post.promote.step1.h2.3.p1"/></p>
        <p><g:message code="post.promote.step1.h2.3.p2"/></p>
        <p><g:message code="post.promote.step1.h2.3.p3"/></p>


    </article><!-- /article -->
</content>

<content tag="cColumn">
    <g:render template="/modules/recommendedPosts" model="[recommendedPost:[post], title:message(code:'post.promote.columnC.postPromoted.title'),specialCssClass:'']"/>
</content>